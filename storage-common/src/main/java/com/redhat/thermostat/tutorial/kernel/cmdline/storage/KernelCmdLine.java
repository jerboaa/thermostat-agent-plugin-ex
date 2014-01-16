package com.redhat.thermostat.tutorial.kernel.cmdline.storage;

import java.util.Objects;

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
	
	
	private long timeStamp;
	
	public KernelCmdLine(String writerId) {
		super(writerId);
	}
	
	// Used for JSON serialization. Don't
	// explicitly use it.
	public KernelCmdLine() {
		this(null);
	}
	
	@Persist
    public void setCmdLine(String cmdLine) {
		this.cmdLine = cmdLine;
	}

    @Persist
    public String getCmdLine() {
        return cmdLine;
    }

    @Persist
	public long getTimeStamp() {
		return timeStamp;
	}

    @Persist
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
    
    public int hashCode() {
    	return Objects.hash(super.hashCode(), cmdLine, timeStamp);
    }
    
    public boolean equals(Object other) {
    	if (!(other instanceof KernelCmdLine)) {
    		return false;
    	}
    	KernelCmdLine o = (KernelCmdLine)other;
    	return super.equals(o) &&
    			cmdLine.equals(o.cmdLine) &&
    			timeStamp == o.timeStamp;
    			
    }
}
