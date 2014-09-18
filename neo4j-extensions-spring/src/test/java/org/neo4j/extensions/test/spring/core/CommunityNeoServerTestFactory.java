package org.neo4j.extensions.test.spring.core;

/**
 * @author bradnussbaum
 * @since 2014.09.17
 */
public class CommunityNeoServerTestFactory {

    private static Object lock = new Object();

    private static CommunityNeoServerTestInstance instance;

    public static CommunityNeoServerTestInstance getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new CommunityNeoServerTestInstance();
                }
            }
        }
        return instance;
    }

    public CommunityNeoServerTestInstance instance() {
        return getInstance();
    }
}
