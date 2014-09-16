package org.neo4j.extensions.spring.rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.extensions.spring.domain.FriendResult;
import org.neo4j.extensions.spring.domain.UsersResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.server.CommunityNeoServer;
import org.neo4j.server.configuration.Configurator;
import org.neo4j.server.helpers.CommunityServerBuilder;
import org.neo4j.server.helpers.LoggingFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * @author bradnussbaum
 * @since 2014-05-25
 */
@Configurable
@ContextConfiguration("classpath*:META-INF/spring/test-springContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class UserControllerTest {

    private static final Logger LOGGER = Logger.getLogger(UserControllerTest.class.getName());

    @Value("${neo4j.server.port}")
    private Integer neo4jServerPort;

    @Value("${neo4j.remoteShell.port}")
    private Integer neo4jRemoteShellPort;

    @Value("${neo4j.graph.db}")
    private String neo4jGraphDb;

    private GraphDatabaseService db;
    private CommunityNeoServer server;

    @Before
    public void before() throws IOException {
        LOGGER.info(String.format("neo4jServerPort: %d", neo4jServerPort));
        LOGGER.info(String.format("neo4jRemoteShellPort: %d", neo4jRemoteShellPort));
        LOGGER.info(String.format("neo4jGraphDb: %s", neo4jGraphDb));

        server = CommunityServerBuilder
                .server(LoggingFactory.DEFAULT_LOGGING.create(Configurator.EMPTY))
                .usingDatabaseDir(neo4jGraphDb)
                .onPort(neo4jServerPort)
                .withProperty("remote_shell_port", neo4jRemoteShellPort.toString())
                .withDefaultDatabaseTuning()
                .withThirdPartyJaxRsPackage("org.neo4j.extensions.spring", "/extensions-spring")
                .build();
        server.start();
        db = server.getDatabase().getGraph();
    }

    @After
    public void after() {
        server.stop();
    }

    @Test
    public void testCreateUser() throws Exception {

        ClientResponse response = jerseyClient()
                .resource(server.baseUri().toString() + "extensions-spring/user/create?indexingOn=true")
                .accept(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class);
        InputStream streamingOutput = response.getEntityInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        FriendResult result = objectMapper.readValue(streamingOutput, FriendResult.class);

        Assert.assertNotNull(result.getUser());
        Assert.assertNotNull(result.getFriends());

        try (Transaction tx = db.beginTx()) {
            Node node = db.getNodeById(result.getUser().getId());
            Assert.assertNotNull(node.getProperty("createdTime").toString());
            Assert.assertEquals(node.getProperty("createdTime").toString(), result.getUser().getCreatedTime().toString());
            tx.success();
        }
    }

    @Test
    public void testFindUsers() throws Exception {

        ClientResponse response = jerseyClient()
                .resource(server.baseUri().toString() + "extensions-spring/user/pages?page=0&page.size=10&pages=10")
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        InputStream streamingOutput = response.getEntityInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        UsersResult result = objectMapper.readValue(streamingOutput, UsersResult.class);
        Assert.assertNotNull(result);
    }

    private Client jerseyClient() {
        DefaultClientConfig defaultClientConfig = new DefaultClientConfig();
        defaultClientConfig.getClasses().add(JacksonJsonProvider.class);
        return Client.create(defaultClientConfig);
    }

}
