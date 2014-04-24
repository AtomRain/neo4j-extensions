package org.neo4j.extensions.spring.rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.server.CommunityNeoServer;
import org.neo4j.server.helpers.CommunityServerBuilder;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Configurable
@ContextConfiguration("classpath*:META-INF/spring/test-springContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DeployTest {

    @Value("${neo4j.server.port}")
    private Integer neo4jServerPort;

    @Value("${neo4j.remoteShell.port}")
    private Integer neo4jRemoteShellPort;

    private GraphDatabaseAPI db;
    private CommunityNeoServer server;

    @Before
    public void before() throws IOException {

        server = CommunityServerBuilder
                .server()
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
    public void shouldCreateUser() {

        JsonNode response = jerseyClient()
                .resource(server.baseUri().toString() + "extensions-spring/user/create?indexingOn=false")
                .accept(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class)
                .getEntity(JsonNode.class);

        Assert.assertNotNull(response.get("user").asText());
        Assert.assertNotNull(response.get("friends").asText());
        Transaction tx = db.beginTx();
        Node node = db.getNodeById(response.get("user").get("id").asLong());
        Assert.assertNotNull(node.getProperty("createdTime").toString());
        Assert.assertEquals(node.getProperty("createdTime").toString(), response.get("user").get("createdTime").asText());
        tx.success();
        tx.close();
    }

    private Client jerseyClient() {
        DefaultClientConfig defaultClientConfig = new DefaultClientConfig();
        defaultClientConfig.getClasses().add(JacksonJsonProvider.class);
        return Client.create(defaultClientConfig);
    }

}
