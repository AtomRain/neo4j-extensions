package org.neo4j.extensions.spring.common;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

import au.com.bytecode.opencsv.bean.MappingStrategy;

/**
 * Exposes needed methods.
 * 
 * 
 * @author bradnussbaum
 * @version 0.1.0
 * 
 * @since 0.1.0
 * 
 */
public class CsvToBean<T> extends au.com.bytecode.opencsv.bean.CsvToBean<T> {

    @Override
    public T processLine(MappingStrategy<T> mapper, String[] line) throws IllegalAccessException, InvocationTargetException,
            InstantiationException, IntrospectionException {
        return super.processLine(mapper, line);
    }

}
