package org.neo4j.extensions.spring.core;


import org.apache.commons.configuration.Configuration;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collection;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.helpers.Pair;
import org.neo4j.server.plugins.Injectable;
import org.neo4j.server.plugins.PluginLifecycle;

/**
 * Initializer to run Spring Data Neo4j based Server Plugins in a Neo4j REST-server. It takes the list of
 * config locations and a number of spring beans from those contexts that should be exposed
 * as injectable dependencies with a Jersey @Context.<br/>
 * For example:</br>
 * <pre>
 * class MyInitializer extends SpringPluginInitializer {
 *     public MyInitializer() {
 *         super(new String[]{"myContext.xml"},"graphRepositoryFactory","myRepository");
 *     }
 * }
 * </pre>
 */
public abstract class SpringPluginInitializer implements PluginLifecycle
{

    private String[] contextLocations;
    private Pair<String,Class>[] exposedBeans;
    protected ProvidedClassPathXmlApplicationContext ctx;

    public SpringPluginInitializer( String[] contextLocations, Pair<String,Class>... exposedBeans )
    {
        this.contextLocations = contextLocations;
        this.exposedBeans = exposedBeans;
    }

    protected static Pair<String,Class> expose( String name, Class<?> type )
    {
        return Pair.of( name, (Class) type );
    }

    /**
     * Binds the provided graph database to the spring contexts so that spring beans that consume a
     * graph database can be populated.<br/>
     *
     * @param graphDatabaseService of the Neo4j server
     * @param config of the Neo4j Server
     * @return Exposes the requested Spring beans as @{see Injectable}s
     */
    @Override
    public Collection<Injectable<?>> start( GraphDatabaseService graphDatabaseService, Configuration config )
    {
        ctx = new ProvidedClassPathXmlApplicationContext( graphDatabaseService, contextLocations );
        Collection<Injectable<?>> result = new ArrayList<>( exposedBeans.length );
        for ( final Pair<String,Class> exposedBean : exposedBeans )
        {
            @SuppressWarnings( "unchecked" ) final SpringBeanInjectable injectable =
                    new SpringBeanInjectable( ctx, exposedBean.first(), exposedBean.other() );
            result.add( injectable );
        }
        return result;
    }

    /**
     * closes the spring context
     */
    public void stop()
    {
        if ( ctx != null )
        {
            ctx.close();
        }
    }

    /**
     * provides access to the Spring bean, proxying the @{see Injectable}
     *
     * @param <T> optional type of the bean
     */
    private static class SpringBeanInjectable<T> implements Injectable<T>
    {
        private final String exposedBean;
        protected ApplicationContext ctx;
        private final Class<T> clazz;

        public SpringBeanInjectable( final ApplicationContext ctx, String exposedBean, Class<T> clazz )
        {
            this.exposedBean = exposedBean;
            this.ctx = ctx;
            this.clazz = clazz;
        }

        @SuppressWarnings( "unchecked" )
        public T getValue()
        {
            return (T) ctx.getBean( exposedBean );

        }

        public Class<T> getType()
        {
            return clazz;
        }
    }
}
