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

import java.util.logging.Logger;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.Service;

import com.redhat.thermostat.common.utils.LoggingUtils;
import com.redhat.thermostat.storage.core.HostRef;
import com.redhat.thermostat.storage.core.Key;
import com.redhat.thermostat.storage.core.PreparedStatement;
import com.redhat.thermostat.storage.core.Storage;
import com.redhat.thermostat.storage.core.WriterID;
import com.redhat.thermostat.storage.dao.AbstractDao;
import com.redhat.thermostat.storage.dao.AbstractDaoQuery;
import com.redhat.thermostat.storage.dao.AbstractDaoStatement;
import com.redhat.thermostat.tutorial.kernel.cmdline.storage.KernelCmdLine;
import com.redhat.thermostat.tutorial.kernel.cmdline.storage.KernelCmdLineDAO;

@Component(immediate = true)
@Service(value = KernelCmdLineDAO.class)
public class KernelCmdLineDAOImpl extends AbstractDao implements KernelCmdLineDAO {
	
	static final String REPLACE_DESCRIPTOR = "REPLACE " + kernelCmdLineCategory.getName() +
			                                                  " SET '" + Key.AGENT_ID.getName() + "' = ?s , " +
			                                                       "'" + CMD_LINE_KEY.getName() + "' = ?s , " +
			                                                       "'" + Key.TIMESTAMP.getName() + "' = ?l " +
			                                                       "WHERE '" + Key.AGENT_ID.getName() + "' = ?s";
	static final String QUERY_DESCRIPTOR = "QUERY " + kernelCmdLineCategory.getName() + " WHERE " +
			                                                       "'" + Key.AGENT_ID.getName() + "' = ?s";
	@Reference
	private Storage storage;
	@Reference
	private WriterID writerId;
	
	public KernelCmdLineDAOImpl() {
		// Constructor for DS
	}
    
    @Activate
    private void activate() {
        storage.registerCategory(kernelCmdLineCategory);
    }
    
	@Override
    public void putCmdLine(final String cmdLine) {
		executeStatement(new AbstractDaoStatement<KernelCmdLine>(storage, kernelCmdLineCategory, REPLACE_DESCRIPTOR) {

            @Override
            public PreparedStatement<KernelCmdLine> customize(PreparedStatement<KernelCmdLine> preparedStatement) {
                preparedStatement.setString(0, writerId.getWriterID());
                preparedStatement.setString(1, cmdLine);
                preparedStatement.setLong(2, System.currentTimeMillis());
                preparedStatement.setString(3, writerId.getWriterID());
                return preparedStatement;
            }
        });
	}
    
	@Override
    public String getCmdLine(final HostRef ref) {
		KernelCmdLine cmdLine = executeQuery(new AbstractDaoQuery<KernelCmdLine>(storage, kernelCmdLineCategory, QUERY_DESCRIPTOR) {

			@Override
			public PreparedStatement<KernelCmdLine> customize(PreparedStatement<KernelCmdLine> query) {
				query.setString(0, ref.getAgentId());
				return query;
			}
		}).head();
		return cmdLine.getCmdLine();
    }

	@Override
	protected Logger getLogger() {
		return LoggingUtils.getLogger(KernelCmdLineDAOImpl.class);
	}

}

