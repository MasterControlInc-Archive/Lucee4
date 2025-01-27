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
package lucee.runtime.listener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import lucee.commons.io.res.Resource;
import lucee.commons.lang.ClassException;
import lucee.commons.lang.StringUtil;
import lucee.commons.lang.types.RefBoolean;
import lucee.runtime.Mapping;
import lucee.runtime.MappingImpl;
import lucee.runtime.PageContext;
import lucee.runtime.PageContextImpl;
import lucee.runtime.PageSource;
import lucee.runtime.config.Config;
import lucee.runtime.config.ConfigImpl;
import lucee.runtime.config.ConfigWeb;
import lucee.runtime.config.ConfigWebImpl;
import lucee.runtime.config.Constants;
import lucee.runtime.db.ApplicationDataSource;
import lucee.runtime.db.DBUtil;
import lucee.runtime.db.DBUtil.DataSourceDefintion;
import lucee.runtime.db.DataSource;
import lucee.runtime.db.DataSourceImpl;
import lucee.runtime.exp.ApplicationException;
import lucee.runtime.exp.PageException;
import lucee.runtime.net.s3.Properties;
import lucee.runtime.net.s3.PropertiesImpl;
import lucee.runtime.op.Caster;
import lucee.runtime.op.Decision;
import lucee.runtime.orm.ORMConfigurationImpl;
import lucee.runtime.type.Array;
import lucee.runtime.type.ArrayImpl;
import lucee.runtime.type.Collection;
import lucee.runtime.type.Collection.Key;
import lucee.runtime.type.KeyImpl;
import lucee.runtime.type.Struct;
import lucee.runtime.type.StructImpl;
import lucee.runtime.type.scope.Scope;
import lucee.runtime.type.scope.Undefined;
import lucee.runtime.type.util.CollectionUtil;
import lucee.runtime.type.util.KeyConstants;
import lucee.runtime.type.util.ListUtil;

public final class AppListenerUtil {

	public static final int TYPE_ALL = 0;
	public static final int TYPE_NEW = 1;
	public static final int TYPE_CLASSIC = 2;
	
	public static final Collection.Key ACCESS_KEY_ID = KeyImpl.intern("accessKeyId");
	public static final Collection.Key AWS_SECRET_KEY = KeyImpl.intern("awsSecretKey");
	public static final Collection.Key DEFAULT_LOCATION = KeyImpl.intern("defaultLocation");
	public static final Collection.Key CONNECTION_STRING = KeyImpl.intern("connectionString");
	
	public static final Collection.Key BLOB = KeyImpl.intern("blob");
	public static final Collection.Key CLOB = KeyImpl.intern("clob");
	public static final Collection.Key CONNECTION_LIMIT = KeyImpl.intern("connectionLimit");
	public static final Collection.Key CONNECTION_TIMEOUT = KeyImpl.intern("connectionTimeout");
	public static final Collection.Key META_CACHE_TIMEOUT = KeyImpl.intern("metaCacheTimeout");
	public static final Collection.Key TIMEZONE = KeyImpl.intern("timezone");
	public static final Collection.Key ALLOW = KeyImpl.intern("allow");
	public static final Collection.Key STORAGE = KeyImpl.intern("storage");
	public static final Collection.Key READ_ONLY = KeyImpl.intern("readOnly");
	public static final Collection.Key VALIDATE = KeyImpl.intern("validate");
	public static final Collection.Key DATABASE = KeyConstants._database;
	
	public static PageSource getApplicationPageSource(PageContext pc,PageSource requestedPage, int mode,int type, RefBoolean isCFC) {
		if(mode==ApplicationListener.MODE_CURRENT2ROOT)
			return getApplicationPageSourceCurrToRoot(pc, requestedPage,type, isCFC);
		if(mode==ApplicationListener.MODE_CURRENT_OR_ROOT)
			return getApplicationPageSourceCurrOrRoot(pc, requestedPage,type, isCFC);
		if(mode==ApplicationListener.MODE_CURRENT)
			return getApplicationPageSourceCurrent(requestedPage,type, isCFC);
		return getApplicationPageSourceRoot(pc,type, isCFC);
	}

