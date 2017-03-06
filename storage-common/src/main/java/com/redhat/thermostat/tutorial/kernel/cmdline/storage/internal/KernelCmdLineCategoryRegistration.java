package com.redhat.thermostat.tutorial.kernel.cmdline.storage.internal;

import java.util.HashSet;
import java.util.Set;

import com.redhat.thermostat.storage.core.auth.CategoryRegistration;

public class KernelCmdLineCategoryRegistration implements CategoryRegistration {

	@Override
	public Set<String> getCategoryNames() {
		Set<String> categories = new HashSet<>();
		categories.add(KernelCmdLineDAOImpl.kernelCmdLineCategory.getName());
		return categories;
	}

}
