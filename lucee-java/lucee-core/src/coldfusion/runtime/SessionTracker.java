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
package coldfusion.runtime;

import lucee.runtime.CFMLFactoryImpl;
import lucee.runtime.PageContext;
import lucee.runtime.engine.ThreadLocalPageContext;
import lucee.runtime.type.Struct;
import lucee.runtime.type.scope.ScopeContext;

public class SessionTracker {

	/*
	 * prepare to restrict access to this class
	 * private String webAdminPassword;

	public SessionTracker(){
		throw new RuntimeException("for Security reasons we have restricted the usage of the class SessionTracker, " +
				"you now can only use this class by defining the web admin password, when you call the constructor of this class, " +
				"so instead of just doing the following: SessionTracker=createObject('java','coldfusion.runtime.SessionTracker') " +
				"and then use it, you have to call the constructor with the web admin password: SessionTracker=createObject('java','coldfusion.runtime.SessionTracker').init('yourwebadminpassword')");
		
		
	}
	public SessionTracker(String webAdminPassword) throws SAXException, IOException{
		this.webAdminPassword=webAdminPassword;
		
		PageContext pc = ThreadLocalPageContext.get();
		//ConfigWebAdmin admin = ConfigWebAdmin.newInstance((ConfigWebImpl)pc.getConfig(),webAdminPassword);
		// TODO verify password
	}*/
	
	
	public static int getSessionCount(){
		PageContext pc = ThreadLocalPageContext.get();
		ScopeContext sc = ((CFMLFactoryImpl)pc.getCFMLFactory()).getScopeContext();
		return sc.getSessionCount(pc);
	}
	
	public static Struct getSessionCollection(String appName){
		PageContext pc = ThreadLocalPageContext.get();
		ScopeContext sc = ((CFMLFactoryImpl)pc.getCFMLFactory()).getScopeContext();
		return sc.getAllSessionScopes(appName);
	}
	
	/*
	public static coldfusion.runtime.SessionScope getSession(java.lang.String,java.lang.String)
	public static coldfusion.runtime.SessionScope getSession(java.lang.String)
	public static coldfusion.runtime.SessionScope getSession(javax.servlet.http.HttpSession,java.lang.String)
	public static coldfusion.runtime.SessionScope getSession(java.lang.String,java.lang.String,java.lang.String)
	public static coldfusion.runtime.SessionScope createSession(java.lang.String,java.lang.String)
	public static coldfusion.runtime.SessionScope createSession(java.lang.String,java.lang.String,java.lang.String)
	public static coldfusion.runtime.SessionScope createSession(javax.servlet.http.HttpSession,java.lang.String)
	public static void cleanUp(java.lang.String,java.lang.String,java.lang.String)
	public static void cleanUp(javax.servlet.http.HttpSession,java.lang.String)
	public static void cleanUp(java.lang.String,java.lang.String)
	public static java.util.Enumeration getSessionKeys()
	public static java.util.Hashtable getMSessionPool()
	public static coldfusion.runtime.AppSessionCollection getSessionCollection(java.lang.String)
	*/


}