	public static PageSource getApplicationPageSourceCurrent(PageSource requestedPage, int type, RefBoolean isCFC) {
		if(type!=TYPE_CLASSIC) {
			PageSource res=requestedPage.getRealPage(Constants.APP_CFC);
		    if(res.exists()) {
		    	if(isCFC!=null)isCFC.setValue(true);
		    	return res;
		    }
		}
		if(type!=TYPE_NEW) {
			PageSource res=requestedPage.getRealPage(Constants.APP_CFM);
			if(res.exists()) return res;
		}
		
		return null;
	}
	
	public static PageSource getApplicationPageSourceRoot(PageContext pc, int type, RefBoolean isCFC) {
		if(type!=TYPE_CLASSIC) {
			PageSource ps = ((PageContextImpl)pc).getPageSourceExisting("/"+Constants.APP_CFC);
			if(ps!=null) {
				if(isCFC!=null)isCFC.setValue(true);
		    	return ps;
			}
		}
		
		if(type!=TYPE_NEW) {
			PageSource ps = ((PageContextImpl)pc).getPageSourceExisting("/"+Constants.APP_CFM);
			if(ps!=null) return ps;
		}
		return null;
	}
	

	public static PageSource getApplicationPageSourceCurrToRoot(PageContext pc,PageSource requestedPage, int type, RefBoolean isCFC) {
	    
		PageSource res = getApplicationPageSourceCurrent(requestedPage, type, isCFC);
		if(res!=null) return res;
		
	    Array arr=lucee.runtime.type.util.ListUtil.listToArrayRemoveEmpty(requestedPage.getFullRealpath(),"/");
		//Config config = pc.getConfig();
		String path;
		for(int i=arr.size()-1;i>0;i--) {
		    StringBuilder sb=new StringBuilder("/");
			for(int y=1;y<i;y++) {
			    sb.append((String)arr.get(y,""));
			    sb.append('/');
			}
			path=sb.toString();
			if(type!=TYPE_CLASSIC) {
				res = ((PageContextImpl)pc).getPageSourceExisting(path.concat(Constants.APP_CFC));
				if(res!=null) {
					if(isCFC!=null)isCFC.setValue(true);
					return res;
				}
			}
			if(type!=TYPE_NEW) {
				res = ((PageContextImpl)pc).getPageSourceExisting(path.concat(Constants.APP_CFM));
				if(res!=null) return res;
			}
			
		}
		return null;
	}
	
	public static PageSource getApplicationPageSourceCurrOrRoot(PageContext pc,PageSource requestedPage, int type,RefBoolean isCFC) {
	    // current 
		PageSource res = getApplicationPageSourceCurrent(requestedPage, type, isCFC);
		if(res!=null) return res;
		
	    // root
	    return getApplicationPageSourceRoot(pc,type, isCFC);
	}
	
	public static String toStringMode(int mode) {
		if(mode==ApplicationListener.MODE_CURRENT)	return "curr";
		if(mode==ApplicationListener.MODE_ROOT)		return "root";
		if(mode==ApplicationListener.MODE_CURRENT2ROOT)		return "curr2root";
		if(mode==ApplicationListener.MODE_CURRENT_OR_ROOT)		return "currorroot";
		return "curr2root";
	}

	public static String toStringType(ApplicationListener listener) {
		if(listener instanceof NoneAppListener)			return "none";
		else if(listener instanceof MixedAppListener)	return "mixed";
		else if(listener instanceof ClassicAppListener)	return "classic";
		else if(listener instanceof ModernAppListener)	return "modern";
		return "";
	}
	
	public static DataSource[] toDataSources(Object o,DataSource[] defaultValue) {
		try {
			return toDataSources(o);
		} catch (Throwable t) {
			return defaultValue;
		}
	}

