package org.neo4j.extensions.java.rest;

import java.util.logging.Logger;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.neo4j.extensions.common.client.StatusClient;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

/**
 * The status controller.
 *
 * @author bradnussbaum
 * @version 0.1.0
 * @since 0.1.0
 */
@Path("/status")
public class StatusController implements StatusClient
{

    private static final Logger LOGGER = Logger.getLogger( StatusController.class.getName() );

    @Context
    private GraphDatabaseService db;

    public Response status()
    {
        Transaction txn = null;
        try
        {
            txn = db.beginTx();
            txn.success();
            LOGGER.info( "STATUS: success" );
        }
        catch ( Exception e )
        {
            txn.failure();
            LOGGER.info( "STATUS: failure" );
            throw new WebApplicationException( e );
        }
        finally
        {
            if ( txn != null )
            {
                txn.close();
            }
        }
        return Response.ok().entity( "ok" ).build();
    }

}
