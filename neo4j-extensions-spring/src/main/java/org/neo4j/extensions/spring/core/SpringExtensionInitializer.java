package org.neo4j.extensions.spring.core;

import org.neo4j.extensions.spring.repository.UserRepository;
import org.springframework.data.neo4j.server.SpringPluginInitializer;

/**
 * Hook for Spring initialization
 *
 * @author bradnussbaum
 * @version 0.1.0
 * @since 0.1.0
 */
public class SpringExtensionInitializer extends SpringPluginInitializer {

    public SpringExtensionInitializer() {
        super(new String[]{
                "META-INF/spring/springContext.xml"
        }, expose("userRepository", UserRepository.class));
    }

}