	public static DataSource[] toDataSources(Object o) throws PageException, ClassException {
		Struct sct = Caster.toStruct(o);
		Iterator<Entry<Key, Object>> it = sct.entryIterator();
		Entry<Key, Object> e;
		java.util.List<DataSource> dataSources=new ArrayList<DataSource>();
		while(it.hasNext()) {
			e = it.next();
			dataSources.add(toDataSource(e.getKey().getString().trim(), Caster.toStruct(e.getValue())));
		}
		return dataSources.toArray(new DataSource[dataSources.size()]);
	}

	public static DataSource toDataSource(String name,Struct data) throws PageException, ClassException {
			String user = Caster.toString(data.get(KeyConstants._username,null),null);
			String pass = Caster.toString(data.get(KeyConstants._password,""),"");
			if(StringUtil.isEmpty(user)) {
				user=null;
				pass=null;
			}
			else {
				user=user.trim();
				pass=pass.trim();
			}
			
			// first check for {class:... , connectionString:...}
			Object oConnStr=data.get(CONNECTION_STRING,null);
			if(oConnStr!=null)
				return ApplicationDataSource.getInstance(
					name, 
					Caster.toString(data.get(KeyConstants._class)), 
					Caster.toString(oConnStr), 
					user, pass,
					Caster.toBooleanValue(data.get(BLOB,null),false),
					Caster.toBooleanValue(data.get(CLOB,null),false), 
					Caster.toIntValue(data.get(CONNECTION_LIMIT,null),-1), 
					Caster.toIntValue(data.get(CONNECTION_TIMEOUT,null),1), 
					Caster.toLongValue(data.get(META_CACHE_TIMEOUT,null),60000L), 
					Caster.toTimeZone(data.get(TIMEZONE,null),null), 
					Caster.toIntValue(data.get(ALLOW,null),DataSource.ALLOW_ALL),
					Caster.toBooleanValue(data.get(STORAGE,null),false),
					Caster.toBooleanValue(data.get(READ_ONLY,null),false),
					Caster.toBooleanValue(data.get(VALIDATE,null),false));
			
			// then for {type:... , host:... , ...}
			String type=Caster.toString(data.get(KeyConstants._type));
			DataSourceDefintion dbt = DBUtil.getDataSourceDefintionForType(type, null);
			if(dbt==null) throw new ApplicationException("no datasource type ["+type+"] found");
			DataSourceImpl ds = new DataSourceImpl(
					name, 
					dbt.className, 
					Caster.toString(data.get(KeyConstants._host)), 
					dbt.connectionString,
					Caster.toString(data.get(DATABASE)), 
					Caster.toIntValue(data.get(KeyConstants._port,null),-1), 
					user,pass, 
					Caster.toIntValue(data.get(CONNECTION_LIMIT,null),-1), 
					Caster.toIntValue(data.get(CONNECTION_TIMEOUT,null),1), 
					Caster.toLongValue(data.get(META_CACHE_TIMEOUT,null),60000L), 
					Caster.toBooleanValue(data.get(BLOB,null),false), 
					Caster.toBooleanValue(data.get(CLOB,null),false), 
					DataSource.ALLOW_ALL, 
					Caster.toStruct(data.get(KeyConstants._custom,null),null,false), 
					Caster.toBooleanValue(data.get(READ_ONLY,null),false), 
					true, 
					Caster.toBooleanValue(data.get(STORAGE,null),false), 
					Caster.toTimeZone(data.get(TIMEZONE,null),null),
					""
			);

			return ds;
		
	}

	public static Mapping[] toMappings(ConfigWeb cw,Object o,Mapping[] defaultValue, Resource source) { 
		try {
			return toMappings(cw, o,source);
		} catch (Throwable t) {
			return defaultValue;
		}
	}

	public static Mapping[] toMappings(ConfigWeb cw,Object o, Resource source) throws PageException {
		Struct sct = Caster.toStruct(o);
		Iterator<Entry<Key, Object>> it = sct.entryIterator();
		Entry<Key, Object> e;
		java.util.List<Mapping> mappings=new ArrayList<Mapping>();
		ConfigWebImpl config=(ConfigWebImpl) cw;
		String virtual;
		while(it.hasNext()) {
			e = it.next();
			virtual=translateMappingVirtual(e.getKey().getString());
			MappingData md=toMappingData(e.getValue(),source);
			mappings.add(config.getApplicationMapping("application",virtual,md.physical,md.archive,md.physicalFirst,false));
		}
		return mappings.toArray(new Mapping[mappings.size()]);
	}
	

