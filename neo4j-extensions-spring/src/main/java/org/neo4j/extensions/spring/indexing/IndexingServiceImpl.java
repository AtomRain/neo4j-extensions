package org.neo4j.extensions.spring.indexing;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.neo4j.graphdb.GraphDatabaseService;


/**
 * Indexing services implementation.
 * 
 * 
 * @author bradnussbaum
 * @version 0.1.0
 * 
 * @since 0.1.0
 * 
 */
public class IndexingServiceImpl implements IndexingService {

    /**
     * Double-checked locking.
     * 
     * @return Thread-safe instance.
     */
    public static IndexingServiceImpl getInstance(GraphDatabaseService graphDb) {
        if (instance == null) {
            synchronized (IndexingServiceImpl.class) {
                if (instance == null)
                    instance = new IndexingServiceImpl(graphDb);
            }
        }
        return instance;
    }

    private static final Logger LOGGER = Logger.getLogger(IndexingServiceImpl.class.getName());

    private static IndexingServiceImpl instance;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private GraphDatabaseService graphDb;

    private IndexingManager indexingManager = null;

    /**
     * Requires a graph database.
     * 
     * @param graphDb The graph database.
     */
    public IndexingServiceImpl(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
    }

    /**
     * @see IndexingService#isRunning()
     */
    @Override
    public synchronized Boolean isRunning() {
        return (indexingManager != null && indexingManager.isRunning());
    }

    /**
     * @see IndexingService#isWaiting()
     */
    @Override
    public synchronized Boolean isWaiting() {
        return (indexingManager != null && indexingManager.isWaiting());
    }

    /**
     * @see IndexingService#isCompleted()
     */
    @Override
    public synchronized Boolean isCompleted() {
        return (indexingManager != null && indexingManager.isCompleted());
    }

    /**
     * @see IndexingService#start(Integer, String, String, String, String, Boolean)
     */
    @Override
    public synchronized void start(Integer startIndex, String indexName, String indexKey, String qualifierKey,
            String qualifierValue, Boolean forNodes) {
        if (isRunning()) {
            LOGGER.log(Level.INFO, "INDEXING: SERVICE: status=RUNNING");
            // already running
            return;
        }
        // create and start the manager
        try {
            LOGGER.log(Level.INFO, "INDEXING: SERVICE: status=STARTED");
            indexingManager = new IndexingManager(graphDb, startIndex, indexName, indexKey, qualifierKey, qualifierValue, forNodes);
            // start running in a separate thread
            executorService.execute(indexingManager);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "INDEXING: SERVICE: status=FAILURE", e);
            indexingManager = null;
        }
    }

    /**
     * @see IndexingService#stop(Boolean)
     */
    @Override
    public synchronized void stop(Boolean force) {
        // stop if running
        if (isRunning()) {
            if (force) {
                indexingManager.forceStop();
            } else {
                indexingManager.stop();
            }
        }
    }

    @Override
    public void configure(Integer maxConcurrent, Integer maxThreads, Integer batchSize) {
        // update parameters
        synchronized (IndexingManager.class) {
            IndexingManager.MAX_CONCURRENT = maxConcurrent;
            IndexingManager.MAX_THREADS = maxThreads;
            IndexingManager.BATCH_SIZE = batchSize;
        }
    }

}
