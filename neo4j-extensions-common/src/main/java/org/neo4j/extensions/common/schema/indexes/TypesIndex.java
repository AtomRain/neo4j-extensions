package org.neo4j.extensions.common.schema.indexes;

import java.util.Map;

/**
 * Class types indexed properties.
 *
 * @author bradnussbaum
 * @since 2014.05.25
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
