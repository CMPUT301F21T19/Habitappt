package com.CMPUT301F21T19.habitappt;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class SharedHelper_test {

    @Before
    public void setup(){

    }

    //test that this utility function is working properly.
    @Test
    public void testGetStringDateFromLong() throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date time = dateFormat.parse("02/09/2000");

        long time_millis = time.getTime();

        System.out.println(SharedHelper.getStringDateFromLong(time_millis));

        assertTrue(SharedHelper.getStringDateFromLong(time_millis).equals("02/09/2000"));

    }
}
