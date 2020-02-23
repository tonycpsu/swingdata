package org.tonyc.swingdata;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;

public class Analysis {

    /**
     * Helper function for finding continuous samples in one or more metrics
     * matching one or more predicates.
     *
     * @param metrics list containing one or more lists of metrics to be tested
     *
     * @param conditions list containing one or more predicate functions used to
     * determine criteria for what defines a continuity.  The Nth predicate is
     * used to test elements in the Nth list in {@code metrics}, so the lengths
     * of these two lists must be equal.
     *
     * @param indexBegin beginning index of range to search
     *
     * @param indexEnd end index of range to search
     *
     * @param winLength number of samples to pass the given criteria in order to
     * be considered a continuity
     *
     * @param reverse if true, search from end of range to beginning
     *
     * @return Optional integer of beginning of continuity, or empty
     */
    public static Optional<Integer> findContinuities(
        List<List<Double>> metrics,
        List<Predicate<Double>> conditions,
        int indexBegin,
        int indexEnd,
        int winLength,
        boolean reverse)
    {
        int step = reverse ? -1 : 1;

        assert(metrics.size() == conditions.size());

      window:
        for (int i = indexBegin;
             reverse ? i >= indexEnd + winLength : i <= indexEnd - winLength + 1;
             i += step) {

            for (int j = 0; j < winLength; j++) {

                for (int m = 0; m < metrics.size(); m++) {
                    if (! conditions.get(m)
                        .test(metrics.get(m).get(reverse ? i-j-1: i+j)))
                    {
                        // advance window to avoid unnecessary tests
                        i += ( reverse ? -j : j );
                        continue window;
                    }
                }
            }
            return Optional.of(reverse ? i - winLength : i);
        }
        return Optional.empty();
    }


    public static Optional<Integer> findContinuities(
        List<List<Double>> metrics,
        List<Predicate<Double>> conditions,
        int indexBegin,
        int indexEnd,
        int winLength)
    {
        return findContinuities(
            metrics, conditions,
            indexBegin, indexEnd, winLength,
            false);
    }

    // API functions

    public static Optional<Integer> searchContinuityAboveValue(
        List<Double> data,
        int indexBegin,
        int indexEnd,
        double threshold,
        int winLength)
    {
        return findContinuities(
            List.of(data),
            List.of( v -> v > threshold),
            indexBegin,
            indexEnd,
            winLength
        );
    }


    public static Optional<Integer> backSearchContinuityWithinRange(
        List<Double> data,
        int indexBegin,
        int indexEnd,
        double thresholdLo,
        double thresholdHi,
        int winLength)
    {
        return findContinuities(
            List.of(data),
            List.of( v -> v > thresholdLo && v < thresholdHi),
            indexBegin, indexEnd, winLength,
            true);
    }

   public static Optional<Integer> searchContinuityAboveValueTwoSignals(
        List<Double> data1,
        List<Double> data2,
        int indexBegin,
        int indexEnd,
        double threshold1,
        double threshold2,
        int winLength)

    {
        return findContinuities(
            List.of(data1, data2),
            List.of( v -> v > threshold1, v -> v > threshold2),
            indexBegin,
            indexEnd,
            winLength
        );

    }

    public static List<List<Integer>> searchMultiContinuityWithinRange(
        List<Double> data,
        int indexBegin,
        int indexEnd,
        double thresholdLo,
        double thresholdHi,
        int winLength)
    {
        int i=0, j=0;
        int first = -1;
        List<List<Integer>> ret = new ArrayList<List<Integer>>();

      window:
        // iterate through the input data, stop when window hits the end
        for (i = indexBegin; i <= indexEnd - winLength +1; i++) {

            // for each starting index, iterate through the window size,
            // stopping when sample exceeds one of the thresholds
            for (j = 0;
                 i+j <= indexEnd
                     && data.get(i+j) > thresholdLo
                     && data.get(i+j) < thresholdHi
                     ; j++) {
            }
            if (j >= winLength)
            {
                List<Integer> l = Arrays.asList(i, i+j-1);
                ret.add(l);
            }
        }
        return ret;
    }

    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.out.println("missing required CSV file argument");
            System.exit(1);
        }

        var swing = SwingData.fromCSV(args[0]);

        System.out.println(
            searchContinuityAboveValue(swing.wy, 2, 12, -15.0, 5)
        );

        System.out.println(
            backSearchContinuityWithinRange(swing.wx, 14, 0, 4, 4.5, 5)
        );

        System.out.println(
            searchContinuityAboveValueTwoSignals(
                swing.ax, swing.ay, 0, 14, -1, 0.3, 5
            )
        );

        System.out.println(
            Arrays.toString(
                searchMultiContinuityWithinRange(
                    swing.az, 0, 14, -1, 0, 1
                ).toArray()
            )
        );
    }

}
