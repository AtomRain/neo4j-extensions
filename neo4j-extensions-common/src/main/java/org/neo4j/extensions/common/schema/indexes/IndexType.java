package org.neo4j.extensions.common.schema.indexes;

import org.neo4j.index.impl.lucene.LuceneIndexImplementation;

import java.util.Map;

/**
 * Index types.
 *
 * @author bradnussbaum
 * @since 2014.05.25
 */
public enum IndexType {

    __types__(LuceneIndexImplementation.EXACT_CONFIG),

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