	private static MappingData toMappingData(Object value, Resource source) throws PageException {
		MappingData md=new MappingData();
		
		if(Decision.isStruct(value)) {
			Struct map=Caster.toStruct(value);
			
			
			// physical
			String physical=Caster.toString(map.get("physical",null),null);
			if(!StringUtil.isEmpty(physical,true)) 
				md.physical=translateMappingPhysical(physical.trim(),source);

			// archive
			String archive = Caster.toString(map.get("archive",null),null);
			if(!StringUtil.isEmpty(archive,true)) 
				md.archive=translateMappingPhysical(archive.trim(),source);
			
			if(archive==null && physical==null) 
				throw new ApplicationException("you must define archive or/and physical!");
			
			// primary
			md.physicalFirst=true;
			// primary is only of interest when both values exists
			if(archive!=null && physical!=null) {
				String primary = Caster.toString(map.get("primary",null),null);
				if(primary!=null && primary.trim().equalsIgnoreCase("archive")) md.physicalFirst=false;
			}
			// only a archive
			else if(archive!=null) md.physicalFirst=false;
		}
		// simple value == only a physical path
		else {
			md.physical=translateMappingPhysical(Caster.toString(value).trim(),source);
			md.physicalFirst=true;
		}
		
		return md;
	}

	private static String translateMappingPhysical(String path, Resource source) {
		if(source==null) return path;
		source=source.getParentResource().getRealResource(path);
		if(source.exists()) return source.getAbsolutePath();
		return path;
	}

	private static String translateMappingVirtual(String virtual) {
		virtual=virtual.replace('\\', '/');
		if(!StringUtil.startsWith(virtual,'/'))virtual="/".concat(virtual);
		return virtual;
	}
	
	public static Mapping[] toCustomTagMappings(ConfigWeb cw, Object o, Resource source) throws PageException {
		return toMappings(cw,"custom", o,false,source);
	}

	public static Mapping[] toCustomTagMappings(ConfigWeb cw, Object o, Resource source, Mapping[] defaultValue) {
		try {
			return toMappings(cw,"custom", o,false,source);
		} catch (Throwable t) {
			return defaultValue;
		}
	}

	public static Mapping[] toComponentMappings(ConfigWeb cw, Object o, Resource source) throws PageException {
		return toMappings(cw,"component", o,true,source);
	}

	public static Mapping[] toComponentMappings(ConfigWeb cw, Object o, Resource source,Mapping[] defaultValue) {
		
		try {
			return toMappings(cw,"component", o,true,source);
		} catch (Throwable t) {
			return defaultValue;
		}
	}

	private static Mapping[] toMappings(ConfigWeb cw,String type, Object o, boolean useStructNames, Resource source) throws PageException {
		ConfigWebImpl config=(ConfigWebImpl) cw;
		Array array;
		if(o instanceof String){
			array=ListUtil.listToArrayRemoveEmpty(Caster.toString(o),',');
		}
		else if(o instanceof Struct){
			Struct sct=(Struct) o;
			if(useStructNames) {
				Iterator<Entry<Key, Object>> it = sct.entryIterator();
				List<Mapping> list=new ArrayList<Mapping>();
				Entry<Key, Object> e;
				String virtual;
				while(it.hasNext()) {
					e = it.next();
					virtual=e.getKey().getString();
					if(virtual.length()==0) virtual="/";
					if(!virtual.startsWith("/")) virtual="/"+virtual;
			        if(!virtual.equals("/") && virtual.endsWith("/"))virtual=virtual.substring(0,virtual.length()-1);
			        MappingData md=toMappingData(e.getValue(),source);
					list.add(config.getApplicationMapping(type,virtual,md.physical,md.archive,md.physicalFirst,true));
				}
				return list.toArray(new Mapping[list.size()]);
			}
			
			array=new ArrayImpl();
			Iterator<Object> it = sct.valueIterator();
			while(it.hasNext()) {
				array.append(it.next());
			}
		}
		else {
			array=Caster.toArray(o);
		}
		MappingImpl[] mappings=new MappingImpl[array.size()];
		for(int i=0;i<mappings.length;i++) {
			
			MappingData md=toMappingData(array.getE(i+1),source);
			mappings[i]=(MappingImpl) config.getApplicationMapping(type,"/"+i,md.physical,md.archive,md.physicalFirst,true);
		}
		return mappings;
	}


