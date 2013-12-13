package org.neo4j.extensions.rest;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.neo4j.graphdb.GraphDatabaseService;

import org.neo4j.extensions.indexing.IndexingManager;
import org.neo4j.extensions.indexing.IndexingService;
import org.neo4j.extensions.indexing.IndexingServiceImpl;


/**
 * The re-indexing controller.
 * 
 * 
 * @author bradnussbaum
 * @version 0.1.0
 * 
 * @since 0.1.0
 * 
 */
@Path("/indexing")
public class IndexingController {

    private static final Logger LOGGER = Logger.getLogger(IndexingController.class.getName());

    @Context
    private GraphDatabaseService graphDb;

    @PostConstruct
    public void init() {
        indexingService = IndexingServiceImpl.getInstance(graphDb);
    }

    private IndexingService indexingService;

    /**
     * Performs re-indexing for a given index and property (key) with constraints based on
     * qualification values.
     * 
     * @param startIndex The starting index.
     * @param indexName The index name to update.
     * @param indexKey The key for a property to update.
     * @param qualifierKey The key for qualification purposes.
     * @param qualifierValue The value to qualify against.
     * @param forNodes Whether for nodes or relationships.
     * 
     * @return Status 200 on success.
     */
    @GET
    @Path("/start")
    @Produces(MediaType.TEXT_PLAIN)
    public Response start(@QueryParam("startIndex") @DefaultValue("0") Integer startIndex, @QueryParam("indexName") String indexName,
            @QueryParam("indexKey") String indexKey, @QueryParam("qualifierKey") String qualifierKey,
            @QueryParam("qualifierValue") String qualifierValue, @QueryParam("forNodes") @DefaultValue("true") Boolean forNodes) {

        // validate arguments
        if (startIndex < 0) {
            String entity = "Query parameter 'startIndex' must not be less than zero!";
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(entity).build());
        }

        if (indexName == null || indexName.isEmpty()) {
            String entity = "Query parameter 'indexName' must not be empty!";
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(entity).build());
        }

        if (indexKey == null || indexKey.isEmpty()) {
            String entity = "Query parameter 'indexKey' must not be empty!";
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(entity).build());
        }

        if (qualifierKey == null || qualifierKey.isEmpty()) {
            String entity = "Query parameter 'qualifierKey' must not be empty!";
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(entity).build());
        }

        if (qualifierValue == null || qualifierValue.isEmpty()) {
            String entity = "Query parameter 'qualifierValue' must not be empty!";
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(entity).build());
        }

        LOGGER.info(String.format("REINDEX: GET /indexing?indexName=%s&indexKey=%s&qualifierKey=%s&qualifierValue=%s&forNodex=%s",
                indexName, indexKey, qualifierKey, qualifierValue, forNodes));

        // start indexing
        try {
            indexingService.start(startIndex, indexName, indexKey, qualifierKey, qualifierValue, forNodes);
        } catch (Exception e) {
            throw new WebApplicationException(Response.serverError().entity(e).build());
        }

        return Response.ok().entity("ok").build();
    }

    /**
     * Stop indexing.
     * 
     * @param force Whether to force stop.
     * 
     * @return Status OK on success
     */
    @GET
    @Path("/stop")
    public Response stop(@QueryParam("force") @DefaultValue("false") Boolean force) {

        LOGGER.info(String.format("FACTUAL: GET /factual/stop?force=%s", force));

        try {
            indexingService.stop(force);
        } catch (Exception e) {
            throw new WebApplicationException(Response.serverError().entity(e).build());
        }

        return Response.ok().entity("ok").build();
    }

    /**
     * Updates manager configuration.
     * 
     * @param maxConcurrent Max concurrent processors.
     * @param maxThreads Max active threads.
     * @param batchSize Batch processing size.
     * 
     * @return Status OK on success
     */
    @GET
    @Path("/configure")
    public Response configure(@QueryParam("maxConcurrent") @DefaultValue(IndexingManager.DEFAULT_MAX_CONCURRENT) Integer maxConcurrent,
            @QueryParam("maxThreads") @DefaultValue(IndexingManager.DEFAULT_MAX_THREADS) Integer maxThreads,
            @QueryParam("batchSize") @DefaultValue(IndexingManager.DEFAULT_BATCH_SIZE) Integer batchSize) {

        // validate arguments
        if (maxConcurrent < 1) {
            String entity = "Query parameter 'maxConcurrent' must not be less than one!";
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(entity).build());
        }

        if (maxThreads < 1) {
            String entity = "Query parameter 'maxThreads' must not be less than one!";
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(entity).build());
        }

        if (batchSize < 1) {
            String entity = "Query parameter 'batchSize' must not be less than one!";
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(entity).build());
        }

        LOGGER.info(String.format("FACTUAL: GET /factual/configure?maxConcurrent=%d&maxThreads=%d&batchSize=%d", maxConcurrent, maxThreads,
                batchSize));

        try {
            indexingService.configure(maxConcurrent, maxThreads, batchSize);
        } catch (Exception e) {
            throw new WebApplicationException(Response.serverError().entity(e).build());
        }

        return Response.ok().entity("ok").build();
    }

}
