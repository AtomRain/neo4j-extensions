package org.neo4j.extensions.spring.core;

import org.neo4j.extensions.spring.repository.UserRepository;
import org.springframework.data.neo4j.server.SpringPluginInitializer;
import org.springframework.data.neo4j.support.Neo4jTemplate;


/**
 * Hook for Spring initialization
 * 
 * @author bradnussbaum
 * @version 0.1.0
 * 
 * @since 0.1.0
 */
public class SpringExtensionInitializer extends SpringPluginInitializer {

    @SuppressWarnings("unchecked")
    public SpringExtensionInitializer() {
        super(new String[] {
            "classpath*:META-INF/spring/springContext.xml"
        }, expose("template", Neo4jTemplate.class), expose("userRepository", UserRepository.class));
    }

}
