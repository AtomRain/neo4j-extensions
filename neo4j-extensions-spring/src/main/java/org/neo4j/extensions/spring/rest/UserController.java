package org.neo4j.extensions.spring.rest;

import org.neo4j.extensions.spring.domain.FriendResult;
import org.neo4j.extensions.spring.domain.User;
import org.neo4j.extensions.spring.repository.UserRepository;
import org.neo4j.graphdb.GraphDatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The User controller.
 *
 * @author bradnussbaum
 * @version 0.1.0
 * @since 0.1.0
 */
@Controller
@Path("/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Context
    private GraphDatabaseService db;

    @Context
    private UserRepository userRepository;

    /**
     * @return Status 201 on success.
     */
    @POST
    @Path("/create")
    @Produces({
            MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML
    })
    @Transactional(readOnly = false)
    public Response create(@QueryParam("indexingOn") @DefaultValue("true") Boolean indexingOn) {
        LOGGER.info(String.format("POST /user/create?indexingOn=%s", indexingOn));
        long startTimeTx = System.currentTimeMillis();

        // create user
        User user = new User();
        // create friend 1
        User friend1 = new User();
        // create friend 2
        User friend2 = new User();

        // establish friends for user
        Set<User> friends = new LinkedHashSet<User>();
        friends.add(friend1);
        friends.add(friend2);
        user.setFriends(friends);

        // establish friends for friend 1
        Set<User> friend1Friends = new LinkedHashSet<>();
        friend1Friends.add(user);
        friend1.setFriends(friend1Friends);

        // establish friends for friend 2
        Set<User> friend2Friends = new LinkedHashSet<>();
        friend2Friends.add(user);
        friend2.setFriends(friend2Friends);

        // save the user and cascade friends
        user = userRepository.save(user);

        // lookup to validate being saved
        User userActual = userRepository.findOne(user.getId());
        Set<User> friendsActual = userActual.getFriends();

        // assemble result package
        FriendResult result = new FriendResult();
        result.setUser(userActual);
        result.setFriends(friendsActual);

        // capture times
        long successTimeTx = System.currentTimeMillis();
        long processTimeTx = successTimeTx - startTimeTx;

        // log details
        LOGGER.error(String.format("UserController: TX: userId=%s, processTime=%dms", user.getId(), processTimeTx));

        return Response.status(Response.Status.CREATED).entity(result).build();
    }

}