	public static String toLocalMode(int mode, String defaultValue) {
		if(Undefined.MODE_LOCAL_OR_ARGUMENTS_ALWAYS==mode) return "modern";
		if(Undefined.MODE_LOCAL_OR_ARGUMENTS_ONLY_WHEN_EXISTS==mode)return "classic";
		return defaultValue;
	}
	
	public static int toLocalMode(Object oMode, int defaultValue) {
		if(oMode==null) return defaultValue;
		
		if(Decision.isBoolean(oMode)) {
			if(Caster.toBooleanValue(oMode, false))
				return Undefined.MODE_LOCAL_OR_ARGUMENTS_ALWAYS;
			return Undefined.MODE_LOCAL_OR_ARGUMENTS_ONLY_WHEN_EXISTS;
		}
		String strMode=Caster.toString(oMode,null);
		if("always".equalsIgnoreCase(strMode) || "modern".equalsIgnoreCase(strMode)) 
			return Undefined.MODE_LOCAL_OR_ARGUMENTS_ALWAYS;
		if("update".equalsIgnoreCase(strMode) || "classic".equalsIgnoreCase(strMode)) 
			return Undefined.MODE_LOCAL_OR_ARGUMENTS_ONLY_WHEN_EXISTS;
		return defaultValue;
	}
	
	public static int toLocalMode(String strMode) throws ApplicationException {
		int lm = toLocalMode(strMode, -1);
		if(lm!=-1) return lm;
		throw new ApplicationException("invalid localMode definition ["+strMode+"] for tag "+Constants.CFAPP_NAME+"/"+Constants.APP_CFC+", valid values are [classic,modern,true,false]");
	}

	public static short toSessionType(String str, short defaultValue) {
		if(!StringUtil.isEmpty(str,true)){
			str=str.trim().toLowerCase();
			if("cfml".equals(str)) return Config.SESSION_TYPE_CFML;
			if("j2ee".equals(str)) return Config.SESSION_TYPE_J2EE;
			if("cfm".equals(str)) return Config.SESSION_TYPE_CFML;
			if("jee".equals(str)) return Config.SESSION_TYPE_J2EE;
			if("j".equals(str)) return Config.SESSION_TYPE_J2EE;
			if("c".equals(str)) return Config.SESSION_TYPE_J2EE;
		}
		return defaultValue;
	}

	public static short toSessionType(String str) throws ApplicationException {
		if(!StringUtil.isEmpty(str,true)){
			str=str.trim().toLowerCase();
			if("cfml".equals(str)) return Config.SESSION_TYPE_CFML;
			if("j2ee".equals(str)) return Config.SESSION_TYPE_J2EE;
			if("cfm".equals(str)) return Config.SESSION_TYPE_CFML;
			if("jee".equals(str)) return Config.SESSION_TYPE_J2EE;
			if("j".equals(str)) return Config.SESSION_TYPE_J2EE;
			if("c".equals(str)) return Config.SESSION_TYPE_J2EE;
		}
		throw new ApplicationException("invalid sessionType definition ["+str+"] for tag "+Constants.CFAPP_NAME+"/"+Constants.APP_CFC+", valid values are [cfml,j2ee]");
	}
	
