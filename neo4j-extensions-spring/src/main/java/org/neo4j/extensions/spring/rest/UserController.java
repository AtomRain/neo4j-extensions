package org.neo4j.extensions.spring.rest;

import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.extensions.common.client.UserClient;
import org.neo4j.extensions.spring.domain.FriendResult;
import org.neo4j.extensions.spring.domain.User;
import org.neo4j.extensions.spring.domain.UserProxy;
import org.neo4j.extensions.spring.domain.UsersResult;
import org.neo4j.extensions.spring.repository.UserRepository;
import org.neo4j.extensions.spring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * The User controller.
 *
 * @author bradnussbaum
 * @version 0.1.0
 * @since 0.1.0
 */
@Path("/user")
public class UserController implements UserClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Context
    private UserRepository userRepository;

    @Context
    private UserService userService;

    /**
     * @return Status 201 on success.
     */
    @Transactional(readOnly = false)
    public Response create(Boolean indexingOn, Integer count) {
        LOGGER.info(String.format("POST /user/create?indexingOn=%s&count=%d", indexingOn, count));
        long startTimeTx = System.currentTimeMillis();

        // create user
        User user = new User();
        user.setUsername(UUID.randomUUID().toString());
        // create friend 1
        User friend1 = new User();
        friend1.setUsername(UUID.randomUUID().toString());
        // create friend 2
        User friend2 = new User();
        friend2.setUsername(UUID.randomUUID().toString());

        // establish friends for user
        Set<User> friends = new LinkedHashSet<>();
        friends.add(friend1);
        friends.add(friend2);
        user.setFriends(friends);

        // save the user and cascade friends
        user = userRepository.save(user);

        // lookup to validate being saved
        User userActual = userRepository.findOne(user.getId());
        Collection<User> friendsActual = userActual.getFriends();

        // assemble result package
        ObjectMapper mapper = new ObjectMapper();
        UserProxy userActualProxy = mapper.convertValue(userActual, UserProxy.class);

        FriendResult result = new FriendResult();
        result.setUser(userActualProxy);
        result.setFriends(userActualProxy.getFriends());

        // capture times
        long successTimeTx = System.currentTimeMillis();
        long processTimeTx = successTimeTx - startTimeTx;

        // log details
        LOGGER.info(String.format("UserController: TX: userId=%s, processTime=%dms", user.getId(), processTimeTx));

        return Response.status(Response.Status.CREATED).entity(result).build();
    }

    /**
     * @return Status 200 on success.
     */
    @Transactional(readOnly = true)
    public Response findUsers(Integer page, Integer pageSize, Integer pages) {
        try {
            Collection<User> users = userService.findUsersAsync(page, pageSize, pages);
            UsersResult usersResult = new UsersResult();
            Set<UserProxy> userProxies = new LinkedHashSet<>();
            for (User user : users) {
                // assemble result package
                ObjectMapper mapper = new ObjectMapper();
                UserProxy userProxy = mapper.convertValue(user, UserProxy.class);
                userProxies.add(userProxy);
            }
            usersResult.setUsers(userProxies);
            return Response.ok(usersResult).build();
        } catch (Exception e) {
            throw new WebApplicationException(e);
        }
    }

}
