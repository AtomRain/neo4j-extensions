package org.neo4j.extensions.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;

/**
 * Utility for converting data objects to and from CSV.
 * 
 * 
 * @author bradnussbaum
 * @version 0.1.0
 * 
 * @since 0.1.0
 * 
 */
public class CSVUtils<T> {

    public List<T> fromCSV(Class<T> type, CSVReader reader, String[] columnMapping) {
        // create the bean mapping
        ColumnPositionMappingStrategy<T> strategy = new ColumnPositionMappingStrategy<T>();
        strategy.setType(type);
        strategy.setColumnMapping(columnMapping);
        // parse the csv to the bean
        CsvToBean<T> csv = new CsvToBean<T>();
        return csv.parse(strategy, reader);
    }

    public void writeCSV(Class<T> type, CSVCapable capableBean, Writer writer) throws IOException {
        // parse the csv to the bean
        CSVWriter csvWriter = new CSVWriter(writer);
        csvWriter.writeNext(capableBean.getValues());
        csvWriter.close();
    }

    public List<T> parse(InputStream input, Class<T> type, String[] columnMapping) {
        CSVReader reader = new CSVReader(new InputStreamReader(input));
        return fromCSV(type, reader, columnMapping);
    }

}
