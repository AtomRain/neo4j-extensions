package org.neo4j.extensions.java.common;

import java.net.URLEncoder;
import java.util.LinkedList;

import javax.ws.rs.core.Context;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.helpers.collection.MapUtil;

public class NodeWrapper {

    @Context
    static GraphDatabaseService db;

    private Node node;

    public NodeWrapper(Node node) {
        this.node = node;
    }

    public static Index<Node> getIndex() {
        return getDB().index().forNodes("nodes", MapUtil.stringMap(IndexManager.PROVIDER, "lucene", "type", "fulltext"));
    }

    public static GraphDatabaseService getDB() {
        return db;
    }

    public static void setDB(GraphDatabaseService value) {
        db = value;
    }

    public Node getNode() {
        return node;
    }

    public long getId() {
        return node.getId();
    }

    public static String facetize(String value) {
        try {
            return URLEncoder.encode(value.trim().toLowerCase(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static NodeWrapper findSingleByIndexedAttribute(String name, Object value, String type) {
        if (value instanceof String) {
            if ((name.equals("name") || name.equals("description"))) {
                // Name and description are not facetized
                value = ((String) value).toLowerCase();
            } else {
                // Assume facetization for other attributes
                value = facetize(((String) value));
            }
        }

        LinkedList<NodeWrapper> result = new LinkedList<NodeWrapper>();
        for (Node node : getIndex().get(name, value)) {
            if (node.hasProperty("type") && node.getProperty("type").equals(type)) {
                result.add(new NodeWrapper(node));
            }
        }
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    public static NodeWrapper getOrCreateSubReferenceNode(RelationshipType type) {
        Node root = getDB().getNodeById(0);
        Relationship rel = root.getSingleRelationship(type, Direction.OUTGOING);
        if (rel != null) {
            return new NodeWrapper(rel.getOtherNode(root));
        }
        Transaction txn = getDB().beginTx();
        Node subref = null;
        try {
            subref = getDB().createNode();
            root.createRelationshipTo(subref, type);
            txn.success();
        } catch (Exception e) {
            txn.failure();
        } finally {
            txn.finish();
        }
        if (subref != null) {
            return new NodeWrapper(subref);
        }
        return null;
    }

    public static NodeWrapper createTypedNode(RelationshipTypes root) {
        NodeWrapper subref = getOrCreateSubReferenceNode(root);
        Node node = getDB().createNode();
        subref.getNode().createRelationshipTo(node, RelationshipTypes.MEMBER);
        return new NodeWrapper(node);
    }

    public Object getProperty(String name) {
        return node.hasProperty(name) ? node.getProperty(name) : null;
    }

    public void setProperty(String name, Object value, boolean indexed) {
        Index<Node> index = getIndex();
        Node node = getNode();
        Object normalized = null;
        Object untokenized = null;

        if (node.hasProperty(name)) {
            normalized = node.getProperty(name);

            if (normalized instanceof String) {
                if (name.equals("name") || name.equals("description")) {
                    normalized = ((String) normalized).toLowerCase();
                    untokenized = facetize((String) normalized);
                } else {
                    normalized = facetize((String) normalized);
                }
            }
            try {
                index.remove(node, name, normalized);
                if (untokenized != null) {
                    index.remove(node, name + "_ut", untokenized);
                }
            } catch (NullPointerException e) {}
        }

        if (value == null) {
            if (node.hasProperty(name)) {
                node.removeProperty(name);
            }
            return;
        }

        node.setProperty(name, value);
        if (value instanceof String) {
            if (name.equals("name") || name.equals("description")) {
                normalized = ((String) value).toLowerCase();
                untokenized = facetize((String) value);
            } else {
                normalized = facetize((String) value);
            }
            index.add(node, name, normalized);
            if (untokenized != null) {
                index.add(node, name + "_ut", untokenized);
            }
        } else {
            index.add(node, name, value);
        }
    }

    public void indexFullTextFacet(String name, String value) {
        getIndex().add(node, name, value);
    }

    public void unIndexFullTextFacet(String name, String value) {
        getIndex().remove(node, name, value);
    }

    public void unIndexFullTextFacet(String name) {
        getIndex().remove(node, name);
    }

    public void indexFacet(String name, String value) {
        getIndex().add(node, name, facetize(value));
    }

    public void unindexFacet(String name, String value) {
        getIndex().remove(node, name, facetize(value));
    }

    public void unindexFacet(String name) {
        getIndex().remove(node, name);
    }

    @Override
    public int hashCode() {
        return (int) getId();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof NodeWrapper)) {
            return false;
        }
        return ((NodeWrapper) other).getId() == getId();
    }

}
