package org.neo4j.extensions.spring.indexing;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.helpers.collection.MapUtil;

/**
 * Indexing processor.
 * 
 * 
 * @author bradnussbaum
 * @version 0.1.0
 * 
 * @since 0.1.0
 * 
 */
public class IndexingProcessor implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(IndexingProcessor.class.getName());

    private Status status = Status.WAITING;

    private Mode mode = Mode.REINDEX;

    private GraphDatabaseService graphDb;

    private Integer batchIndex;

    private Integer batchSize;

    private String indexName;

    private String indexKey;

    private String qualifierKey;

    private String qualifierValue;

    private Boolean forNodes;

    /**
     * Constructs with all required data for processing.
     * 
     * @param graphDatabaseService Access to graph database.
     * @param batchIndex The batch number.
     * @param batchSize The batch size.
     * @param indexName The name of the index.
     * @param indexKey The key of the index.
     * @param qualifierKey The qualifier key.
     * @param qualifierValue The qualifier value.
     * @param forNodes Whether for nodes or relationships.
     */
    public IndexingProcessor(GraphDatabaseService graphDatabaseService, Integer batchIndex, Integer batchSize, String indexName,
                             String indexKey, String qualifierKey, String qualifierValue, Boolean forNodes) {
        this.graphDb = graphDatabaseService;
        this.batchIndex = batchIndex;
        this.batchSize = batchSize;
        this.indexName = indexName;
        this.indexKey = indexKey;
        this.qualifierKey = qualifierKey;
        this.qualifierValue = qualifierValue;
        this.forNodes = forNodes;
    }

    public GraphDatabaseService getGraphDatabaseService() {
        return graphDb;
    }

    public Integer getBatchIndex() {
        return batchIndex;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    /**
     * Begins batch data processing.
     */
    @Override
    public void run() {
        // start running
        status = Status.RUNNING;

        // capture start time
        long startTime = System.currentTimeMillis();

        LOGGER.info(String.format("INDEXING: PROCESSOR: status=START, batchNumber=%d, batchSize=%d, startTime=%d", batchIndex, batchSize,
                startTime));

        // initialize the execution engine
        ExecutionEngine executionEngine = new ExecutionEngine(graphDb);

        // process batch data
        Transaction tx = null;
        try {
            tx = graphDb.beginTx();
            LOGGER.info(String.format("INDEXING: TX: status=START, batchNumber=%d, batchSize=%d, startTime=%d", batchIndex, batchSize,
                    startTime));

            // determine whether for nodes or relationships
            if (forNodes) {
                ExecutionResult nodeResult = executionEngine.execute("START n=node(*) RETURN n SKIP {batchIndex} LIMIT {batchSize}",
                        MapUtil.map("batchIndex", batchIndex, "batchSize", batchSize));
                ResourceIterator<Node> nodeIterator = nodeResult.columnAs("n");
                Index<Node> nodesIndex = graphDb.index().forNodes(indexName);
                while (nodeIterator.hasNext()) {
                    Node node = nodeIterator.next();
                    if (node.hasProperty(indexKey)) {
                        Object propertyValue = node.getProperty(indexKey);
                        if (propertyValue != null) {
                            IndexHits<Node> indexHits = nodesIndex.get(indexKey, propertyValue);
                            while (indexHits.hasNext()) {
                                if (node.equals(indexHits.next())) {
                                    nodesIndex.remove(node, indexKey, propertyValue);
                                    LOGGER.info(String.format("INDEXING: TX: type=node, id=%d, property=%s, value=%s, action=remove",
                                            node.getId(), indexKey, propertyValue.toString()));
                                }
                            }

                            // use the qualifier property to determine whether to add this
                            if (node.hasProperty(qualifierKey) && node.getProperty(qualifierKey).toString().equals(qualifierValue)) {
                                // add the key/pair to the index if absent
                                nodesIndex.putIfAbsent(node, indexKey, propertyValue);
                                LOGGER.info(String.format("INDEXING: TX: type=node, id=%d, property=%s, value=%s, action=add",
                                        node.getId(), indexKey, propertyValue.toString()));
                            }

                            // close the index
                            indexHits.close();
                        }
                    }
                }

                // close the iterator
                nodeIterator.close();
            } else {
                ExecutionResult relationshipResult = executionEngine.execute(
                        "start r=relationship(*) return r SKIP {batchIndex} LIMIT {batchSize}",
                        MapUtil.map("batchIndex", batchIndex, "batchSize", batchSize));
                ResourceIterator<Relationship> relationshipIterator = relationshipResult.columnAs("r");
                Index<Relationship> relationshipsIndex = graphDb.index().forRelationships(indexName);
                while (relationshipIterator.hasNext()) {
                    Relationship relationship = relationshipIterator.next();
                    if (relationship.hasProperty(indexKey)) {
                        Object propertyValue = relationship.getProperty(indexKey);
                        if (propertyValue != null) {
                            IndexHits<Relationship> indexHits = relationshipsIndex.get(indexKey, propertyValue);
                            while (indexHits.hasNext()) {
                                if (relationship.equals(indexHits.next())) {
                                    relationshipsIndex.remove(relationship, indexKey, propertyValue);
                                    LOGGER.info(String.format(
                                            "INDEXING: TX: type=relationship, id=%d, property=%s, value=%s, action=remove",
                                            relationship.getId(), indexKey, propertyValue.toString()));
                                }
                            }

                            // use the qualifier property to determine whether to add this
                            if (relationship.hasProperty(qualifierKey) &&
                                relationship.getProperty(qualifierKey).toString().equals(qualifierValue)) {
                                // add the key/pair to the index if absent
                                relationshipsIndex.putIfAbsent(relationship, indexKey, propertyValue);
                                LOGGER.info(String.format("INDEXING: TX: type=relationship, id=%d, property=%s, value=%s, action=add",
                                        relationship.getId(), indexKey, propertyValue.toString()));
                            }

                            // close the index
                            indexHits.close();
                        }
                    }
                }

                // close the iterator
                relationshipIterator.close();
            }

            // successful transaction
            tx.success();
            long successTime = System.currentTimeMillis();
            long processTime = successTime - startTime;
            LOGGER.info(String.format(
                    "INDEXING: TX: status=SUCCESS, batchNumber=%d, batchSize=%d, startTime=%d, successTime=%d, processTime=%d", batchIndex,
                    batchSize, startTime, successTime, processTime));
        } catch (Exception e) {
            // fail transaction
            tx.failure();
            long failureTime = System.currentTimeMillis();
            long processTime = failureTime - startTime;
            LOGGER.log(Level.SEVERE, String.format(
                    "INDEXING: TX: status=FAILURE, batchNumber=%d, batchSize=%d, startTime=%d, failureTime=%d, processTime=%d", batchIndex,
                    batchSize, startTime, failureTime, processTime), e);
            throw new WebApplicationException(e);
        } finally {
            if (tx != null) {
                tx.finish();
            }
        }

        // calculate process time
        long endTime = System.currentTimeMillis();
        long processTime = endTime - startTime;

        LOGGER.info(String.format(
                "INDEXING: PROCESSOR: status=COMPLETE, batchNumber=%d, batchSize=%d, startTime=%d, endTime=%d, processTime=%d", batchIndex,
                batchSize, startTime, endTime, processTime));

        // start running
        status = Status.COMPLETED;
    }

    public Status getStatus() {
        return status;
    }

    public Mode getMode() {
        return mode;
    }

    /**
     * Status of the processor.
     */
    public enum Status {

        WAITING, RUNNING, COMPLETED, FAILED;

    }

    /**
     * Mode of the processor.
     * 
     * REINDEX - re-index property by qualifier
     * 
     */
    public enum Mode {

        REINDEX;

    }

}
