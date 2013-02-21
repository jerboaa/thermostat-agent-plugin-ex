package com.redhat.thermostat.tutorial.kernel.cmdline.storage;

import com.redhat.thermostat.storage.core.Entity;
import com.redhat.thermostat.storage.core.Persist;
import com.redhat.thermostat.storage.model.BasePojo;

/**
 * This is the model class which gets persisted
 *
 */
@Entity
public class KernelCmdLine extends BasePojo {

	private String cmdLine;
	
	@Persist
    public void setCmdLine(String cmdLine) {
		this.cmdLine = cmdLine;
	}

    @Persist
    public String getCmdLine() {
        return cmdLine;
    }
	
}
