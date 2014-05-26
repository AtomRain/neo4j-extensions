package org.neo4j.extensions.common.schema.indexes;

import java.util.Map;

/**
 * UserIndex indexed properties.
 *
 * @author bradnussbaum
 * @since 2014.05.25
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
