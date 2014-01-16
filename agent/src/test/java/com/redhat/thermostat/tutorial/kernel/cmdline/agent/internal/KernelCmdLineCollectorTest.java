package com.redhat.thermostat.tutorial.kernel.cmdline.agent.internal;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

public class KernelCmdLineCollectorTest {
	
	@Test
	public void canReadKernelCmdLine() throws Exception {
		String path = KernelCmdLineCollectorTest.class.getResource("/testKernelCmdLine.txt").getFile();
		File testFile = new File(path);
		KernelCmdLineCollector collector = new KernelCmdLineCollector(testFile);
		
		String actual = "WRONG";
		actual = collector.getCmdLine();
		String expected = "something not in proc";
		assertEquals(expected, actual);
	}
}
