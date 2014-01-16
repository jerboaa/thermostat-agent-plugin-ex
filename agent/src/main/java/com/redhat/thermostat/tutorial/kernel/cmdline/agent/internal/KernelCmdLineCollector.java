package com.redhat.thermostat.tutorial.kernel.cmdline.agent.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import com.redhat.thermostat.storage.core.WriterID;
import com.redhat.thermostat.tutorial.kernel.cmdline.storage.KernelCmdLine;

/**
 * Reads the kernel cmdline from /proc
 *
 */
class KernelCmdLineCollector {

	private static final File defaultCmdlineFile = new File("/proc/cmdline");
	private final File cmdLineFile;
	
	// Constructor not available outside package
	KernelCmdLineCollector() {
		this.cmdLineFile = defaultCmdlineFile;
	}
	
	// For testing only
	KernelCmdLineCollector(File file) {
		this.cmdLineFile = file;
	}
	
	String getCmdLine() throws IOException {
		String cmdLine = "";
		try (FileInputStream in = new FileInputStream(cmdLineFile)) {
			Scanner scanner = new Scanner(in);
			cmdLine = scanner.nextLine();
			scanner.close();
		}
		return cmdLine;
	}
}
