package com.redhat.thermostat.tutorial.kernel.cmdline.agent.internal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.thermostat.storage.core.WriterID;

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
