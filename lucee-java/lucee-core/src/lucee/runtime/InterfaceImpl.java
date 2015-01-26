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
package lucee.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lucee.commons.lang.StringUtil;
import lucee.runtime.component.ComponentLoader;
import lucee.runtime.component.MetaDataSoftReference;
import lucee.runtime.component.MetadataUtil;
import lucee.runtime.dump.DumpData;
import lucee.runtime.dump.DumpProperties;
import lucee.runtime.dump.DumpTable;
import lucee.runtime.engine.ThreadLocalPageContext;
import lucee.runtime.exp.PageException;
import lucee.runtime.type.Array;
import lucee.runtime.type.ArrayImpl;
import lucee.runtime.type.Collection;
import lucee.runtime.type.KeyImpl;
import lucee.runtime.type.Struct;
import lucee.runtime.type.StructImpl;
import lucee.runtime.type.UDF;
import lucee.runtime.type.UDFImpl;
import lucee.runtime.type.UDFProperties;
import lucee.runtime.type.util.ArrayUtil;
import lucee.runtime.type.util.KeyConstants;

/**
 * 
 * MUST add handling for new attributes (style, namespace, serviceportname, porttypename, wsdlfile, bindingname, and output)
 */ 
public class InterfaceImpl implements Interface {

	private static final long serialVersionUID = -2488865504508636253L;

	private static final InterfaceImpl[] EMPTY = new InterfaceImpl[]{};
	
	//private InterfacePage page;
	private PageSource pageSource;
	private String extend;
	private String hint;
	private String dspName;
	private String callPath;
	private boolean relPath;
	private Map meta;
	
	private InterfaceImpl[] superInterfaces;
	
	private Map<Collection.Key,UDF> udfs=new HashMap<Collection.Key,UDF>();
	private Map<Collection.Key,UDF> interfacesUDFs;

	/**
     * Constructor of the Component
     * @param output 
     * @param extend 
     * @param hint 
     * @param dspName 
     */
	public InterfaceImpl(InterfacePage page,String extend, String hint, String dspName,String callPath, boolean relPath,Map interfacesUDFs) {
    	this(page.getPageSource(),extend, hint, dspName,callPath, relPath,interfacesUDFs,null);
	}
	public InterfaceImpl(InterfacePage page,String extend, String hint, String dspName,String callPath, boolean relPath,Map interfacesUDFs, Map meta) {
    	this(page.getPageSource(),extend, hint, dspName,callPath, relPath,interfacesUDFs,meta);
	}
	public InterfaceImpl(PageSource pageSource,String extend, String hint, String dspName,String callPath, boolean relPath,Map interfacesUDFs) {
    	this(pageSource, extend, hint, dspName, callPath, relPath, interfacesUDFs, null);
	}
	public InterfaceImpl(PageSource pageSource,String extend, String hint, String dspName,String callPath, boolean relPath,Map interfacesUDFs, Map meta) {
    	this.pageSource=pageSource;
    	this.extend=extend;
    	this.hint=hint;
    	this.dspName=dspName;
    	this.callPath=callPath;
    	this.relPath=relPath;
    	this.interfacesUDFs=interfacesUDFs;
    	this.meta=meta;
}
	 
	 
	 
	    

	private static void init(PageContext pc,InterfaceImpl icfc) throws PageException {
		if(!StringUtil.isEmpty(icfc.extend) && (icfc.superInterfaces==null || icfc.superInterfaces.length==0)) {
			icfc.superInterfaces=loadImplements(ThreadLocalPageContext.get(pc),icfc.getPageSource(),icfc.extend,icfc.interfacesUDFs);
		}
		else icfc.superInterfaces=EMPTY;
	}


    public static InterfaceImpl[] loadImplements(PageContext pc, PageSource child, String lstExtend, Map interfaceUdfs) throws PageException {
    	List<InterfaceImpl> interfaces=new ArrayList<InterfaceImpl>();
    	loadImplements(pc,child, lstExtend, interfaces, interfaceUdfs);
    	return interfaces.toArray(new InterfaceImpl[interfaces.size()]);
    	
	}

    private static void loadImplements(PageContext pc,PageSource child, String lstExtend,List interfaces, Map interfaceUdfs) throws PageException {
    	
    	Array arr = lucee.runtime.type.util.ListUtil.listToArrayRemoveEmpty(lstExtend, ',');
    	Iterator<Object> it = arr.valueIterator();
    	InterfaceImpl ic;
    	String extend;

    	while(it.hasNext()) {
    		extend=((String) it.next()).trim();
    		ic=ComponentLoader.loadInterface(pc,child,extend,interfaceUdfs);
    		interfaces.add(ic);
    		ic.setUDFListener(interfaceUdfs);
    		if(!StringUtil.isEmpty(ic.extend)) {
    			loadImplements(pc,ic.getPageSource(),ic.extend,interfaces,interfaceUdfs);
    		}
    	}
	}
    
    private void setUDFListener(Map<Collection.Key,UDF> interfacesUDFs) {
		this.interfacesUDFs=interfacesUDFs;
	}



