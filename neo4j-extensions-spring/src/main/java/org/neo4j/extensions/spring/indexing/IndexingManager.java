package org.neo4j.extensions.spring.indexing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.neo4j.graphdb.GraphDatabaseService;

/**
 * Indexing manager.
 * 
 * 
 * @author bradnussbaum
 * @version 0.1.0
 * 
 * @since 0.1.0
 * 
 */
public class IndexingManager implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(IndexingManager.class.getName());

    public static final String DEFAULT_MAX_CONCURRENT = "6";

    public static final String DEFAULT_MAX_THREADS = "4";

    public static final String DEFAULT_BATCH_SIZE = "6000";

    public static volatile Integer MAX_CONCURRENT = Integer.valueOf(DEFAULT_MAX_CONCURRENT);

    public static volatile Integer MAX_THREADS = Integer.valueOf(DEFAULT_MAX_THREADS);

    public static volatile Integer BATCH_SIZE = Integer.valueOf(DEFAULT_BATCH_SIZE);

    private volatile Boolean continueProcessing = true;

    private volatile Boolean forceStop = false;

    private volatile ExecutorService executorService;

    private Status status = Status.WAITING;

    private volatile List<IndexingProcessor> processors = new ArrayList<IndexingProcessor>();

    private GraphDatabaseService graphDb;

    private Integer startIndex;

    private String indexName;

    private String indexKey;

    private String qualifierKey;

    private String qualifierValue;

    private Boolean forNodes;

    /**
     * Constructs with all required data for processing.
     * 
     * @param graphDatabaseService Access to graph database.
     * @param startIndex The start index (line).
     * @param indexName The name of the index.
     * @param indexKey The key of the index.
     * @param qualifierKey The qualifier key.
     * @param qualifierValue The qualifier value.
     * @param forNodes Whether for nodes or relationships.
     */
    public IndexingManager(GraphDatabaseService graphDatabaseService, Integer startIndex, String indexName, String indexKey,
                           String qualifierKey, String qualifierValue, Boolean forNodes) {
        this.graphDb = graphDatabaseService;
        this.startIndex = startIndex;
        this.indexName = indexName;
        this.indexKey = indexKey;
        this.qualifierKey = qualifierKey;
        this.qualifierValue = qualifierValue;
        this.forNodes = forNodes;
    }

    /**
     * @return The graph database.
     */
    public GraphDatabaseService getGraphDatabaseService() {
        return graphDb;
    }

    /**
     * Starts re-indexing batch processing.
     */
    @Override
    public void run() {
        // start running
        status = Status.RUNNING;

        // track process continuation
        continueProcessing = true;

        // track batch index (line)
        int batchIndex = startIndex;

        // create a fixed thread pool with max threads allowed
        executorService = Executors.newFixedThreadPool(MAX_THREADS);

        long startTime = System.currentTimeMillis();
        LOGGER.info(String.format("INDEXING: MANAGER: status=STARTED, startTime=%d", startTime));

        // check for interruptions and stops before continuing processing
        while (continueProcessing) {

            long sleepStart = System.currentTimeMillis();

            // check for max concurrent limit
            while (continueProcessing && updateConcurrent() >= MAX_CONCURRENT) {
                // wait for a thread to open up
                try {
                    LOGGER.info(String.format("INDEXING: MANAGER: status=SLEEPING, batchIndex=%d", batchIndex));
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
                long sleepTime = System.currentTimeMillis() - sleepStart;
                LOGGER.info(String.format("INDEXING: MANAGER: status=AWAKE, batchIndex=%d, sleepTime=%d", batchIndex, sleepTime));
            }

            // check for process stop request
            if (!continueProcessing) {
                break;
            }

            // track parser index (line)
            int index = 0;

            // parse the factual data file and create nodes
            try {
                LOGGER.info(String.format("INDEXING: MANAGER: status=PROCESSING, index=%d", index));

                // keep processing until all threads are consumed (greedy)
                while (continueProcessing && updateConcurrent() < MAX_CONCURRENT) {
                    // start process next batch

                    // start batch processing
                    try {
                        // check for end of the index

                        // create a new thread to process
                        LOGGER.info(String.format("INDEXING: MANAGER: status=PROCESSING, index=%d, batchIndex=%d", index, batchIndex));
                        IndexingProcessor processor = new IndexingProcessor(graphDb, batchIndex, BATCH_SIZE, indexName, indexKey,
                                qualifierKey, qualifierValue, forNodes);
                        processors.add(processor);
                        executorService.execute(processor);
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE,
                                String.format("INDEXING: MANAGER: status=FAILURE, index=%d, batchNumber=%d", index, batchIndex), e);
                    }


                }

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "INDEXING: MANAGER: status=FAILURE", e);
            }

            // update batch index for next round
            batchIndex = index;
        }

        // track processing times
        long endTime = System.currentTimeMillis();
        long processTime = endTime - startTime;
        LOGGER.info(String.format("INDEXING: MANAGER: status=COMPLETE, startTime=%d, endTime=%d, processTime=%d", startTime, endTime,
                processTime));

        if (!forceStop) {
            LOGGER.info("INDEXING: MANAGER: status=SUCCESS");
            // shutdown the service
            executorService.shutdown();
        } else {
            LOGGER.info("INDEXING: MANAGER: status=FORCE_STOP");
        }

        // cleanup
        executorService = null;
        processors = new ArrayList<IndexingProcessor>();

        // start running
        status = Status.COMPLETED;
    }

    /**
     * Concurrent processors. Removes completed.
     * 
     * @return Total concurrent processors.
     */
    public synchronized int updateConcurrent() {
        // find all concurrent
        // mark completed for removal
        int concurrent = 0;
        List<IndexingProcessor> completed = new ArrayList<IndexingProcessor>();
        for (IndexingProcessor processor : processors) {
            if (processor.getStatus().equals(IndexingProcessor.Status.COMPLETED)) {
                completed.add(processor);
            } else {
                concurrent++;
            }
        }

        // remove all completed
        if (completed.size() > 0) {
            processors.removeAll(completed);
        }

        // total concurrent
        return concurrent;
    }

    public Boolean isRunning() {
        return status.equals(Status.RUNNING);
    }

    public Boolean isWaiting() {
        return status.equals(Status.WAITING);
    }

    public Boolean isCompleted() {
        return status.equals(Status.COMPLETED);
    }

    public Boolean isForceStopped() {
        return forceStop;
    }

    public synchronized void forceStop() {
        // stop processing
        continueProcessing = false;
        // force stop in effect
        forceStop = true;
        // notify all
        notifyAll();
        // shutdown executor (hard)
        executorService.shutdownNow();
    }

    public synchronized void stop() {
        // stop processing
        continueProcessing = false;
        // notify all
        notifyAll();
    }

    /**
     * Status of the manager.
     * 
     * @author bradnussbaum
     * @version 0.1.0
     * 
     * @since 0.1.0
     * 
     */
    public enum Status {

        WAITING, RUNNING, COMPLETED, FAILED;

    }

}
