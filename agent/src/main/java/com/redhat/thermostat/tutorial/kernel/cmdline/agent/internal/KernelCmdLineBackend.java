package com.redhat.thermostat.tutorial.kernel.cmdline.agent.internal;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.redhat.thermostat.backend.Backend;
import com.redhat.thermostat.common.ApplicationService;
import com.redhat.thermostat.common.Ordered;
import com.redhat.thermostat.common.Timer;
import com.redhat.thermostat.common.Timer.SchedulingType;
import com.redhat.thermostat.common.TimerFactory;
import com.redhat.thermostat.common.utils.LoggingUtils;
import com.redhat.thermostat.storage.core.WriterID;
import com.redhat.thermostat.tutorial.kernel.cmdline.storage.KernelCmdLineDAO;

@Component(immediate = true)
@Service(value = Backend.class)
public class KernelCmdLineBackend implements Backend {

	private static final Logger log = LoggingUtils.getLogger(KernelCmdLineBackend.class);
	private static final int CHECK_INTERVAL_MINUTES = 60;
	private static final String NAME = "Kernel CMD line Backend";
	private static final String DESC = "Gathers kernel cmdline of a host";
	private static final String VENDOR = "Red Hat, Inc.";
	private static final String VERSION = "0.0.4";
	
	private final KernelCmdLineCollector collector;
	private Timer timer;
	private boolean started;
	@Reference
	private ApplicationService appService;
	@Reference
	private KernelCmdLineDAO dao;
	@Reference
	private WriterID writer;
	
	public KernelCmdLineBackend() {
		this.collector = new KernelCmdLineCollector();
		this.started = false;
	}
	
	@Activate
	protected void dsActivate() {
		// Nothing. Just override Backend.activate which would be used
		// by default.
	}
	
	@Deactivate
	protected void dsDeactivate() {
		// Nothing. Just override Backend.activate which would be used
		// by default.
	}
	
	@Override
	public boolean activate() {
		createAndStartTimer();
        started = true;
        return true;
	}

	private void createAndStartTimer() {
		// This is silly and shouldn't really check every 60 minutes since
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
                    log.finest("putting kernel cmd line: " + cmdLine + " to storage.");
                    dao.putCmdLine(cmdLine);
                } catch (IOException e) {
                    log.log(Level.WARNING, e.getMessage(), e);
                }
            }
        });
        timer.start();
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

	@Override
	public String getDescription() {
		return DESC;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean getObserveNewJvm() {
		return true;
	}

	@Override
	public String getVendor() {
		return VENDOR;
	}

	@Override
	public String getVersion() {
		return VERSION;
	}

	@Override
	public void setObserveNewJvm(boolean newVal) {
		// no-op
	}
	
	@Override
	public String toString() {
		return String.format("name=%s, version=%s, vendor=%s, description=%s",
				              NAME,
				              VERSION,
				              VENDOR,
				              DESC);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (KernelCmdLineBackend.class != other.getClass()) {
			return false;
		}
		KernelCmdLineBackend o = (KernelCmdLineBackend)other;
		return Objects.equals(getName(), o.getName()) &&
			   Objects.equals(getVersion(), o.getVersion()) &&
			   Objects.equals(getVendor(), o.getVendor());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getName(), getVersion(), getVendor());
	}

}
