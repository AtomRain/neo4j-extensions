package org.neo4j.extensions.java.indexes;

import java.util.Map;

/**
 * Class types indexed properties.
 * 
 * 
 * @author bradnussbaum
 * @version 0.1.0
 * 
 * @since 0.1.0
 * 
 */
public enum TypesIndex {

    className(IndexType.__types__);

    private IndexType type;

    private TypesIndex(IndexType type) {
        this.type = type;
    }

    public Map<String, String> getIndexType() {
        return type.getIndexType();
    }

    public String getIndexName() {
        return type.name();
    }

}
