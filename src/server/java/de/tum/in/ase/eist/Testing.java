package de.tum.in.ase.eist;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Testing {
    public static void main(String[] args) throws Exception {
        // String sampleTime = "Sun 11:30 AM - 7:00 PM";
        String sampleTime = "Mon 11:30 AM - 2:30 PM 5:00 PM - 10:30 PM";
        Pattern pattern = Pattern
                .compile("[A-Za-z]{3}\\ ([0-9]{1,2}:[0-9]{2})\\ ((A|P)M) - ([0-9]{1,2}:[0-9]{2})\\ ((A|P)M)");
        Matcher matcher = pattern.matcher(sampleTime);
        while (matcher.find()) {
            String timeRange = matcher.group(1) + " " + matcher.group(2) + " - " + matcher.group(4) + " "
                    + matcher.group(5);
            String timeRange24 = convertTimeTo24(timeRange);
            System.out.println(timeRange24);
        }
    }

    private static String convertTimeTo24(String timeRange) throws ParseException {
        String[] timeRangeArray = timeRange.split(" - ");
        String[] startTimeArray = timeRangeArray[0].split(" ");
        String[] endTimeArray = timeRangeArray[1].split(" ");
        String startTime = startTimeArray[0] + " " + startTimeArray[1];
        String endTime = endTimeArray[0] + " " + endTimeArray[1];
        DateFormat df = new SimpleDateFormat("hh:mm a");
        DateFormat df24 = new SimpleDateFormat("HH:mm");
        Date startTime24 = df.parse(startTime);
        Date endTime24 = df.parse(endTime);
        String startTime24String = df24.format(startTime24);
        String endTime24String = df24.format(endTime24);
        return startTime24String + " - " + endTime24String;
    }
}