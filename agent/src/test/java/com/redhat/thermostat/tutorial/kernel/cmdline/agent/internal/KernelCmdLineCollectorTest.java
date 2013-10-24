package com.redhat.thermostat.tutorial.kernel.cmdline.agent.internal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.thermostat.storage.core.WriterID;

public class KernelCmdLineCollectorTest {

	@Ignore
	@Test
	public void canReadKernelCmdLine() {
		WriterID writer = mock(WriterID.class);
		when(writer.getWriterID()).thenReturn("ID");
		KernelCmdLineCollector collector = new KernelCmdLineCollector(writer);
		
		String actual = null;
		try {
			actual = collector.getCmdLine().getCmdLine();
		} catch (IOException e) {
			e.printStackTrace();
			actual = "WRONG!";
		}
		String expected = "BOOT_IMAGE=/vmlinuz-3.7.8-202.fc18.x86_64 root=/dev/mapper/vg_laptop-root ro rd.md=0 rd.dm=0 rd.luks=0 vconsole.keymap=us rd.lvm.lv=vg_laptop/swap rd.lvm.lv=vg_laptop/root rhgb quiet LANG=en_US.UTF-8";
		assertEquals(expected, actual);
	}
}
