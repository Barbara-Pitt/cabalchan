package org.cabalchan.cabalchan.utilities;

import java.time.Duration;
import java.time.Period;
import java.time.temporal.TemporalAmount;

public class TimeUtil {
    public static TemporalAmount parse(String feString) {
        if (Character.isUpperCase(feString.charAt(feString.length() - 1))) {
            return Period.parse("P" + feString);
        } else {
            return Duration.parse("PT" + feString);
        }
    }
}
