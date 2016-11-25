package org.neo4j.extensions.test.spring.core;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author bradnussbaum
 * @since 2014.09.17
 */
@Component
@Configurable
public class CommunityNeoServerTestProperties
{

    @Value( "${dbms.connector.http.port}" )
    private Integer dbmsConnectorHttpPort;

    @Value( "${dbms.connector.bolt.port}" )
    private Integer dbmsConnectorBoltPort;

    @Value( "${dbms.shell.port}" )
    private Integer dbmsShellPort;

    @Value( "${dbms.directories.data}" )
    private String dbmsDirectoriesData;

    public Integer getDbmsConnectorHttpPort()
    {
        return dbmsConnectorHttpPort;
    }

    public void setDbmsConnectorHttpPort( Integer dbmsConnectorHttpPort )
    {
        this.dbmsConnectorHttpPort = dbmsConnectorHttpPort;
    }

    public Integer getDbmsConnectorBoltPort()
    {
        return dbmsConnectorBoltPort;
    }

    public void setDbmsConnectorBoltPort( Integer dbmsConnectorBoltPort )
    {
        this.dbmsConnectorBoltPort = dbmsConnectorBoltPort;
    }

    public Integer getDbmsShellPort()
    {
        return dbmsShellPort;
    }

    public void setDbmsShellPort( Integer dbmsShellPort )
    {
        this.dbmsShellPort = dbmsShellPort;
    }

    public String getDbmsDirectoriesData()
    {
        return dbmsDirectoriesData;
    }

    public void setDbmsDirectoriesData( String dbmsDirectoriesData )
    {
        this.dbmsDirectoriesData = dbmsDirectoriesData;
    }
}
