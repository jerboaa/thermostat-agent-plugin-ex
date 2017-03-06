package com.redhat.thermostat.tutorial.kernel.cmdline.storage.internal;

import java.util.HashSet;
import java.util.Set;

import com.redhat.thermostat.storage.core.auth.StatementDescriptorRegistration;

public class KernelCmdLineDescriptorRegistration implements StatementDescriptorRegistration {

	@Override
	public Set<String> getStatementDescriptors() {
		Set<String> descriptors = new HashSet<>();
		descriptors.add(KernelCmdLineDAOImpl.QUERY_DESCRIPTOR);
		descriptors.add(KernelCmdLineDAOImpl.REPLACE_DESCRIPTOR);
		return descriptors;
	}

}
