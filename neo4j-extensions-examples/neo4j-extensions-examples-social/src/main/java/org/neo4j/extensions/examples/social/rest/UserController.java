package org.neo4j.extensions.examples.social.rest;

import org.neo4j.extensions.common.client.UserClient;
import org.neo4j.extensions.examples.social.domain.FriendResult;
import org.neo4j.extensions.examples.social.domain.User;
import org.neo4j.extensions.examples.social.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.Path;
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
        FriendResult result = new FriendResult();
        result.setUser(userActual);
        result.setFriends(friendsActual);

        // capture times
        long successTimeTx = System.currentTimeMillis();
        long processTimeTx = successTimeTx - startTimeTx;

        // log details
        LOGGER.info(String.format("UserController: TX: userId=%s, processTime=%dms", user.getId(), processTimeTx));

        return Response.status(Response.Status.CREATED).entity(result).build();
    }

}
