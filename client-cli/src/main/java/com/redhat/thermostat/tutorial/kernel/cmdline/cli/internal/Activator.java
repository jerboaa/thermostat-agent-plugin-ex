package com.redhat.thermostat.tutorial.kernel.cmdline.cli.internal;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.redhat.thermostat.common.cli.Command;

public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		// register your kernel cmd line command here
		Hashtable<String, String> props = new Hashtable<>();
		props.put(Command.NAME, "kernel-cmdline");
		context.registerService(Command.class.getName(), new KernelCmdlineCommand(context), props);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// gets unregistered automatically, noop
	}

}
