package org.neo4j.extensions.indexes;

import java.util.Map;

/**
 * UID indexed properties.
 * 
 * 
 * @author bradnussbaum
 * @version 0.1.0
 * 
 * @since 0.1.0
 * 
 */
public enum UidsIndex {

    uid(IndexType.uids);

    private IndexType type;

    private UidsIndex(IndexType type) {
        this.type = type;
    }

    public Map<String, String> getIndexType() {
        return type.getIndexType();
    }

    public String getIndexName() {
        return type.name();
    }

}
