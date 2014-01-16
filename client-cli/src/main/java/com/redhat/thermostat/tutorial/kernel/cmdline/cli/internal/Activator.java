package com.redhat.thermostat.tutorial.kernel.cmdline.cli.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.redhat.thermostat.common.cli.CommandRegistry;
import com.redhat.thermostat.common.cli.CommandRegistryImpl;

public class Activator implements BundleActivator {

	private CommandRegistry reg;

	@Override
	public void start(BundleContext context) throws Exception {
		KernelCmdlineCommand cmd = new KernelCmdlineCommand(context);
		reg = new CommandRegistryImpl(context);
		reg.registerCommand(KernelCmdlineCommand.NAME, cmd);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (reg != null) {
			// unregisters commands which this registry registered
			reg.unregisterCommands();
		}
	}

}
