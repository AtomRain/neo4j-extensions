package org.neo4j.extensions.spring.indexing;


/**
 * Indexing services.
 * 
 * 
 * @author bradnussbaum
 * @version 0.1.0
 * 
 * @since 0.1.0
 * 
 */
public interface IndexingService {

    /**
     * @return Whether manager is running.
     */
    Boolean isRunning();

    /**
     * @return Whether manager is waiting.
     */
    Boolean isWaiting();

    /**
     * @return Whether manager is completed.
     */
    Boolean isCompleted();

    /**
     * Starts indexing if not running.
     * 
     * Exists immediately if the process has already been started.
     * 
     * @param startIndex The index (line) as a starting point.
     * @param indexName The index name.
     * @param indexKey The index key.
     * @param qualifierKey The qualifier key.
     * @param qualifierValue The qualifier value.
     * @param forNodes Whether for nodes or relationships.
     */
    void start(Integer startIndex, String indexName, String indexKey, String qualifierKey, String qualifierValue, Boolean forNodes);

    /**
     * Stops indexing if it's running.
     * 
     * @param force Shutdown all executors by force or let them finish.
     */
    void stop(Boolean force);

    /**
     * Updates manager configuration.
     * 
     * @param maxConcurrent Max concurrent processors.
     * @param maxThreads Max active threads.
     * @param batchSize Batch processing size.
     */
    void configure(Integer maxConcurrent, Integer maxThreads, Integer batchSize);

}
