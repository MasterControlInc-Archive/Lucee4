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
package lucee.runtime.orm.hibernate;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lucee.commons.io.log.Log;
import lucee.commons.io.log.LogUtil;
import lucee.commons.io.res.Resource;
import lucee.commons.io.res.filter.ExtensionResourceFilter;
import lucee.commons.io.res.util.ResourceUtil;
import lucee.commons.lang.StringUtil;
import lucee.commons.sql.SQLUtil;
import lucee.loader.util.Util;
import lucee.runtime.Component;
import lucee.runtime.InterfacePage;
import lucee.runtime.Mapping;
import lucee.runtime.Page;
import lucee.runtime.PageContext;
import lucee.runtime.PageSource;
import lucee.runtime.component.ComponentLoader;
import lucee.runtime.config.Config;
import lucee.runtime.config.Constants;
import lucee.runtime.db.DataSource;
import lucee.runtime.db.DatasourceConnection;
import lucee.runtime.exp.PageException;
import lucee.runtime.listener.ApplicationContext;
import lucee.runtime.orm.ORMConfiguration;
import lucee.runtime.reflection.Reflector;
import lucee.runtime.type.Collection.Key;

import org.luceehibernate.MappingException;
import org.luceehibernate.cache.RegionFactory;
import org.luceehibernate.cfg.Configuration;
import org.luceehibernate.tool.hbm2ddl.SchemaExport;
import org.luceehibernate.tool.hbm2ddl.SchemaUpdate;
import org.w3c.dom.Document;


public class HibernateSessionFactory {


	public static final String HIBERNATE_3_PUBLIC_ID = "-//Hibernate/Hibernate Mapping DTD 3.0//EN";
	public static final String HIBERNATE_3_SYSTEM_ID = "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd";
	public static final Charset HIBERNATE_3_CHARSET = CommonUtil.UTF8;
	public static final String HIBERNATE_3_DOCTYPE_DEFINITION = "<!DOCTYPE hibernate-mapping PUBLIC \""+HIBERNATE_3_PUBLIC_ID+"\" \""+HIBERNATE_3_SYSTEM_ID+"\">";


