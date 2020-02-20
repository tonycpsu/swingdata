package org.tonyc.swingdata;

import java.io.*;
import java.util.*;

public class Analysis {

    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.out.println("missing required CSV file argument");
            System.exit(1);
        }

        SwingData swing = SwingData.fromCSV(args[0]);
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
                )
            )
        );

    }

    public static Optional<Integer> searchContinuityAboveValue(
        ArrayList<Double> data,
        int indexBegin,
        int indexEnd,
        double threshold,
        int winLength)
    {

      window:
        // iterate through the input data, stop when window hits the end
        for (int i = indexBegin; i <= indexEnd - winLength +1; i++) {
            // for each starting index, iterate through the window size
            for (int j = 0; j < winLength; j++) {
                if (data.get(i+j) <= threshold)
                {
                    // advance window, skipping non-matching data in this window
                    i += j;
                    continue window;
                }
            }
            return Optional.of(i);
        }
        return Optional.empty();
    }


    public static Optional<Integer> backSearchContinuityWithinRange(
        ArrayList<Double> data,
        int indexBegin,
        int indexEnd,
        double thresholdLo,
        double thresholdHi,
        int winLength)
    {

      window:
        // iterate through the input data, stop when window hits the beginning
        for (int i = indexBegin; i >= indexEnd + winLength; i--) {

            // for each starting index, iterate through the window size
            for (int j = 0; j < winLength; j++) {
                // if value at index within window is lower or higher than
                // specified thresholds, skip to next window
                if (data.get(i-j-1) <= thresholdLo
                    || data.get(i-j) >= thresholdHi
                    ) {
                    // advance window, skipping non-matching data in this window
                    i -= j;
                    continue window;
                }
            }
            // this window is good, return the first index
            return Optional.of(i - winLength);
        }
        return Optional.empty();
    }


    public static Optional<Integer> searchContinuityAboveValueTwoSignals(
        ArrayList<Double> data1,
        ArrayList<Double> data2,
        int indexBegin,
        int indexEnd,
        double threshold1,
        double threshold2,
        int winLength)
    {
      window:
        // iterate through the input data, stop when window hits the end
        for (int i = indexBegin; i <= indexEnd - winLength +1; i++) {

            // for each starting index, iterate through the window size
            for (int j = 0; j < winLength; j++) {
                if (data1.get(i+j) <= threshold1
                    || data2.get(i+j) <= threshold2) {
                    // advance window, skipping non-matching data in this window
                    i += j;
                    continue window;
                }
            }
            return Optional.of(i);
        }
        return Optional.empty();

    }

    public static int[] searchMultiContinuityWithinRange(
        ArrayList<Double> data,
        int indexBegin,
        int indexEnd,
        double thresholdLo,
        double thresholdHi,
        int winLength)
    {
        int i=0, j=0;
        int first = -1;
        int[] ret = {-1, -1};

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
                ret[0] = i;
                ret[1] = i+j-1;
                break window;
            }
        }
        return ret;


    }
}
