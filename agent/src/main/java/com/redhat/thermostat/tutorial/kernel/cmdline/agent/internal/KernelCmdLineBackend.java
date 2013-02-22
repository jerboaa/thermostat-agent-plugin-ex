package com.redhat.thermostat.tutorial.kernel.cmdline.agent.internal;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.redhat.thermostat.backend.BaseBackend;
import com.redhat.thermostat.common.ApplicationService;
import com.redhat.thermostat.common.Ordered;
import com.redhat.thermostat.common.Timer;
import com.redhat.thermostat.common.Timer.SchedulingType;
import com.redhat.thermostat.common.TimerFactory;
import com.redhat.thermostat.common.Version;
import com.redhat.thermostat.common.utils.LoggingUtils;
import com.redhat.thermostat.tutorial.kernel.cmdline.storage.KernelCmdLineDAO;

public class KernelCmdLineBackend extends BaseBackend {

	private static final Logger log = LoggingUtils.getLogger(KernelCmdLineBackend.class);
	private static final int CHECK_INTERVAL_MINUTES = 60;
	
	private final ApplicationService appService;
	private Timer timer;
	private final KernelCmdLineCollector collector;
	private final KernelCmdLineDAO dao;
	private boolean started;
	
	public KernelCmdLineBackend(ApplicationService service, Version version, KernelCmdLineDAO dao) {
		super("Kernel CMD line Backend",
                "Gathers kernel cmdline of a host",
                "Red Hat, Inc.",
                version.getVersionNumber());
		this.appService = service;
		this.collector = new KernelCmdLineCollector();
		this.dao = dao;
		this.started = false;
	}
	
	@Override
	public boolean activate() {
		// This is silly and shouldn't really check every 30 minutes since
		// the actual data we collect only changes on reboot (if at all).
		// Anyhow, it's good to illustrate thermostat timers using appService.
		TimerFactory timerFactory = appService.getTimerFactory();
		timer = timerFactory.createTimer();
        timer.setDelay(CHECK_INTERVAL_MINUTES);
        timer.setInitialDelay(0);
        timer.setSchedulingType(SchedulingType.FIXED_RATE);
        timer.setTimeUnit(TimeUnit.MINUTES);
        timer.setAction(new Runnable() {

            @Override
            public void run() {
                try {
                    String cmdLine = collector.getCmdLine();
                    dao.putCmdLine(cmdLine);
                } catch (IOException e) {
                    log.log(Level.WARNING, e.getMessage(), e);
                }
            }
        });
        timer.start();
        started = true;
        return true;
	}

	@Override
	public boolean deactivate() {
		started = false;
        timer.stop();
        return true;
	}

	@Override
	public boolean isActive() {
		return started;
	}

	@Override
	public int getOrderValue() {
		// offset should be < 100 in this case 2.
		return Ordered.ORDER_CPU_GROUP + 2;
	}

}
