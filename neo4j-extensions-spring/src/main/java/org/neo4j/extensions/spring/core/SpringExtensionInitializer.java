package org.neo4j.extensions.spring.core;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.Configuration;
import org.springframework.data.neo4j.server.SpringPluginInitializer;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.util.Assert;

import org.neo4j.extensions.spring.repository.UserRepository;
import org.neo4j.extensions.spring.service.UserService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.server.plugins.Injectable;

/**
 * Hook for Spring initialization
 *
 * @author bradnussbaum
 * @version 0.1.0
 * @since 0.1.0
 */
public class SpringExtensionInitializer extends SpringPluginInitializer
{

    private static final Logger LOGGER = Logger.getLogger( SpringExtensionInitializer.class.getName() );

    public SpringExtensionInitializer()
    {
        super( new String[]{"META-INF/spring/springContext.xml"},
                expose( "neo4jTemplate", Neo4jTemplate.class ),
                expose( "userRepository", UserRepository.class ),
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
            Neo4jTemplate neo4jTemplate = ctx.getBean( Neo4jTemplate.class );
            Assert.notNull( neo4jTemplate, "Spring Data Neo4j failed to initialize!" );
            LOGGER.info( "Successfully loaded neo4jTemplate." );
            LOGGER.info( "Loading userRepository..." );
            UserRepository userRepository = ctx.getBean( UserRepository.class );
            Assert.notNull( userRepository, "Spring Data Neo4j failed to initialize!" );
            LOGGER.info( "Successfully loaded userRepository." );
            UserService userService = ctx.getBean( UserService.class );
            Assert.notNull( userService, "Spring Data Neo4j failed to initialize!" );
            LOGGER.info( "Successfully loaded userService." );
        }
        catch ( Exception e )
        {
            LOGGER.log( Level.SEVERE, e.getMessage(), e );
        }
        return injectableCollection;
    }

}
