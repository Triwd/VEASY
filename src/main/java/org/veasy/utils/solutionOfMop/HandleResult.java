package org.veasy.utils.solutionOfMop;

import java.util.Date;

public class HandleResult {

    public static Double handleHours(Double hours) {
        return hours;
    }

    public static Date handleDate(Double date, Date signTime, Date closeTime) {
        Date resultTime = new Date();
        resultTime.setTime((long) (date / 60300.0) * (closeTime.getTime() - signTime.getTime()));
        return resultTime;
    }
}
