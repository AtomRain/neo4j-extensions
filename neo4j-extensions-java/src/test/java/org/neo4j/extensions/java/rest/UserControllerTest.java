package org.neo4j.extensions.java.rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.extensions.common.domain.FriendResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.server.CommunityNeoServer;
import org.neo4j.server.helpers.CommunityServerBuilder;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author bradnussbaum
 * @since 2014-05-25
 */
public class UserControllerTest {

    private static final Logger LOGGER = Logger.getLogger(UserControllerTest.class.getName());

    Properties properties = new Properties();
    InputStream input = null;

    private String neo4jServerPort = System.getProperty("neo4j.server.port");

    private String neo4jRemoteShellPort = System.getProperty("neo4j.remoteShell.port");

    private String neo4jGraphDb = System.getProperty("neo4j.graph.db");

    private GraphDatabaseService db;
    private CommunityNeoServer server;

    @Before
    public void before() throws IOException {
        try {
            input = UserControllerTest.class.getClassLoader().getResourceAsStream("maven.properties");
            // load the properties file
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // allow system properties to override
        if (neo4jServerPort == null) {
            neo4jServerPort = properties.getProperty("neo4j.server.port");
        }
        if (neo4jRemoteShellPort == null) {
            neo4jRemoteShellPort = properties.getProperty("neo4j.remoteShell.port");
        }
        if (neo4jGraphDb == null) {
            neo4jGraphDb = properties.getProperty("neo4j.graph.db");
        }

        LOGGER.info(String.format("neo4jServerPort: %s)", neo4jServerPort));
        LOGGER.info(String.format("neo4jRemoteShellPort: %s)", neo4jRemoteShellPort));
        LOGGER.info(String.format("neo4jGraphDb: %s)", neo4jGraphDb));

        // build the server
        server = CommunityServerBuilder
                .server()
                .usingDatabaseDir(neo4jGraphDb)
                .onPort(Integer.valueOf(neo4jServerPort))
                .withProperty("remote_shell_port", neo4jRemoteShellPort)
                .withDefaultDatabaseTuning()
                .withThirdPartyJaxRsPackage("org.neo4j.extensions.java", "/extensions-java")
                .build();
        server.start();
        db = server.getDatabase().getGraph();
    }

    @After
    public void after() {
        server.stop();
    }

    @Test
    public void testCreateUser() {

        ClientResponse response = jerseyClient()
                .resource(server.baseUri().toString() + "extensions-java/user/create?indexingOn=true")
                .accept(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class);
        FriendResult result = response.getEntity(FriendResult.class);

        Assert.assertNotNull(result.getUser());
        Assert.assertNotNull(result.getFriends());
        Transaction tx = db.beginTx();
        Node node = db.getNodeById(result.getUser().getId());
        Assert.assertNotNull(node.getProperty("createdTime").toString());
        Assert.assertEquals(node.getProperty("createdTime").toString(), result.getUser().getCreatedTime().toString());
        tx.success();
        tx.close();
    }

    private Client jerseyClient() {
        DefaultClientConfig defaultClientConfig = new DefaultClientConfig();
        defaultClientConfig.getClasses().add(JacksonJsonProvider.class);
        return Client.create(defaultClientConfig);
    }

}
