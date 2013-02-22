package com.redhat.thermostat.tutorial.kernel.cmdline.cli.internal;

import java.util.Collection;

import com.redhat.thermostat.common.cli.AbstractCommand;
import com.redhat.thermostat.common.cli.CommandContext;
import com.redhat.thermostat.common.cli.CommandException;
import com.redhat.thermostat.common.utils.OSGIUtils;
import com.redhat.thermostat.storage.core.HostRef;
import com.redhat.thermostat.storage.dao.HostInfoDAO;
import com.redhat.thermostat.tutorial.kernel.cmdline.storage.KernelCmdLineDAO;

public class KernelCmdlineCommand extends AbstractCommand {

	private static final String NAME = "kernel-cmdline";
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void run(CommandContext ctxt) throws CommandException {
		OSGIUtils serviceProvider = OSGIUtils.getInstance();
		// options via plug-ins aren't supported just yet, need to work around
		// this. Let's just pick the first HostRef.
		HostInfoDAO hostsDAO = serviceProvider.getServiceAllowNull(HostInfoDAO.class);
        if (hostsDAO == null) {
            throw new CommandException("HostInfoDAO unavaialable!");
        }
        // This is also highly inefficient. Remember to do this right for a
        // real world plug-in :-)
        Collection<HostRef> hosts = hostsDAO.getAliveHosts();
        if (hosts.isEmpty()) {
        	throw new CommandException("No alive hosts. Is an agent running?");
        }
        HostRef hostRef = hosts.toArray(new HostRef[0])[0];
        serviceProvider.ungetService(HostInfoDAO.class, hostsDAO);
        KernelCmdLineDAO dao = serviceProvider.getServiceAllowNull(KernelCmdLineDAO.class);
        if (dao == null) {
        	throw new CommandException("KernelCmdLineDAO unavailable");
        }
        String cmdLine = dao.getCmdLine(hostRef);
        serviceProvider.ungetService(KernelCmdLineDAO.class, dao);
        // Here one should use appropriate formatters instead. Shame on me :(
        ctxt.getConsole().getOutput().println("Host: " + hostRef.getHostName());
        ctxt.getConsole().getOutput().println("Kernel Commandline: " + cmdLine);
	}
	
	@Override
	public boolean isStorageRequired() {
		// Let the launcher connect to storage for us
		return true;
	}

}
