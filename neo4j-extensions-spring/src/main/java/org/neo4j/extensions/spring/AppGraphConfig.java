package org.neo4j.extensions.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.drivers.http.driver.HttpDriver;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

/**
 * Graph configuration.
 *
 * @author bradnussbaum
 * @since 2015.12.30
 */
@org.springframework.context.annotation.Configuration
@EnableNeo4jRepositories( basePackages = {"org.neo4j.extensions.spring.repository"} )
@EnableTransactionManagement
public class AppGraphConfig extends Neo4jConfiguration
{

    @Value( "${neo4j.graph.db}" )
    private String graphDb;

    @Value( "${spring.data.neo4j.url}" )
    private String url;

    @Value( "${neo4j.server.port}" )
    private Integer port;

    @Value( "${spring.data.neo4j.username}" )
    private String username;

    @Value( "${spring.data.neo4j.password}" )
    private String password;

    @Bean
    public Configuration getConfiguration()
    {
        if ( StringUtils.isEmpty( url ) )
        {
            url = "http://localhost:" + port;
        }
        Configuration config = new Configuration();
        config.driverConfiguration().setDriverClassName( HttpDriver.class.getName() )
                .setCredentials( username, password ).setURI( url );
        return config;
    }

    @Bean
    public SessionFactory getSessionFactory()
    {
        return new SessionFactory( getConfiguration(), "org.neo4j.extensions.spring.domain" );
    }

    @Bean
    @Scope( value = ConfigurableBeanFactory.SCOPE_PROTOTYPE )
    public Session getSession() throws Exception
    {
        return super.getSession();
    }

}
