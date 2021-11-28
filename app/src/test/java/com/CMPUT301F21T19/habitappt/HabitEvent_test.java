package com.CMPUT301F21T19.habitappt;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertTrue;

import com.CMPUT301F21T19.habitappt.Entities.Habit;
import com.CMPUT301F21T19.habitappt.Entities.HabitEvent;


public class HabitEvent_test {

    HabitEvent mockHabitEvent;

    private String comment = "test";

    private long eventDate = 123123;

    String id = "123123";

    private Habit parentHabit = new Habit();

    private Bitmap img = null;

    @Before
    public void setup(){
        mockHabitEvent = new HabitEvent(comment,eventDate,parentHabit,id,img);
    }

    //test all getters. BitMap is not mocked so it cannot be tested. just see if it returns null
    @Test
    public void testGetters(){
        assertTrue(mockHabitEvent.getComment().equals(comment));
        assertTrue(mockHabitEvent.getId().equals(id));
        assertTrue(mockHabitEvent.getEventDate() == eventDate);
        assertTrue(mockHabitEvent.getParentHabit().equals(parentHabit));
        assertTrue(mockHabitEvent.getImg() == img);
    }

    //test all setters except image because bitmap is not mocked.
    @Test
    public void testSetters(){
        mockHabitEvent.setEventDate(12019);
        assertTrue(mockHabitEvent.getEventDate() == 12019);

        Habit test_habit = new Habit();

        mockHabitEvent.setParentHabit(test_habit);
        assertTrue(mockHabitEvent.getParentHabit().equals(test_habit));

        mockHabitEvent.setComment("test comment!");
        assertTrue(mockHabitEvent.getComment().equals("test comment!"));

        mockHabitEvent.setId("12019");
        assertTrue(mockHabitEvent.getId().equals("12019"));

    }

    //test exception for when comments are too long
    @Test(expected = IllegalArgumentException.class)
    public void testCommentIllegalArgumentException() {
        mockHabitEvent.setComment("12345678901234567890123456789012345");
    }
}
