package com.redhat.thermostat.tutorial.kernel.cmdline.agent.internal;

import com.redhat.thermostat.backend.BaseBackend;
import com.redhat.thermostat.common.ApplicationService;
import com.redhat.thermostat.common.Timer;
import com.redhat.thermostat.common.Version;

public class KernelCmdLineBackend extends BaseBackend {

	private final ApplicationService appService;
	private Timer timer;
	
	public KernelCmdLineBackend(ApplicationService service, Version version) {
		super("Kernel CMD line Backend",
                "Gathers kernel cmdline of a host",
                "Red Hat, Inc.",
                version.getVersionNumber());
		this.appService = service;
	}
	@Override
	public boolean activate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deactivate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getOrderValue() {
		// TODO Auto-generated method stub
		return 0;
	}

}
