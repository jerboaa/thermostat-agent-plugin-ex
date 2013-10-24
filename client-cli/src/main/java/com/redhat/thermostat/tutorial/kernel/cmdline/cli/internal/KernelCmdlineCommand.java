package com.redhat.thermostat.tutorial.kernel.cmdline.cli.internal;

import java.util.Collection;

import com.redhat.thermostat.common.cli.AbstractCommand;
import com.redhat.thermostat.common.cli.CommandContext;
import com.redhat.thermostat.common.cli.CommandException;
import com.redhat.thermostat.shared.locale.LocalizedString;
import com.redhat.thermostat.storage.core.HostRef;
import com.redhat.thermostat.storage.dao.HostInfoDAO;
import com.redhat.thermostat.tutorial.kernel.cmdline.storage.KernelCmdLineDAO;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class KernelCmdlineCommand extends AbstractCommand {

    private BundleContext bundleContext;

    public KernelCmdlineCommand(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

	@Override
	public void run(CommandContext ctxt) throws CommandException {
		// options via plug-ins aren't supported just yet, need to work around
		// this. Let's just pick the first HostRef.
        ServiceReference hostDaoRef = bundleContext.getServiceReference(HostInfoDAO.class.getName());
        if (hostDaoRef == null) {
            throw new CommandException(new LocalizedString("HostInfoDAO unavaialable!"));
        }
		HostInfoDAO hostsDAO = (HostInfoDAO) bundleContext.getService(hostDaoRef);
        if (hostsDAO == null) {
            throw new CommandException(new LocalizedString("HostInfoDAO unavaialable!"));
        }
        // This is also highly inefficient. Remember to do this right for a
        // real world plug-in :-)
        Collection<HostRef> hosts = hostsDAO.getAliveHosts();
        if (hosts.isEmpty()) {
        	throw new CommandException(new LocalizedString("No alive hosts. Is an agent running?"));
        }
        HostRef hostRef = hosts.toArray(new HostRef[0])[0];
        hostsDAO = null;
        bundleContext.ungetService(hostDaoRef);

        ServiceReference kernelDaoRef = bundleContext.getServiceReference(KernelCmdLineDAO.class.getName());
        if (kernelDaoRef == null) {
        	throw new CommandException(new LocalizedString("KernelCmdLineDAO unavailable"));
        }
        KernelCmdLineDAO dao = (KernelCmdLineDAO) bundleContext.getService(kernelDaoRef);
        if (dao == null) {
        	throw new CommandException(new LocalizedString("KernelCmdLineDAO unavailable"));
        }
        String cmdLine = dao.getCmdLine(hostRef);
        bundleContext.ungetService(kernelDaoRef);
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
