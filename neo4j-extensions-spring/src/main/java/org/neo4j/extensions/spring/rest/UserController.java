package org.neo4j.extensions.spring.rest;

import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.extensions.common.client.UserClient;
import org.neo4j.extensions.spring.domain.FriendResult;
import org.neo4j.extensions.spring.domain.User;
import org.neo4j.extensions.spring.service.UserService;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

/**
 * The User controller.
 *
 * @author bradnussbaum
 * @version 0.1.0
 * @since 0.1.0
 */
@Path("/user")
public class UserController implements UserClient {

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @Context
    private UserService userService;

    /**
     * @return Status 201 on success.
     */
    public Response create(Boolean indexingOn, Integer count) {
        LOGGER.info(String.format("POST /user/create?indexingOn=%s&count=%d", indexingOn, count));
        long startTimeTx = System.currentTimeMillis();

        User user = userService.createUser(indexingOn, count);

        FriendResult result = new FriendResult();
        result.setUser(user);
        result.setFriends(user.getFriends());

        // capture times
        long successTimeTx = System.currentTimeMillis();
        long processTimeTx = successTimeTx - startTimeTx;

        // log details
        LOGGER.info(String.format("UserController: TX: userId=%s, processTime=%dms", user.getId(), processTimeTx));

        return Response.status(Response.Status.CREATED).entity(writeEntity(result)).build();
    }

    /**
     * @return Status 200 on success.
     */
    public Response findUsers(Integer page, Integer pageSize, Integer pages) {
        try {
            User[] users = userService.findUsers(page, pageSize, pages);
            return Response.ok(writeEntity(users)).build();
        } catch (Exception e) {
            throw new WebApplicationException(e);
        }
    }

    private StreamingOutput writeEntity(final Object entity) {
        StreamingOutput streamingOutput = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(output, entity);
            }
        };
        return streamingOutput;
    }

}