	public static Configuration createConfiguration(Log log,String mappings, DatasourceConnection dc, SessionFactoryData data) throws SQLException, IOException, PageException {
		/*
		 autogenmap
		 cacheconfig
		 cacheprovider
		 cfclocation
		 datasource
		 dbcreate
		 eventHandling
		 flushatrequestend
		 ormconfig
		 sqlscript
		 useDBForMapping
		 */

		ORMConfiguration ormConf = data.getORMConfiguration();

		// dialect
		DataSource ds = dc.getDatasource();
		String dialect=null;
		try	{
			if (Class.forName(ormConf.getDialect()) != null) {
				dialect = ormConf.getDialect();
			}
		}
		catch (Exception e) {
			// MZ: The dialect value could not be bound to a classname or instantiation causes an exception - ignore and use the default dialect entries
		}
		if (dialect == null) {
			dialect = Dialect.getDialect(ormConf.getDialect());
			if(Util.isEmpty(dialect)) dialect=Dialect.getDialect(ds);
		}
		if(Util.isEmpty(dialect))
			throw ExceptionUtil.createException(data,null,"A valid dialect definition inside the "+Constants.APP_CFC+"/"+Constants.CFAPP_NAME+" is missing. The dialect cannot be determinated automatically",null);

		// Cache Provider
		String cacheProvider = ormConf.getCacheProvider();
		Class<? extends RegionFactory> regionFactory=null;

        // We don't use EHCache at MasterControl and isn't compatible with luceehibernate change so commented out.
		/*if(Util.isEmpty(cacheProvider) || "EHCache".equalsIgnoreCase(cacheProvider)) {
			regionFactory=net.sf.ehcache.hibernate.EhCacheRegionFactory.class;
			cacheProvider=regionFactory.getName();//"org.hibernate.cache.EhCacheProvider";
		}
		else*/
		if("JBossCache".equalsIgnoreCase(cacheProvider)) 	cacheProvider="org.hibernate.cache.TreeCacheProvider";
		else if("HashTable".equalsIgnoreCase(cacheProvider)) 	cacheProvider="org.hibernate.cache.HashtableCacheProvider";
		else if("SwarmCache".equalsIgnoreCase(cacheProvider)) 	cacheProvider="org.hibernate.cache.SwarmCacheProvider";
		else if("OSCache".equalsIgnoreCase(cacheProvider)) 		cacheProvider="org.hibernate.cache.OSCacheProvider";

		Resource cacheConfig = ormConf.getCacheConfig();
		Configuration configuration = new Configuration();

		// ormConfig
		Resource conf = ormConf.getOrmConfig();
		if(conf!=null){
			try {
				Document doc = CommonUtil.toDocument(conf,null);
				configuration.configure(doc);
			}
			catch (Throwable t) {
				LogUtil.log(log, Log.LEVEL_ERROR, "hibernate", t);

			}
		}

		try{
			configuration.addXML(mappings);
		}
		catch(MappingException me){
			throw ExceptionUtil.createException(data,null, me);
		}

		configuration

        // Database connection settings
        .setProperty("hibernate.connection.driver_class", ds.getClazz().getName())
    	.setProperty("hibernate.connection.url", ds.getDsnTranslated());
		if(!StringUtil.isEmpty(ds.getUsername())) {
			configuration.setProperty("hibernate.connection.username", ds.getUsername());
			if(!StringUtil.isEmpty(ds.getPassword()))
				configuration.setProperty("hibernate.connection.password", ds.getPassword());
		}
    	//.setProperty("hibernate.connection.release_mode", "after_transaction")
    	configuration.setProperty("hibernate.transaction.flush_before_completion", "false")
    	.setProperty("hibernate.transaction.auto_close_session", "false")

    	// JDBC connection pool (use the built-in)
    	//.setProperty("hibernate.connection.pool_size", "2")//MUST


    	// SQL dialect
    	.setProperty("hibernate.dialect", dialect)
    	// Enable Hibernate's current session context
    	.setProperty("hibernate.current_session_context_class", "thread")

    	// Echo all executed SQL to stdout
    	.setProperty("hibernate.show_sql", CommonUtil.toString(ormConf.logSQL()))
    	.setProperty("hibernate.format_sql", CommonUtil.toString(ormConf.logSQL()))
    	// Specifies whether secondary caching should be enabled
    	.setProperty("hibernate.cache.use_second_level_cache", CommonUtil.toString(ormConf.secondaryCacheEnabled()))
		// Drop and re-create the database schema on startup
    	.setProperty("hibernate.exposeTransactionAwareSessionFactory", "false")
		//.setProperty("hibernate.hbm2ddl.auto", "create")
		.setProperty("hibernate.default_entity_mode", "dynamic-map");

		if(!Util.isEmpty(ormConf.getCatalog()))
			configuration.setProperty("hibernate.default_catalog", ormConf.getCatalog());
		if(!Util.isEmpty(ormConf.getSchema()))
			configuration.setProperty("hibernate.default_schema",ormConf.getSchema());


		if(ormConf.secondaryCacheEnabled()){
			if(cacheConfig!=null && cacheConfig.isFile())
				configuration.setProperty("hibernate.cache.provider_configuration_file_resource_path",cacheConfig.getAbsolutePath());
			if(regionFactory!=null || Reflector.isInstaneOf(cacheProvider, RegionFactory.class))
				configuration.setProperty("hibernate.cache.region.factory_class", cacheProvider);
			else
				configuration.setProperty("hibernate.cache.provider_class", cacheProvider);

			configuration.setProperty("hibernate.cache.use_query_cache", "true");

	    	//hibernate.cache.provider_class=org.hibernate.cache.EhCacheProvider
		}

		/*
		<!ELEMENT tuplizer EMPTY>
	    <!ATTLIST tuplizer entity-mode (pojo|dom4j|dynamic-map) #IMPLIED>   <!-- entity mode for which tuplizer is in effect -->
	    <!ATTLIST tuplizer class CDATA #REQUIRED>                           <!-- the tuplizer class to use -->
		*/

		schemaExport(log,configuration,dc,data);

		return configuration;
	}

