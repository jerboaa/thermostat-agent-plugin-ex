/*
 * Copyright 2012, 2013 Red Hat, Inc.
 *
 * This file is part of Thermostat.
 *
 * Thermostat is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2, or (at your
 * option) any later version.
 *
 * Thermostat is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Thermostat; see the file COPYING.  If not see
 * <http://www.gnu.org/licenses/>.
 *
 * Linking this code with other modules is making a combined work
 * based on this code.  Thus, the terms and conditions of the GNU
 * General Public License cover the whole combination.
 *
 * As a special exception, the copyright holders of this code give
 * you permission to link this code with independent modules to
 * produce an executable, regardless of the license terms of these
 * independent modules, and to copy and distribute the resulting
 * executable under terms of your choice, provided that you also
 * meet, for each linked independent module, the terms and conditions
 * of the license of that module.  An independent module is a module
 * which is not derived from or based on this code.  If you modify
 * this code, you may extend this exception to your version of the
 * library, but you are not obligated to do so.  If you do not wish
 * to do so, delete this exception statement from your version.
 */

package com.redhat.thermostat.tutorial.kernel.cmdline.storage.internal;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.redhat.thermostat.common.utils.LoggingUtils;
import com.redhat.thermostat.storage.core.Cursor;
import com.redhat.thermostat.storage.core.DescriptorParsingException;
import com.redhat.thermostat.storage.core.HostRef;
import com.redhat.thermostat.storage.core.Key;
import com.redhat.thermostat.storage.core.PreparedStatement;
import com.redhat.thermostat.storage.core.StatementDescriptor;
import com.redhat.thermostat.storage.core.StatementExecutionException;
import com.redhat.thermostat.storage.core.Storage;
import com.redhat.thermostat.tutorial.kernel.cmdline.storage.KernelCmdLine;
import com.redhat.thermostat.tutorial.kernel.cmdline.storage.KernelCmdLineDAO;

public class KernelCmdLineDAOImpl implements KernelCmdLineDAO {
	
	private static final Logger logger = LoggingUtils.getLogger(KernelCmdLineDAOImpl.class);
	private static final String REPLACE_DESC = "REPLACE " + kernelCmdLineCategory.getName() + " SET '" + Key.AGENT_ID.getName() + "' = ?s , '" + cmdLineKey.getName() + "' = ?s WHERE '" + Key.AGENT_ID.getName() + "' = ?s";
	private static final String QUERY_DESC = "QUERY " + kernelCmdLineCategory.getName() + " WHERE '" + Key.AGENT_ID.getName() + "' = ?s LIMIT 1";

	private final Storage storage;
	
    KernelCmdLineDAOImpl(Storage storage) {
    	this.storage = storage;
        storage.registerCategory(kernelCmdLineCategory);
    }
    
	@Override
    public void putCmdLine(KernelCmdLine cmdLine) {
		StatementDescriptor<KernelCmdLine> desc = new StatementDescriptor<>(kernelCmdLineCategory, REPLACE_DESC);
		PreparedStatement<KernelCmdLine> stmt;
		try {
			stmt = storage.prepareStatement(desc);
			stmt.setString(0, cmdLine.getAgentId());
			stmt.setString(1, cmdLine.getCmdLine());
			stmt.setString(2, cmdLine.getAgentId());
			stmt.execute();
		} catch (DescriptorParsingException e) {
			logger.log(Level.SEVERE, "Failed to parse statement description", e);
		} catch (StatementExecutionException e) {
			logger.log(Level.SEVERE, "Failed to execute prepared statement", e);
		}
	}
    
	@Override
    public String getCmdLine(HostRef ref) {
		String cmdLine = "UNKNOWN";
		StatementDescriptor<KernelCmdLine> desc = new StatementDescriptor<>(kernelCmdLineCategory, QUERY_DESC);
		PreparedStatement<KernelCmdLine> stmt;
		try {
			stmt = storage.prepareStatement(desc);
			stmt.setString(0, ref.getAgentId());
			Cursor<KernelCmdLine> cmdLineCursor = stmt.executeQuery();
			if (cmdLineCursor.hasNext()) {
				cmdLine =  cmdLineCursor.next().getCmdLine();
			}
		} catch (DescriptorParsingException e) {
			logger.log(Level.SEVERE, "Failed to parse statement description", e);
		} catch (StatementExecutionException e) {
			logger.log(Level.SEVERE, "Failed to execute prepared statement", e);
		}
        
        return cmdLine;
    }

}

