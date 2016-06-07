package org.neo4j.extensions.spring.core;

import org.apache.commons.configuration.Configuration;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.neo4j.extensions.spring.service.UserService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.server.plugins.Injectable;

/**
 * Hook for Spring initialization
 *
 * @author bradnussbaum
 * @since 2015.11.25
 */
public class SpringExtensionInitializer extends SpringPluginInitializer
{

    private static final Logger LOGGER = Logger.getLogger( SpringExtensionInitializer.class.getName() );

    public SpringExtensionInitializer()
    {
        super( new String[]{"META-INF/spring/springContext.xml"}, expose( "neo4jOperations", Neo4jOperations.class ),
                expose( "userServiceImpl", UserService.class ) );
        LOGGER.info( "Spring context configured." );
    }

    @Override
    public Collection<Injectable<?>> start( GraphDatabaseService graphDatabaseService, Configuration config )
    {
        LOGGER.info( "Starting spring context..." );
        Collection<Injectable<?>> injectableCollection = super.start( graphDatabaseService, config );
        LOGGER.info( "Spring context started." );
        try
        {
            LOGGER.info( "Loading neo4jTemplate..." );
            Neo4jOperations neo4jOperations = ctx.getBean( Neo4jOperations.class );
            Assert.notNull( neo4jOperations, "Spring Data Neo4j failed to initialize!" );
            LOGGER.info( "Successfully loaded neo4jTemplate." );
            LOGGER.info( "Loading userRepository..." );
            UserService userService = ctx.getBean( UserService.class );
            Assert.notNull( userService, "Spring Data Neo4j failed to initialize!" );
            LOGGER.info( "Successfully loaded userRepository." );
        }
        catch ( Exception e )
        {
            LOGGER.log( Level.SEVERE, e.getMessage(), e );
        }
        return injectableCollection;
    }

}
