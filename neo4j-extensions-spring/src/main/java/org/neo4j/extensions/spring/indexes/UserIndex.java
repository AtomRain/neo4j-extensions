package org.neo4j.extensions.spring.indexes;

import java.util.Map;

/**
 * UserIndex indexed properties.
 * 
 * 
 * @author bradnussbaum
 * @version 0.1.0
 * 
 * @since 0.1.0
 * 
 */
public enum UserIndex {

    lastModifiedTime(IndexType.user_exact),

    email(IndexType.user_fulltext),

    name(IndexType.user_fulltext),

    username(IndexType.user_fulltext);

    private IndexType type;

    private UserIndex(IndexType type) {
        this.type = type;
    }

    public Map<String, String> getIndexType() {
        return type.getIndexType();
    }

    public String getIndexName() {
        return type.name();
    }

}
