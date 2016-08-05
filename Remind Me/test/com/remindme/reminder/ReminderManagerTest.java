package com.remindme.reminder;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class ReminderManagerTest {
	
	@Test
	public void getTagsTest(){
		ReminderManager rm = new ReminderManager();
		String reminder1 = null;
		ArrayList<String> tags = rm.getTags(reminder1);
		assertEquals(tags, null);
		
		
		String reminder2 = "";
		tags = rm.getTags(reminder2);
		assertEquals(tags == null, false);
		assertEquals(tags.size(), 0);
		
		String reminder3 = "this is a reminder";
		tags = rm.getTags(reminder3);
		assertEquals(tags == null, false);
		assertEquals(tags.size(), 0);
		
		
		String reminder4 = "this is brents reminder #test #check";
		tags = rm.getTags(reminder4);
		assertEquals(tags == null, false);
		assertEquals(tags.size(), 2);
		assertEquals(tags.get(0), "#test");
		assertEquals(tags.get(1), "#check");
	}
	
	@Test
	public void getStringsCommaDeliminatedTest(){
		ReminderManager rm = new ReminderManager();
		String reminder1 = null;
		ArrayList<String> tags = rm.getStringsFromCommaDeliminatedString(reminder1);
		assertEquals(tags, null);
		
		
		String reminder2 = "";
		tags = rm.getStringsFromCommaDeliminatedString(reminder2);
		assertEquals(tags == null, false);
		assertEquals(tags.size(), 0);
		
		String reminder3 = "this is a reminder";
		tags = rm.getStringsFromCommaDeliminatedString(reminder3);
		assertEquals(tags == null, false);
		assertEquals(tags.size(), 1);
		
		
		String reminder4 = "this is brents reminder #test #check";
		tags = rm.getStringsFromCommaDeliminatedString(reminder4);
		assertEquals(tags == null, false);
		assertEquals(tags.size(), 1);
		assertEquals(tags.get(0), "this is brents reminder #test #check");
		
		String reminder5 = "#soccer, #hockey";
		tags = rm.getStringsFromCommaDeliminatedString(reminder5);
		assertEquals(tags == null, false);
		assertEquals(tags.size(), 2);
		assertEquals(tags.get(0), "#soccer");
		assertEquals(tags.get(1), "#hockey");
	}

	@Test
	public void getTagsCommaDelimitedTest(){
		ReminderManager rm = new ReminderManager();
		ArrayList<String> list = null;
		String result;
		
		result = rm.getTagsCommaDelimited(list);
		assertEquals(result, null);

		list = new ArrayList<String>();
		result = rm.getTagsCommaDelimited(list);
		assertEquals(result, null);
		
		list.add("#hello");
		result = rm.getTagsCommaDelimited(list);
		assertEquals(result, "#hello");
		
		list.add("#soccer");
		result = rm.getTagsCommaDelimited(list);
		assertEquals(result, "#hello, #soccer");
	}
}
