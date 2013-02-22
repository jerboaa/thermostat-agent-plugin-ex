package com.redhat.thermostat.tutorial.kernel.cmdline.agent.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * Reads the kernel cmdline from /proc
 *
 */
public class KernelCmdLineCollector {

	private File cmdlineFile = new File("/proc/cmdline");
	
	// Constructor not available outside package
	KernelCmdLineCollector() {
	}
	
	public String getCmdLine() throws IOException {
		String cmdLine = "";
		try (FileInputStream in = new FileInputStream(cmdlineFile)) {
			Scanner scanner = new Scanner(in);
			cmdLine = scanner.nextLine();
			scanner.close();
		}
		return cmdLine;
	}
}
