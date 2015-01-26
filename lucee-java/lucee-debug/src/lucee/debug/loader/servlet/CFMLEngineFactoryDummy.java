/**
 *
 * Copyright (c) 2014, the Railo Company Ltd. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 **/
package lucee.debug.loader.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import lucee.commons.io.SystemUtil;
import lucee.commons.lang.StringUtil;
import lucee.loader.engine.CFMLEngine;
import lucee.loader.engine.CFMLEngineFactory;
import lucee.runtime.engine.CFMLEngineImpl;

/**
 * 
 */
public final class CFMLEngineFactoryDummy extends CFMLEngineFactory {

    private static CFMLEngineFactoryDummy factory;
    private ServletConfig config;
    private CFMLEngine engine;

    
    
    
    private CFMLEngineFactoryDummy(ServletConfig config) {
    	this.config=config;
        engine = CFMLEngineImpl.getInstance(this);
        
    }
    
    /**
     * returns instance of this factory (singelton-> always the same instance)
     * @param config
     * @return Singelton Instance of the Factory
     * @throws ServletException 
     */
    public static CFMLEngine getInstance(ServletConfig config) throws ServletException {
    	
    	if(factory==null) {
    		factory=new CFMLEngineFactoryDummy(config);
    		CFMLEngineFactory.registerInstance(factory.engine);
    		
    	}
    	factory.engine.addServletConfig(config);
        
        return factory.engine;
    }


    /**
     * @see lucee.loader.engine.CFMLEngineFactory#getResourceRoot()
     */
    public File getResourceRoot() throws IOException {
    	
    	String path=SystemUtil.parsePlaceHolder(config.getInitParameter("lucee-server-directory"), config.getServletContext());
    	path=StringUtil.replace(path, "webroot", "work", true);
    	//print.err(path);
        return new File(path);
    }

    /**
     * @see lucee.loader.engine.CFMLEngineFactory#restart(java.lang.String)
     */
    public boolean restart(String password) throws IOException, ServletException {
        engine.reset();
        return true;
    }

    /**
     * @see lucee.loader.engine.CFMLEngineFactory#update(java.lang.String)
     */
    public boolean update(String password) throws IOException, ServletException {
        return true;
    }
    
    

}
