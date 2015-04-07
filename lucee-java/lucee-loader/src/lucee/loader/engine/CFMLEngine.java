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
package lucee.loader.engine;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import lucee.runtime.CFMLFactory;
import lucee.runtime.PageContext;
import lucee.runtime.util.Cast;
import lucee.runtime.util.Creation;
import lucee.runtime.util.Decision;
import lucee.runtime.util.Excepton;
import lucee.runtime.util.HTTPUtil;
import lucee.runtime.util.Operation;
import lucee.runtime.util.ResourceUtil;
import lucee.runtime.util.ZipUtil;
import lucee.runtime.video.VideoUtil;

/**
 * The CFML Engine
 */
public interface CFMLEngine { 

    /**
     * Field <code>CAN_UPDATE</code>
     */
    public static int CAN_UPDATE=0;
    
    /**
     * Field <code>CAN_RESTART</code>
     */
    public static int CAN_RESTART=1; 
    public static int CAN_RESTART_ALL=CAN_RESTART; 
    public static int CAN_RESTART_CONTEXT=2; 

    public abstract  CFMLFactory getCFMLFactory(ServletContext srvContext, ServletConfig srvConfig,HttpServletRequest req) throws ServletException;
    
    /**
     * adds a servlet config 
     * @param config
     * @throws ServletException
     */
    public abstract void addServletConfig(ServletConfig config) throws ServletException;
    
    /**
     * method to invoke the engine for CFML
     * @param servlet
     * @param req
     * @param rsp
     * @throws ServletException
     * @throws IOException
     * @throws ServletException 
     */
    public void serviceCFML(HttpServlet servlet, HttpServletRequest req, HttpServletResponse rsp) throws IOException, ServletException;

    /**
     * method to invoke the engine for AMF
     * @param servlet
     * @param req
     * @param rsp
     * @throws ServletException
     * @throws IOException
     */
    public void serviceAMF(HttpServlet servlet, HttpServletRequest req, HttpServletResponse rsp) 
        throws ServletException, IOException;

    
    /* *
     * method to invoke the engine for AMF
     * @param serviceAdapter 
     * @param message
     * @return
     * /
    //public Object executeFlex(ServiceAdapter serviceAdapter, Message message);*/
    
    

    /**
     * method to invoke the engine for a simple file
     * @param servlet
     * @param req
     * @param rsp
     * @throws ServletException
     * @throws IOException
     */
    public void serviceFile(HttpServlet servlet, HttpServletRequest req, HttpServletResponse rsp) throws ServletException, IOException; 
    
    /**
     * method to invoke the engine for a Rest Requests
     * @param servlet
     * @param req
     * @param rsp
     * @throws ServletException
     * @throws IOException
     */
    public abstract void serviceRest(HttpServlet servlet, HttpServletRequest req, HttpServletResponse rsp) throws ServletException, IOException; 
    
    
    /**
     * @return returns the version of the engine in the format [x.x.x.xxx]
     */
    public String getVersion();
    
    /**
     * @return returns the stae of the version (alpha,beta,rc,final)
     */
    public String getState();
    
    /**
     * @return returns how this engine will be updated (auto, manuell)
     */
    public String getUpdateType();

    /**
     * @return return location URL to get updates for the engines
     */
    public URL getUpdateLocation();

    /**
     * checks if process has the right to do was given with type, the engine with given password
     * @param type restart type (CFMLEngine.CAN_UPDATE, CFMLEngine.CAN_RESTART)
     * @param password 
     * @return has right
     */
    public boolean can(int type, String password); 
    
    /**
     * @return returns the engine that has produced this engine
     */
    public CFMLEngineFactory getCFMLEngineFactory();

    /**
     * reset the engine
     */
    public void reset();
    
    /**
     * reset the engine
     */
    public void reset(String configId);
    
    /** 
     * return the cast util 
     * @return operaton util 
     */ 
    public Cast getCastUtil(); 
    
    /** 
     * return the operation util 
     * @return operaton util 
     */ 
    public Operation getOperatonUtil(); 

    /** 
     * returns the decision util 
     * @return decision util 
     */ 
    public Decision getDecisionUtil(); 
    
    /** 
     * returns the decision util 
     * @return decision util 
     */ 
    public Excepton getExceptionUtil();
    
    
    
    /** 
     * returns the decision util 
     * @return decision util 
     */ 
    public Creation getCreationUtil();

	/**
	 * returns the FusionDebug Engine
	 * @return IFDController
	 */
	public Object getFDController();

	/**
	 * returns the Blaze DS Util 
	 * @return Blaze DS Util 
	 */
	public Object getBlazeDSUtil(); 

	/**
	 * returns the Resource Util 
	 * @return Blaze DS Util 
	 */
	public ResourceUtil getResourceUtil(); 
	
	/**
	 * returns the HTTP Util
	 * @return the HTTP Util
	 */
	public HTTPUtil getHTTPUtil(); 
	
	/**
	 * @return return PageContext for the current PageContext
	 */
	public PageContext getThreadPageContext();

	public VideoUtil getVideoUtil();

	public ZipUtil getZipUtil();

	public abstract void cli(Map<String, String> config, ServletConfig servletConfig) throws IOException, JspException, ServletException;

	public abstract void registerThreadPageContext(PageContext pc);

	
	// FUTURE add public ConfigServer getConfigServer(String password);

    // FUTURE add public ConfigServer getConfigServer(String key, long timeNonce);

	
}