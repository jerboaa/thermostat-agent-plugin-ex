package com.redhat.thermostat.tutorial.kernel.cmdline.cli.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.redhat.thermostat.common.cli.CommandRegistryImpl;

public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		// register your kernel cmd line command here
		// just uncomment the following line
		//new CommandRegistryImpl(context).registerCommand(new KernelCmdlineCommand());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// gets unregistered automatically, noop
	}

}
