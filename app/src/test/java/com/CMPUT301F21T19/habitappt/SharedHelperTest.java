package com.CMPUT301F21T19.habitappt;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertTrue;

import com.CMPUT301F21T19.habitappt.Utils.SharedHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SharedHelperTest {

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