	private static void schemaExport(Log log,Configuration configuration, DatasourceConnection dc, SessionFactoryData data) throws PageException, SQLException, IOException {
		ORMConfiguration ormConf = data.getORMConfiguration();

		if(ORMConfiguration.DBCREATE_NONE==ormConf.getDbCreate()) {
			return;
		}
		else if(ORMConfiguration.DBCREATE_DROP_CREATE==ormConf.getDbCreate()) {
			SchemaExport export = new SchemaExport(configuration);
			export.setHaltOnError(true);

			export.execute(false,true,false,false);
            printError(log,data,export.getExceptions(),false);
            executeSQLScript(ormConf,dc);
		}
		else if(ORMConfiguration.DBCREATE_UPDATE==ormConf.getDbCreate()) {
			SchemaUpdate update = new SchemaUpdate(configuration);
            update.setHaltOnError(true);
            update.execute(false, true);
            printError(log,data,update.getExceptions(),false);
        }
	}

	private static void printError(Log log,SessionFactoryData data, List<Exception> exceptions,boolean throwException) throws PageException {
		if(exceptions==null || exceptions.size()==0) return;
		Iterator<Exception> it = exceptions.iterator();
        if(!throwException || exceptions.size()>1){
			while(it.hasNext()) {
				LogUtil.log(log, Log.LEVEL_ERROR, "hibernate", it.next());
	        }
        }
        if(!throwException) return;

        it = exceptions.iterator();
        while(it.hasNext()) {
        	throw ExceptionUtil.createException(data,null,it.next());
        }
	}

	private static void executeSQLScript(ORMConfiguration ormConf,DatasourceConnection dc) throws SQLException, IOException {
        Resource sqlScript = ormConf.getSqlScript();
        if(sqlScript!=null && sqlScript.isFile()) {
        	BufferedReader br = CommonUtil.toBufferedReader(sqlScript,(Charset)null);
        	String line;
            StringBuilder sql=new StringBuilder();
            String str;
            Statement stat = dc.getConnection().createStatement();
        	try{
	        	while((line=br.readLine())!=null){
	            	line=line.trim();
	            	if(line.startsWith("//") || line.startsWith("--")) continue;
	            	if(line.endsWith(";")){
	            		sql.append(line.substring(0,line.length()-1));
	            		str=sql.toString().trim();
	            		if(str.length()>0)stat.execute(str);
	            		sql=new StringBuilder();
	            	}
	            	else {
	            		sql.append(line).append(" ");
	            	}
	            }
	        	str=sql.toString().trim();
        		if(str.length()>0){
        			stat.execute(str);
	            }
        	}
    		finally {
    			SQLUtil.closeEL(stat);
    		}
        }
    }


	public static Map<Key,String> createMappings(ORMConfiguration ormConf, SessionFactoryData data) {
		Map<Key,String> mappings=new HashMap<Key,String>();
		Iterator<Entry<Key, Map<String, CFCInfo>>> it = data.getCFCs().entrySet().iterator();
		while(it.hasNext()){
			Entry<Key, Map<String, CFCInfo>> e = it.next();

			Set<String> done=new HashSet<String>();
			StringBuilder mapping=new StringBuilder();
			mapping.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			mapping.append(HIBERNATE_3_DOCTYPE_DEFINITION+"\n");
			mapping.append("<hibernate-mapping>\n");
			Iterator<Entry<String, CFCInfo>> _it = e.getValue().entrySet().iterator();
			Entry<String, CFCInfo> entry;
			while(_it.hasNext()){
				entry = _it.next();
				createMappings(ormConf,entry.getKey(),entry.getValue(),done,mapping,data);

			}
			mapping.append("</hibernate-mapping>");
			mappings.put(e.getKey(), mapping.toString());
		}
		return mappings;
	}

