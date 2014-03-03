package org.neo4j.extensions.java.indexes;

import org.neo4j.index.impl.lucene.LuceneIndexImplementation;

import java.util.Map;

/**
 * Index types.
 *
 * @author bradnussbaum
 * @version 0.1.0
 * @since 0.1.0
 */
public enum IndexType {

    __types__(LuceneIndexImplementation.EXACT_CONFIG),

    uids(LuceneIndexImplementation.EXACT_CONFIG),

    user_exact(LuceneIndexImplementation.EXACT_CONFIG),

    user_fulltext(LuceneIndexImplementation.FULLTEXT_CONFIG);

    private Map<String, String> indexType;

    private IndexType(Map<String, String> indexType) {
        this.indexType = indexType;
    }

    public Map<String, String> getIndexType() {
        return indexType;
    }

}