	/*public boolean instanceOf(String type) {
    	boolean b = _instanceOf(type);
    	print.out("instanceOf("+type+"):"+page+":"+b);
    	return b;
    }*/
    public boolean instanceOf(String type) {
		if(relPath) {
        	if(type.equalsIgnoreCase(callPath)) return true;
            if(type.equalsIgnoreCase(pageSource.getComponentName())) return true;
            if(type.equalsIgnoreCase(_getName())) return true;       
        }
        else {
        	if(type.equalsIgnoreCase(callPath)) return true;
            if(type.equalsIgnoreCase(_getName())) return true; 
        }
		if(superInterfaces==null){
			try {
				init(null,this);
			} catch (PageException e) {
				superInterfaces=EMPTY;
			}
		}
    	for(int i=0;i<superInterfaces.length;i++){
    		if(superInterfaces[i].instanceOf(type))return true;
    	}
    	return false;
    }

 
    /**
	 * @return the callPath
	 */
	public String getCallPath() {
		return callPath;
	}



	private String _getName() { // MUST nicht so toll
	    if(callPath==null) return "";
	    return lucee.runtime.type.util.ListUtil.last(callPath,"./",true);
	}
    
    public void registerUDF(String key, UDF udf) {
    	udfs.put(KeyImpl.init(key),udf);
    	interfacesUDFs.put(KeyImpl.init(key), udf);
    }
    
    public void registerUDF(Collection.Key key, UDF udf) {
    	udfs.put(key,udf);
    	interfacesUDFs.put(key, udf);
    }
    
    public void registerUDF(String key, UDFProperties props) {
    	registerUDF(key, new UDFImpl(props));
    }
    
    public void registerUDF(Collection.Key key, UDFProperties props) {
    	registerUDF(key, new UDFImpl(props));
    }
    
    
    
    
    
    
    
    
    @Override
	public DumpData toDumpData(PageContext pageContext, int maxlevel, DumpProperties dp) {
	    DumpTable table = new DumpTable("interface","#99cc99","#ffffff","#000000");
        table.setTitle("Interface "+callPath+""+(" "+StringUtil.escapeHTML(dspName)));
        table.setComment("Interface can not directly invoked as a object");
        //if(top.properties.extend.length()>0)table.appendRow(1,new SimpleDumpData("Extends"),new SimpleDumpData(top.properties.extend));
        //if(top.properties.hint.trim().length()>0)table.appendRow(1,new SimpleDumpData("Hint"),new SimpleDumpData(top.properties.hint));
        
        //table.appendRow(1,new SimpleDumpData(""),_toDumpData(top,pageContext,maxlevel,access));
        return table;
    }

	/* *
	 * @return the page
	 * /
	public InterfacePage getPage() {
		return page;
	}*/

	public PageSource getPageSource() {
		return pageSource;
	}
	public InterfaceImpl[] getExtends() {
		return superInterfaces;
	}


	public Struct getMetaData(PageContext pc) throws PageException {
		return _getMetaData(pc,this,false);
	}
	public Struct getMetaData(PageContext pc, boolean ignoreCache) throws PageException {
		return _getMetaData(pc,this,ignoreCache);
	}
	private static Struct _getMetaData(PageContext pc,InterfaceImpl icfc, boolean ignoreCache) throws PageException {
		Page page=MetadataUtil.getPageWhenMetaDataStillValid(pc, icfc, ignoreCache);
    	if(page!=null && page.metaData!=null && page.metaData.get()!=null) return page.metaData.get();
    	
    	long creationTime=System.currentTimeMillis();
    	
		
		Struct sct=new StructImpl();
		ArrayImpl arr=new ArrayImpl();
		{
			Iterator<UDF> it = icfc.udfs.values().iterator();
	        while(it.hasNext()) {
	        	arr.append(it.next().getMetaData(pc));
	        }
		}
        
        if(icfc.meta!=null) {
        	Iterator it = icfc.meta.entrySet().iterator();
        	Map.Entry entry;
        	while(it.hasNext()){
        		entry=(Entry) it.next();
        		sct.setEL(KeyImpl.toKey(entry.getKey()), entry.getValue());
        	}
        }
        
        
        if(!StringUtil.isEmpty(icfc.hint,true))sct.set(KeyConstants._hint,icfc.hint);
        if(!StringUtil.isEmpty(icfc.dspName,true))sct.set(KeyConstants._displayname,icfc.dspName);
        init(pc,icfc);
        if(!ArrayUtil.isEmpty(icfc.superInterfaces)){
            Set<String> _set = lucee.runtime.type.util.ListUtil.listToSet(icfc.extend,',',true);
            Struct ex=new StructImpl();
        	sct.set(KeyConstants._extends,ex);
        	for(int i=0;i<icfc.superInterfaces.length;i++){
        		if(!_set.contains(icfc.superInterfaces[i].getCallPath())) continue;
        		ex.setEL(KeyImpl.init(icfc.superInterfaces[i].getCallPath()),_getMetaData(pc,icfc.superInterfaces[i],true));
        	}
        	
        }
        
        if(arr.size()!=0)sct.set(KeyConstants._functions,arr);
        PageSource ps = icfc.pageSource;
        sct.set(KeyConstants._name,ps.getComponentName());
        sct.set(KeyConstants._fullname,ps.getComponentName());
       
        sct.set(KeyConstants._path,ps.getDisplayPath());
        sct.set(KeyConstants._type,"interface");
        

        page.metaData=new MetaDataSoftReference<Struct>(sct,creationTime);
        return sct;
	}

}