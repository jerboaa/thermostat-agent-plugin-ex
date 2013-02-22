package com.redhat.thermostat.tutorial.kernel.cmdline.storage.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import com.redhat.thermostat.storage.core.Storage;
import com.redhat.thermostat.tutorial.kernel.cmdline.storage.KernelCmdLineDAO;

public class Activator implements BundleActivator {
	
	private ServiceRegistration reg;
	private ServiceTracker tracker;

	@Override
	public void start(final BundleContext context) throws Exception {
		// register DAO using a tracker for Storage
		tracker = new ServiceTracker(context, Storage.class.getName(), null) {
            @Override
            public Object addingService(ServiceReference reference) {
                Storage storage = (Storage) context.getService(reference);
                KernelCmdLineDAO kernelCmdlineDAO = new KernelCmdLineDAOImpl(storage);
                reg = context.registerService(KernelCmdLineDAO.class.getName(), kernelCmdlineDAO, null);
                return super.addingService(reference);
            }
            
            @Override
            public void removedService(ServiceReference reference, Object service) {
                reg.unregister();
                super.removedService(reference, service);
            }
        };
        tracker.open();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		tracker.close();
	}

}
