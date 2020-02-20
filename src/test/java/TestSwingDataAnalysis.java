package org.tonyc.swingdata.test;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.Assert;
import java.io.*;
import java.util.Optional;

import org.tonyc.swingdata.SwingData;
import org.tonyc.swingdata.Analysis;


public class TestSwingDataAnalysis {

    SwingData swing;

    @BeforeClass
    public void setup() throws IOException {
        swing = SwingData.fromCSV(
            TestSwingDataAnalysis
            .class.getResource("/latestSwing.csv")
            .getFile());
    }

    @Test
    public void testSearchContinuityAboveValue() throws IOException
    {
        Assert.assertEquals(
            Analysis.searchContinuityAboveValue(swing.wy, 0, 14, -15.0, 5),
            Optional.of(6));

        Assert.assertEquals(
            Analysis.searchContinuityAboveValue(swing.wx, 0, 14, 3, 15),
            Optional.of(0));

        Assert.assertEquals(
            Analysis.searchContinuityAboveValue(swing.ay, 0, 0, 0, 1),
            Optional.of(0));


    }

    @Test
    public void testContinutySearchAboveValueLessThanWinLength() throws IOException
    {
        Assert.assertEquals(
            Analysis.searchContinuityAboveValue(swing.ay, 0, 1276, 1, 30),
            Optional.of(760));
    }

    @Test
    public void testSearchContinuityAboveValueBeginning() throws IOException
    {
        Assert.assertEquals(
            Analysis.searchContinuityAboveValue(swing.ay, 0, 14, 0.23, 5),
            Optional.of(0));

    }

    @Test
    public void testSearchContinuityAboveValueEnd() throws IOException
    {
        Assert.assertEquals(
            Analysis.searchContinuityAboveValue(swing.wz, 0, 14, -5.2, 5),
            Optional.of(10));

    }

    @Test
    public void testSearchContinuityAboveValueEndNoResults() throws IOException
    {
        Assert.assertEquals(
            Analysis.searchContinuityAboveValue(swing.wz, 0, 14, -5.2, 6),
            Optional.empty());

    }

    @Test
    public void testSearchContinuityAboveValueNoResults() throws IOException
    {
        Assert.assertEquals(
            Analysis.searchContinuityAboveValue(swing.wy, 0, 14, 100, 5),
            Optional.empty());
    }

    @Test(expectedExceptions = IOException.class)
    public void testLoadMissingCSV() throws IOException
    {
        SwingData s = SwingData.fromCSV("/path/to/missing/file.csv");
    }


    @Test
    public void backSearchContinuityWithinRange() throws IOException
    {
        Assert.assertEquals(
            Analysis.backSearchContinuityWithinRange(swing.wx, 14, 0, 4, 4.5, 5),
            Optional.of(8));

        Assert.assertEquals(
            Analysis.backSearchContinuityWithinRange(swing.az, 14, 0, -1, 0, 3),
            Optional.of(4));

        Assert.assertEquals(
            Analysis.backSearchContinuityWithinRange(swing.wx, 14, 0, 4.8, 5.0, 1),
            Optional.of(0));
    }


    @Test
    public void backSearchContinuityWithinRangeNoResults() throws IOException
    {
        Assert.assertEquals(
            Analysis.backSearchContinuityWithinRange(swing.wy, 14, 0, 1, 2, 5),
            Optional.empty());

    }

    @Test
    public void backSearchContinuityWithinRangeBegin() throws IOException
    {
        Assert.assertEquals(
            Analysis.backSearchContinuityWithinRange(swing.wy, 14, 0, 1, 2, 5),
            Optional.empty());
    }

    @Test
    public void testSearchContinuityAboveValueTwoSignals() throws IOException
    {
        Assert.assertEquals(
            Analysis.searchContinuityAboveValueTwoSignals(
                swing.ax, swing.ay, 0, 14, -1, 0.3, 5),
            Optional.of(4));

        Assert.assertEquals(
            Analysis.searchContinuityAboveValueTwoSignals(
                swing.ay, swing.wy, 0, 14, 0.4, -14.5, 5),
            Optional.of(9));

    }

    @Test
    public void testSearchMultiContinuityWithinRange() throws IOException
    {
        int [] res = Analysis.searchMultiContinuityWithinRange(
            swing.az, 0, 14, -1, 0, 1);
        Assert.assertEquals(res[0], 4);
        Assert.assertEquals(res[1], 6);

        res = Analysis.searchMultiContinuityWithinRange(
            swing.az, 0, 14, -1, -0.9, 1);
        Assert.assertEquals(res[0], 4);
        Assert.assertEquals(res[1], 6);

        res = Analysis.searchMultiContinuityWithinRange(
            swing.wx, 0, 14, 4.6, 4.7, 2);
        Assert.assertEquals(res[0], 2);
        Assert.assertEquals(res[1], 3);

        res = Analysis.searchMultiContinuityWithinRange(
            swing.wy, 0, 14, -13.5, -13, 1);
        Assert.assertEquals(res[0], 14);
        Assert.assertEquals(res[1], 14);

    }

    @Test
    public void testSearchMultiContinuityWithinRangeNoResults()
        throws IOException {

        int [] res = Analysis.searchMultiContinuityWithinRange(
            swing.ay, 0, 14, 0, 0.1, 1);
        Assert.assertEquals(res[0], -1);
        Assert.assertEquals(res[1], -1);

    }


}
