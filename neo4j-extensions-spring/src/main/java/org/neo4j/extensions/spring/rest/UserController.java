package org.neo4j.extensions.spring.rest;

import org.neo4j.extensions.spring.domain.FriendResult;
import org.neo4j.extensions.spring.domain.User;
import org.neo4j.extensions.spring.repository.UserRepository;
import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @Context
    private GraphDatabaseService db;

    /**
     * @return Status 201 on success.
     */
    @POST
    @Path("/create")
    @Produces({
            MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML
    })
    @Transactional
    public Response create(@Context UserRepository userRepository, @QueryParam("indexingOn") @DefaultValue("true") Boolean indexingOn) {
        LOGGER.info(String.format("POST /user/create?indexingOn=%s", indexingOn));
        long startTimeTx = System.currentTimeMillis();

        // create user
        User user = new User();
        // establish user UID
        String userUid = UUID.randomUUID().toString();
        user.setUid(userUid);

        // create friend 1
        User friend1 = new User();
        // establish friend 1 UID
        String friend1Uid = UUID.randomUUID().toString();
        friend1.setUid(friend1Uid);

        // create friend 2
        User friend2 = new User();
        // establish friend 2 UID
        String friend2Uid = UUID.randomUUID().toString();
        friend2.setUid(friend2Uid);

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
        Set<User> friend2Friends = new LinkedHashSet<User>();
        friend2Friends.add(user);
        friend2.setFriends(friend2Friends);

        // save the user and cascade friends
        userRepository.save(user);

        // assemble result package
        FriendResult result = new FriendResult();
        result.setUser(user);
        result.setFriends(friends);

        // capture times
        long successTimeTx = System.currentTimeMillis();
        long processTimeTx = successTimeTx - startTimeTx;

        // log details
        LOGGER.log(Level.INFO,
                String.format("UserController: TX: userId=%s, userUid=%s, processTime=%dms", user.getId(), user.getUid(), processTimeTx));

        return Response.status(Response.Status.CREATED).entity(result).build();
    }

}
