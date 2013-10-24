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
public class KernelCmdLineCollector {

	private File cmdlineFile = new File("/proc/cmdline");
	private WriterID writer;
	
	// Constructor not available outside package
	KernelCmdLineCollector(WriterID writer) {
		this.writer = writer;
	}
	
	public KernelCmdLine getCmdLine() throws IOException {
		String cmdLine = "";
		try (FileInputStream in = new FileInputStream(cmdlineFile)) {
			Scanner scanner = new Scanner(in);
			cmdLine = scanner.nextLine();
			scanner.close();
		}
		return new KernelCmdLine(writer.getWriterID(), cmdLine);
	}
}