	private static void createMappings(ORMConfiguration ormConf, String key, CFCInfo value,Set<String> done,StringBuilder mappings, SessionFactoryData data) {
		if(done.contains(key)) return;
		CFCInfo v;
		String ext = value.getCFC().getExtends();
		if(!Util.isEmpty(ext)){
			try {
				Component base = data.getEntityByCFCName(ext, false);
				ext=HibernateCaster.getEntityName(base);
			} catch (Throwable t) {}


			ext=HibernateUtil.id(CommonUtil.last(ext, '.').trim());
			if(!done.contains(ext)) {
				v = data.getCFC(ext,null);
				if(v!=null)createMappings(ormConf, ext, v, done, mappings,data);
			}
		}

		mappings.append(value.getXML());
		done.add(key);
	}

	public static List<Component> loadComponents(PageContext pc,HibernateORMEngine engine, ORMConfiguration ormConf) throws PageException {
		ExtensionResourceFilter filter = new ExtensionResourceFilter(pc.getConfig().getCFCExtension(),true);
		List<Component> components=new ArrayList<Component>();
		loadComponents(pc,engine,components,ormConf.getCfcLocations(),filter,ormConf);
		return components;
	}

	private static void loadComponents(PageContext pc, HibernateORMEngine engine,List<Component> components,Resource[] reses,ExtensionResourceFilter filter,ORMConfiguration ormConf) throws PageException {
		Mapping[] mappings = createMappings(pc, reses);
		ApplicationContext ac=pc.getApplicationContext();
		Mapping[] existing = ac.getComponentMappings();
		if(existing==null) existing=new Mapping[0];
		try{
			Mapping[] tmp = new Mapping[existing.length+1];
			for(int i=1;i<tmp.length;i++){
				tmp[i]=existing[i-1];
			}
			ac.setComponentMappings(tmp);
			for(int i=0;i<reses.length;i++){
				if(reses[i]!=null && reses[i].isDirectory()){
					tmp[0] = mappings[i];
					ac.setComponentMappings(tmp);
					loadComponents(pc,engine,mappings[i],components,reses[i], filter,ormConf);
				}
			}
		}
		finally {
			ac.setComponentMappings(existing);
		}
	}

	private static void loadComponents(PageContext pc, HibernateORMEngine engine,Mapping cfclocation,List<Component> components,Resource res,ExtensionResourceFilter filter,ORMConfiguration ormConf) throws PageException {
		if(res==null) return;

		if(res.isDirectory()){
			Resource[] children = res.listResources(filter);

			// first load all files
			for(int i=0;i<children.length;i++){
				if(children[i].isFile())loadComponents(pc,engine,cfclocation,components,children[i], filter,ormConf);
			}

			// and then invoke subfiles
			for(int i=0;i<children.length;i++){
				if(children[i].isDirectory())loadComponents(pc,engine,cfclocation,components,children[i], filter,ormConf);
			}
		}
		else if(res.isFile()){
			if(!res.getName().equalsIgnoreCase(Constants.APP_CFC))	{
				try {

					// MUST still a bad solution
					PageSource ps = pc.toPageSource(res,null);
					if(ps==null || ps.getComponentName().indexOf("..")!=-1) {
						PageSource ps2=null;
						Resource root = cfclocation.getPhysical();
		                String path = ResourceUtil.getPathToChild(res, root);
		                if(!Util.isEmpty(path,true)) {
		                	ps2=cfclocation.getPageSource(path);
		                }
		                if(ps2!=null)ps=ps2;
					}


					//Page p = ps.loadPage(pc.getConfig());
					String name=res.getName();
					name=name.substring(0,name.length()-4);
					Page p = ComponentLoader.loadPage(pc, ps,true);
					if(!(p instanceof InterfacePage)){
						Component cfc = ComponentLoader.loadComponent(pc, p, ps, name, true,true);
						if(CommonUtil.isPersistent(cfc)){
							components.add(cfc);
						}
					}
				}
				catch (PageException e) {
					if(!ormConf.skipCFCWithError())throw e;
					//e.printStackTrace();
				}
			}
		}
	}


	public static Mapping[] createMappings(PageContext pc,Resource[] resources) {

			Mapping[] mappings=new Mapping[resources.length];
			Config config=pc.getConfig();
			for(int i=0;i<mappings.length;i++) {
				mappings[i]=CommonUtil.createMapping(config,
						"/",
						resources[i].getAbsolutePath()
						);
			}
			return mappings;
		}
}
