package com.redhat.thermostat.tutorial.kernel.cmdline.agent.internal;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.redhat.thermostat.tutorial.kernel.cmdline.agent.internal.KernelCmdLineCollector;


public class KernelCmdLineCollectorTest {

	@Test
	public void canReadKernelCmdLine() {
		KernelCmdLineCollector collector = new KernelCmdLineCollector();
		
		String actual = null;
		try {
			actual = collector.getCmdLine();
		} catch (IOException e) {
			e.printStackTrace();
			actual = "WRONG!";
		}
		String expected = "BOOT_IMAGE=/vmlinuz-3.7.8-202.fc18.x86_64 root=/dev/mapper/vg_laptop-root ro rd.md=0 rd.dm=0 rd.luks=0 vconsole.keymap=us rd.lvm.lv=vg_laptop/swap rd.lvm.lv=vg_laptop/root rhgb quiet LANG=en_US.UTF-8";
		assertEquals(expected, actual);
	}
}
