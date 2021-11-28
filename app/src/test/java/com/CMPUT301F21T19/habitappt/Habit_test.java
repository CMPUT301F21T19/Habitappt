package com.CMPUT301F21T19.habitappt;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.CMPUT301F21T19.habitappt.Entities.Habit;
import com.CMPUT301F21T19.habitappt.Entities.HabitEvent;

import java.util.ArrayList;

public class Habit_test {

    Habit mockHabit;

    String title = "test habit";
    String reason = "test reason";
    long dateToStart = 0;
    String test_id = "-1";
    Boolean privacy = true;
    int index = 0;

    ArrayList<Boolean> datesToDo;

    ArrayList<HabitEvent> habitEvents = new ArrayList<>();

    @Before
    public void setup(){

        datesToDo = new ArrayList<>();
        for(int i=0;i<7;i++){
            datesToDo.add(false);
        }

        for(int i=0;i<5;i++){
            habitEvents.add(new HabitEvent("comment",0,new Habit(),"-1"));
        }

        mockHabit = new Habit(title,reason,dateToStart,datesToDo,test_id,privacy,index);


    }

    //test all of habits getters!
    @Test
    public void testGetters(){

        mockHabit.setHabitEvents(habitEvents);

        assertTrue(mockHabit.getId().equals(test_id));
        assertTrue(mockHabit.getDateToStart() == dateToStart);
        assertTrue(mockHabit.getReason().equals(reason));
        assertTrue(mockHabit.getIsPrivate() == privacy);

        mockHabit.setDateSelected(2,true);
        for(int i=0;i<7;i++){
            assertTrue(mockHabit.getDateSelected(i) == datesToDo.get(i));
        }

        assertTrue(mockHabit.getHabitEvents().equals(habitEvents));

        assertTrue(mockHabit.getWeekly().equals(datesToDo));

    }

    //test all of habits setters!
    @Test
    public void testSetters(){
        mockHabit.setTitle("test2");
        assertTrue(mockHabit.getTitle().equals("test2"));
        mockHabit.setHabitEvents(habitEvents);
        assertTrue(mockHabit.getHabitEvents().equals(habitEvents));
        mockHabit.setDateToStart(123123);
        assertTrue(mockHabit.getDateToStart() == 123123);
        mockHabit.setReason("test2");
        assertTrue(mockHabit.getReason().equals("test2"));
        mockHabit.setIsPrivate(false);
        assertTrue(mockHabit.getIsPrivate() == false);
        mockHabit.setDateSelected(2,true);
        assertTrue(mockHabit.getDateSelected(2) == true);
//        mockHabit.setScore((500));
//        assertTrue(mockHabit.getScore() == 500);
    }

    //test exception for when title is too long
    @Test(expected = IllegalArgumentException.class)
    public void testTitleIllegalArgumentException() {
        mockHabit.setTitle("123456789012345678901234567890");
    }

    //test exception for when reason is too long
    @Test(expected = IllegalArgumentException.class)
    public void testReasonIllegalArgumentException() {
        mockHabit.setReason("12345678901234567890123456789012345");
    }

    //test functionality of setDateSelected and getDateSelected
    @Test
    public void testDateSelected(){

        for(int i=0;i<7;i++){
            mockHabit.setDateSelected(i,true);
            assertTrue(mockHabit.getDateSelected(i) == true);
        }

        assertNull(mockHabit.getDateSelected(20));
        assertFalse(mockHabit.setDateSelected(20,true));

        assertNull(mockHabit.getDateSelected(-1));
        assertFalse(mockHabit.setDateSelected(-1,true));
    }
}
