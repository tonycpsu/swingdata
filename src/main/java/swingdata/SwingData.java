package org.tonyc.swingdata;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;

public class SwingData {

    public ArrayList<Integer> timestamp;
    public ArrayList<Double> ax;
    public ArrayList<Double> ay;
    public ArrayList<Double> az;
    public ArrayList<Double> wx;
    public ArrayList<Double> wy;
    public ArrayList<Double> wz;

    public SwingData() {
        this.timestamp = new ArrayList<Integer>();
        this.ax = new ArrayList<Double>();
        this.ay = new ArrayList<Double>();
        this.az = new ArrayList<Double>();
        this.wx = new ArrayList<Double>();
        this.wy = new ArrayList<Double>();
        this.wz = new ArrayList<Double>();
    }

    public static SwingData fromCSV (String filename) throws IOException {

        SwingData swing = new SwingData();

        Reader in = new FileReader(filename);
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader(
            "timestamp", "ax", "ay", "az", "wx", "wy", "wz").parse(in);

        for (CSVRecord csvRecord : records) {
            swing.timestamp.add(Integer.parseInt(csvRecord.get("timestamp")));
            swing.ax.add(Double.parseDouble(csvRecord.get("ax")));
            swing.ay.add(Double.parseDouble(csvRecord.get("ay")));
            swing.az.add(Double.parseDouble(csvRecord.get("az")));
            swing.wx.add(Double.parseDouble(csvRecord.get("wx")));
            swing.wy.add(Double.parseDouble(csvRecord.get("wy")));
            swing.wz.add(Double.parseDouble(csvRecord.get("wz")));
        }
        return swing;
    }

}