	public static Properties toS3(Struct sct) {
		String host=Caster.toString(sct.get(KeyConstants._host,null),null);
		if(StringUtil.isEmpty(host))host=Caster.toString(sct.get(KeyConstants._server,null),null);
		
		return toS3(
				Caster.toString(sct.get(ACCESS_KEY_ID,null),null),
				Caster.toString(sct.get(AWS_SECRET_KEY,null),null),
				Caster.toString(sct.get(DEFAULT_LOCATION,null),null),
				host
			);
	}

	public static Properties toS3(String accessKeyId, String awsSecretKey, String defaultLocation, String host) {
		PropertiesImpl s3 = new PropertiesImpl();
		if(!StringUtil.isEmpty(accessKeyId))s3.setAccessKeyId(accessKeyId);
		if(!StringUtil.isEmpty(awsSecretKey))s3.setSecretAccessKey(awsSecretKey);
		return s3;
	}

	public static void setORMConfiguration(PageContext pc, ApplicationContext ac,Struct sct) throws PageException {
		if(sct==null)sct=new StructImpl();
		Resource res=pc.getCurrentTemplatePageSource().getResourceTranslated(pc).getParentResource();
		ConfigImpl config=(ConfigImpl) pc.getConfig();
		ac.setORMConfiguration(ORMConfigurationImpl.load(config,ac,sct,res,config.getORMConfig()));
		
		// datasource
		Object o = sct.get(KeyConstants._datasource,null);
		
		if(o!=null) {
			o=toDefaultDatasource(o);
			if(o!=null) ((ApplicationContextPro)ac).setORMDataSource(o);
		}
	}
	
	
	/**
	 * translate int definition of script protect to string definition
	 * @param scriptProtect
	 * @return
	 */
	public static String translateScriptProtect(int scriptProtect) {
		if(scriptProtect==ApplicationContext.SCRIPT_PROTECT_NONE) return "none";
		if(scriptProtect==ApplicationContext.SCRIPT_PROTECT_ALL) return "all";
		
		Array arr=new ArrayImpl();
		if((scriptProtect&ApplicationContext.SCRIPT_PROTECT_CGI)>0) arr.appendEL("cgi");
		if((scriptProtect&ApplicationContext.SCRIPT_PROTECT_COOKIE)>0) arr.appendEL("cookie");
		if((scriptProtect&ApplicationContext.SCRIPT_PROTECT_FORM)>0) arr.appendEL("form");
		if((scriptProtect&ApplicationContext.SCRIPT_PROTECT_URL)>0) arr.appendEL("url");
		
		
		
		try {
			return ListUtil.arrayToList(arr, ",");
		} catch (PageException e) {
			return "none";
		} 
	}
	

	/**
	 * translate string definition of script protect to int definition
	 * @param strScriptProtect
	 * @return
	 */
	public static int translateScriptProtect(String strScriptProtect) {
		strScriptProtect=strScriptProtect.toLowerCase().trim();
		
		if("none".equals(strScriptProtect)) return ApplicationContext.SCRIPT_PROTECT_NONE;
		if("no".equals(strScriptProtect)) return ApplicationContext.SCRIPT_PROTECT_NONE;
		if("false".equals(strScriptProtect)) return ApplicationContext.SCRIPT_PROTECT_NONE;
		
		if("all".equals(strScriptProtect)) return ApplicationContext.SCRIPT_PROTECT_ALL;
		if("true".equals(strScriptProtect)) return ApplicationContext.SCRIPT_PROTECT_ALL;
		if("yes".equals(strScriptProtect)) return ApplicationContext.SCRIPT_PROTECT_ALL;
		
		String[] arr = ListUtil.listToStringArray(strScriptProtect, ',');
		String item;
		int scriptProtect=0;
		for(int i=0;i<arr.length;i++) {
			item=arr[i].trim();
			if("cgi".equals(item) && (scriptProtect&ApplicationContext.SCRIPT_PROTECT_CGI)==0)
				scriptProtect+=ApplicationContext.SCRIPT_PROTECT_CGI;
			else if("cookie".equals(item) && (scriptProtect&ApplicationContext.SCRIPT_PROTECT_COOKIE)==0)
				scriptProtect+=ApplicationContext.SCRIPT_PROTECT_COOKIE;
			else if("form".equals(item) && (scriptProtect&ApplicationContext.SCRIPT_PROTECT_FORM)==0)
				scriptProtect+=ApplicationContext.SCRIPT_PROTECT_FORM;
			else if("url".equals(item) && (scriptProtect&ApplicationContext.SCRIPT_PROTECT_URL)==0)
				scriptProtect+=ApplicationContext.SCRIPT_PROTECT_URL;
		}
		return scriptProtect;
	}
	

