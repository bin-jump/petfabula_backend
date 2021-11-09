package com.petfabula.domain.aggregate.pet.service;

import java.time.Instant;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class RecordHelper {

    public static Instant removeTimeInfo(Instant instant) {
        long timestamp = instant.toEpochMilli();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.toInstant();
    }

    public static Instant removeSecondInfo(Instant instant) {
        long timestamp = instant.toEpochMilli();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.toInstant();
    }
}
