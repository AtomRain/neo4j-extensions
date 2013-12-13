package org.neo4j.extensions.spring.indexing;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;

/**
 * Indexing services tests.
 * 
 * 
 * @author bradnussbaum
 * @version 0.1.0
 * 
 * @since 0.1.0
 * 
 */
public class IndexingServiceTest {

    private GraphDatabaseService graphDb;

    private IndexingService indexingService;

    /**
     * Create temporary database for each unit test.
     */
    @Before
    public void prepareTestDatabase() {
        Map<String, String> config = new HashMap<String, String>();
        config.put("neostore.nodestore.db.mapped_memory", "128M");
        config.put("string_block_size", "120");
        config.put("array_block_size", "120");
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabaseBuilder().setConfig(config).newGraphDatabase();
        indexingService = IndexingServiceImpl.getInstance(graphDb);
    }

    /**
     * Shutdown the database.
     */
    @After
    public void destroyTestDatabase() {
        graphDb.shutdown();
    }

    @Test(timeout = 100000)
    public void testStartIngest() throws Exception {
        // establish manager configuration for test
        indexingService.configure(3, 4, 1000);

        Assert.assertFalse("Indexing service did not complete!", indexingService.isRunning());
    }

}
