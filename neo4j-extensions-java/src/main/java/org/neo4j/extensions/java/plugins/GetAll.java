package org.neo4j.extensions.java.plugins;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.server.plugins.*;
import org.neo4j.tooling.GlobalGraphOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bradnussbaum
 * @since 2014-05-25
 */
@Description("An extension to the Neo4j Server for getting all nodes or relationships")
public class GetAll extends ServerPlugin {

    @Name("get_all_nodes")
    @Description("Get all nodes from the Neo4j graph database")
    @PluginTarget(GraphDatabaseService.class)
    public Iterable<Node> getAllNodes(@Source GraphDatabaseService graphDb) {
        ArrayList<Node> nodes = new ArrayList<>();
        try (Transaction tx = graphDb.beginTx()) {
            for (Node node : GlobalGraphOperations.at(graphDb).getAllNodes()) {
                nodes.add(node);
            }
            tx.success();
        }
        return nodes;
    }

    @Description("Get all relationships from the Neo4j graph database")
    @PluginTarget(GraphDatabaseService.class)
    public Iterable<Relationship> getAllRelationships(@Source GraphDatabaseService graphDb) {
        List<Relationship> rels = new ArrayList<>();
        try (Transaction tx = graphDb.beginTx()) {
            for (Relationship rel : GlobalGraphOperations.at(graphDb).getAllRelationships()) {
                rels.add(rel);
            }
            tx.success();
        }
        return rels;
    }
}