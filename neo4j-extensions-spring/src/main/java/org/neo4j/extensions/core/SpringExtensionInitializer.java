package org.neo4j.extensions.core;

import org.neo4j.extensions.repository.UserRepository;
import org.neo4j.helpers.Pair;
import org.springframework.data.neo4j.server.SpringPluginInitializer;


/**
 * Hook for Spring initialization
 * 
 * @author bradnussbaum
 * @version 0.1.0
 * 
 * @since 0.1.0
 */
public class SpringExtensionInitializer extends SpringPluginInitializer {

    public SpringExtensionInitializer() {
        super(new String[] {
            "spring/springContext.xml"
        }, expose("userRepository", UserRepository.class));
    }

}
