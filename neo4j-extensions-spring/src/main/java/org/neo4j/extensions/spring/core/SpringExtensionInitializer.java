package org.neo4j.extensions.spring.core;

import org.apache.commons.configuration.Configuration;
import org.neo4j.extensions.spring.domain.User;
import org.neo4j.extensions.spring.repository.UserRepository;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.server.plugins.Injectable;
import org.springframework.data.neo4j.server.SpringPluginInitializer;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Hook for Spring initialization
 *
 * @author bradnussbaum
 * @version 0.1.0
 * @since 0.1.0
 */
public class SpringExtensionInitializer extends SpringPluginInitializer {

    private static final Logger LOGGER = Logger.getLogger(SpringExtensionInitializer.class.getName());

    public SpringExtensionInitializer() {
        super(new String[]{
                "META-INF/spring/springContext.xml"
        }, expose("neo4jTemplate", Neo4jTemplate.class), expose("userRepository", UserRepository.class));
        LOGGER.info("Spring context configured.");
    }

    @Override
    public Collection<Injectable<?>> start(GraphDatabaseService graphDatabaseService, Configuration config) {
        LOGGER.info("Starting spring context...");
        Collection<Injectable<?>> injectableCollection = super.start(graphDatabaseService, config);
        LOGGER.info("Spring context started.");
        try {
            LOGGER.info("Loading neo4jTemplate...");
            Neo4jTemplate neo4jTemplate = ctx.getBean(Neo4jTemplate.class);
            Assert.notNull(neo4jTemplate, "Spring Data Neo4j failed to initialize!");
            LOGGER.info("Successfully loaded neo4jTemplate");
            LOGGER.info("Loading all label names...");
            Collection<String> allLabelNames = neo4jTemplate.getGraphDatabase().getAllLabelNames();
            LOGGER.info("Successfully loaded all label names");
            LOGGER.info("Getting all database names:");
            Iterator<String> lableNamesIterator = allLabelNames.iterator();
            while (lableNamesIterator.hasNext()) {
                String label = lableNamesIterator.next();
                LOGGER.info(String.format("Found label %s", label));
            }
            Long count = neo4jTemplate.count(User.class);
            LOGGER.info(String.format("User count: %d", count));
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }
        return injectableCollection;
    }

}
