package com.redhat.thermostat.tutorial.kernel.cmdline.storage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class KernelCmdLineTest {

	@Test
	public void testEquals() {
		KernelCmdLine cmdLine = new KernelCmdLine();
		cmdLine.setAgentId("foo");
		cmdLine.setCmdLine("bar");
		long timeStamp = System.currentTimeMillis();
		cmdLine.setTimeStamp(timeStamp);
		
		assertTrue(cmdLine.equals(cmdLine));
		KernelCmdLine cmdLine2 = new KernelCmdLine();
		assertFalse(cmdLine.equals(cmdLine2));
		assertFalse(cmdLine.equals("I'm a string"));
		
		cmdLine2.setAgentId("foo");
		assertFalse(cmdLine.equals(cmdLine2));
		cmdLine2.setCmdLine("bar");
		assertFalse(cmdLine.equals(cmdLine2));
		cmdLine2.setTimeStamp(timeStamp);
		assertTrue(cmdLine.equals(cmdLine2));
	}
	
	@Test
	public void testHashCode() {
		KernelCmdLine cmdLine = new KernelCmdLine();
		cmdLine.setAgentId("foo");
		cmdLine.setCmdLine("bar");
		long timeStamp = System.currentTimeMillis();
		cmdLine.setTimeStamp(timeStamp);
		
		assertTrue(cmdLine.hashCode() == cmdLine.hashCode());
		KernelCmdLine cmdLine2 = new KernelCmdLine();
		assertFalse(cmdLine.hashCode() == cmdLine2.hashCode());
		assertFalse(cmdLine.hashCode() == "I'm a string".hashCode());
		
		cmdLine2.setAgentId("foo");
		assertFalse(cmdLine.hashCode() == cmdLine2.hashCode());
		cmdLine2.setCmdLine("bar");
		assertFalse(cmdLine.hashCode() == cmdLine2.hashCode());
		cmdLine2.setTimeStamp(timeStamp);
		assertTrue(cmdLine.hashCode() == cmdLine2.hashCode());
	}
	
}
