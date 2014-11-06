package org.neo4j.extensions.java.plugins;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpander;
import org.neo4j.graphdb.PathExpanderBuilder;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.Transaction;
import org.neo4j.server.plugins.Description;
import org.neo4j.server.plugins.Parameter;
import org.neo4j.server.plugins.PluginTarget;
import org.neo4j.server.plugins.ServerPlugin;
import org.neo4j.server.plugins.Source;

/**
 * @author bradnussbaum
 * @since 2014-05-25
 */
public class ShortestPath extends ServerPlugin
{

    @Description("Find the shortest path between two nodes.")
    @PluginTarget(Node.class)
    public Iterable<Path> shortestPath(
            @Source Node source,
            @Description("The node to find the shortest path to.")
            @Parameter(name = "target") Node target,
            @Description("The relationship types to follow when searching for the shortest path(s). " +
                    "Order is insignificant, if omitted all types are followed.")
            @Parameter(name = "types", optional = true) String[] types,
            @Description("The maximum path length to search for, default value (if omitted) is 4.")
            @Parameter(name = "depth", optional = true) Integer depth )
    {
        PathExpander<?> expander;
        List<Path> paths = new ArrayList<>();
        if ( types == null )
        {
            expander = PathExpanders.allTypesAndDirections();
        }
        else
        {
            PathExpanderBuilder expanderBuilder = PathExpanderBuilder.empty();
            for ( int i = 0; i < types.length; i++ )
            {
                expanderBuilder = expanderBuilder.add( DynamicRelationshipType.withName( types[i] ) );
            }
            expander = expanderBuilder.build();
        }
        try ( Transaction tx = source.getGraphDatabase().beginTx() )
        {
            PathFinder<Path> shortestPath = GraphAlgoFactory.shortestPath( expander,
                    depth == null ? 4 : depth.intValue() );
            for ( Path path : shortestPath.findAllPaths( source, target ) )
            {
                paths.add( path );
            }
            tx.success();
        }
        return paths;
    }
}
