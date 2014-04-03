package org.neo4j.extensions.spring.rest;


import java.io.IOException;
import java.net.ServerSocket;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.server.CommunityNeoServer;
import org.neo4j.server.helpers.CommunityServerBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.DefaultClientConfig;

@SuppressWarnings("deprecation")
public class DeployTest {
    private GraphDatabaseAPI db;
    private CommunityNeoServer server;

    @Before
    public void before() throws IOException {
        ServerSocket serverSocket = new ServerSocket(0);

        server = CommunityServerBuilder
                .server()
                .onPort(serverSocket.getLocalPort())
                .withDefaultDatabaseTuning()
                .withThirdPartyJaxRsPackage("org.neo4j.extensions.spring", "/extensions-spring")
                .build();
        server.start();
        db = server.getDatabase().getGraph();
        serverSocket.close();
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
