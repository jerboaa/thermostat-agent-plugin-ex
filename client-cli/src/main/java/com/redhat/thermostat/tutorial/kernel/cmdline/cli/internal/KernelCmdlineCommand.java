package com.redhat.thermostat.tutorial.kernel.cmdline.cli.internal;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.redhat.thermostat.common.cli.AbstractCommand;
import com.redhat.thermostat.common.cli.Arguments;
import com.redhat.thermostat.common.cli.CommandContext;
import com.redhat.thermostat.common.cli.CommandException;
import com.redhat.thermostat.shared.locale.LocalizedString;
import com.redhat.thermostat.storage.core.HostRef;
import com.redhat.thermostat.storage.dao.AgentInfoDAO;
import com.redhat.thermostat.storage.dao.HostInfoDAO;
import com.redhat.thermostat.storage.model.AgentInformation;
import com.redhat.thermostat.storage.model.HostInfo;
import com.redhat.thermostat.tutorial.kernel.cmdline.storage.KernelCmdLineDAO;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class KernelCmdlineCommand extends AbstractCommand {

	public static final String NAME = "kernel-cmdline";
	private static final String AGENT_ARG = "hostId";
	private static final String NO_KERNEL_CMD_LINE = "<UNKNOWN>";
	
	private final BundleContext context;
	
	KernelCmdlineCommand(BundleContext context) {
		this.context = context;
	}

	@Override
	public void run(CommandContext ctxt) throws CommandException {
        Arguments args = ctxt.getArguments();
        if (!args.hasArgument(AGENT_ARG)) {
        	throw new CommandException(new LocalizedString("Agent argument required!"));
        }
        AgentInfoDAO agentInfo = getDaoService(AgentInfoDAO.class);
        if (agentInfo == null) {
        	throw new CommandException(new LocalizedString("AgentInfoDAO unavaialable!"));
        }
        String agentId = args.getArgument(AGENT_ARG);
        
        // Use our DAO to resolve the agent, i.e. host
        HostRef hostRef = new HostRef(agentId, "not-used");
        AgentInformation resolvedAgent = agentInfo.getAgentInformation(hostRef);
        if (resolvedAgent == null) {
        	throw new CommandException(new LocalizedString("Unknown agentId: " + agentId));
        }
        HostInfoDAO hostInfo = getDaoService(HostInfoDAO.class);
        if (hostInfo == null) {
        	throw new CommandException(new LocalizedString("HostInfoDAO unavaialable!"));
        }
        HostInfo info = hostInfo.getHostInfo(hostRef);
        KernelCmdLineDAO kernelCmdLineDAO = getDaoService(KernelCmdLineDAO.class);
        if (kernelCmdLineDAO == null) {
        	throw new CommandException(new LocalizedString("KernelCmdLineDAO unavailable"));
        }
        String cmdLine = kernelCmdLineDAO.getCmdLine(hostRef);
        if (cmdLine == null) {
        	cmdLine = NO_KERNEL_CMD_LINE;
        }
        // Here one should use appropriate formatters instead. Shame on me :(
        ctxt.getConsole().getOutput().println("Host: " + info.getHostname());
        ctxt.getConsole().getOutput().println("Kernel Commandline: " + cmdLine);
	}
	
	private <T> T getDaoService(Class<T> clazz) {
		ServiceReference ref = context.getServiceReference(clazz.getName());
		@SuppressWarnings("unchecked")
		T service = (T) context.getService(ref);
		return service;
	}
	
	@Override
	public boolean isStorageRequired() {
		// Let the launcher connect to storage for us
		return true;
	}

}