	public static String translateLoginStorage(int loginStorage) {
		if(loginStorage==Scope.SCOPE_SESSION) return "session";
		return "cookie";
	}
	

	public static int translateLoginStorage(String strLoginStorage, int defaultValue) {
		strLoginStorage=strLoginStorage.toLowerCase().trim();
	    if(strLoginStorage.equals("session"))return Scope.SCOPE_SESSION;
	    if(strLoginStorage.equals("cookie"))return Scope.SCOPE_COOKIE;
	    return defaultValue;
	}
	

	public static int translateLoginStorage(String strLoginStorage) throws ApplicationException {
		int ls=translateLoginStorage(strLoginStorage, -1);
		if(ls!=-1) return ls;
	    throw new ApplicationException("invalid loginStorage definition ["+strLoginStorage+"], valid values are [session,cookie]");
	}
	
	public static Object toDefaultDatasource(Object o) throws PageException {
		if(Decision.isStruct(o)) {
			Struct sct=(Struct) o;
			
			// fix for Jira ticket LUCEE-1931
			if(sct.size()==1) {
				Key[] keys = CollectionUtil.keys(sct);
				if(keys.length==1 && keys[0].equalsIgnoreCase(KeyConstants._name)) {
					return Caster.toString(sct.get(KeyConstants._name));
				}
			}
			
			try {
				return AppListenerUtil.toDataSource("__default__",sct);
			} 
			catch (PageException pe) { 
				// again try fix for Jira ticket LUCEE-1931
				String name= Caster.toString(sct.get(KeyConstants._name,null),null);
				if(!StringUtil.isEmpty(name)) return name;
				throw pe;
			}
			catch (ClassException e) {
				throw Caster.toPageException(e);
			}
		}
		return Caster.toString(o);
	}

	public static String toWSType(short wstype, String defaultValue) {
		if(ApplicationContextPro.WS_TYPE_AXIS1== wstype) return "Axis1";
		if(ApplicationContextPro.WS_TYPE_JAX_WS== wstype) return "JAX-WS";
		if(ApplicationContextPro.WS_TYPE_CXF== wstype) return "CXF";
		return defaultValue;
	}
	
	public static short toWSType(String wstype, short defaultValue) {
		if(wstype==null) return defaultValue;
		wstype=wstype.trim();
		
		if("axis".equalsIgnoreCase(wstype) || "axis1".equalsIgnoreCase(wstype))
			return ApplicationContextPro.WS_TYPE_AXIS1;
		/*if("jax".equalsIgnoreCase(wstype) || "jaxws".equalsIgnoreCase(wstype) || "jax-ws".equalsIgnoreCase(wstype))
			return ApplicationContextPro.WS_TYPE_JAX_WS;
		if("cxf".equalsIgnoreCase(wstype))
			return ApplicationContextPro.WS_TYPE_CXF;*/
		return defaultValue;
	}
	
	public static short toWSType(String wstype) throws ApplicationException {
		String str="";
		KeyImpl cs=new KeyImpl(str){
			
			public String getString() {
				return null;
			}
			
		};
		
		
		
		short wst = toWSType(wstype,(short)-1);
		if(wst!=-1) return wst;
		throw new ApplicationException("invalid webservice type ["+wstype+"], valid values are [axis1]");
		//throw new ApplicationException("invalid webservice type ["+wstype+"], valid values are [axis1,jax-ws,cxf]");
	}
	static private class MappingData {
		private String physical;
		private String archive;
		private boolean physicalFirst;
	}
}


