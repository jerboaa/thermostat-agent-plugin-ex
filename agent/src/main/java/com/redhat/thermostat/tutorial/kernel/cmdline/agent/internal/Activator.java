package com.redhat.thermostat.tutorial.kernel.cmdline.agent.internal;

import java.util.Map;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.redhat.thermostat.backend.Backend;
import com.redhat.thermostat.backend.BackendService;
import com.redhat.thermostat.common.ApplicationService;
import com.redhat.thermostat.common.MultipleServiceTracker;
import com.redhat.thermostat.common.MultipleServiceTracker.Action;
import com.redhat.thermostat.common.Version;
import com.redhat.thermostat.tutorial.kernel.cmdline.storage.KernelCmdLineDAO;

public class Activator implements BundleActivator {

	private MultipleServiceTracker tracker;
	private ServiceRegistration reg;
	private Backend backend;
	
	@Override
	public void start(final BundleContext context) throws Exception {
		Class<?>[] deps = new Class<?>[] {
                BackendService.class, KernelCmdLineDAO.class, ApplicationService.class
        };
        tracker = new MultipleServiceTracker(context, deps, new Action() {
            @Override
            public void dependenciesAvailable(Map<String, Object> services) {
                ApplicationService appService = (ApplicationService) services.get(ApplicationService.class.getName());
                KernelCmdLineDAO dao = (KernelCmdLineDAO) services.get(KernelCmdLineDAO.class.getName());
                Version version = new Version(context.getBundle());
                backend = new KernelCmdLineBackend(appService, version, dao);
                reg = context.registerService(Backend.class.getName(), backend, null);
            }

            @Override
            public void dependenciesUnavailable() {
                if (backend.isActive()) {
                    backend.deactivate();
                }
                reg.unregister();
            }
        });
        tracker.open();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		tracker.close();
	}

}
