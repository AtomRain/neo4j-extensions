package org.neo4j.extensions.indexes;

import java.util.Map;

import org.neo4j.index.impl.lucene.LuceneIndexImplementation;

/**
 * Index types.
 * 
 * 
 * @author bradnussbaum
 * @version 0.1.0
 * 
 * @since 0.1.0
 * 
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
