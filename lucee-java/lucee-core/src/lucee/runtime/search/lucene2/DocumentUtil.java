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
package lucee.runtime.search.lucene2;


import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import lucee.commons.io.IOUtil;
import lucee.commons.io.res.ContentType;
import lucee.commons.io.res.ContentTypeImpl;
import lucee.commons.io.res.Resource;
import lucee.commons.io.res.util.ResourceUtil;
import lucee.commons.net.http.HTTPResponse;
import lucee.runtime.op.Caster;
import lucee.runtime.search.lucene2.docs.FieldUtil;
import lucee.runtime.search.lucene2.docs.FileDocument;
import lucee.runtime.search.lucene2.docs.HTMLDocument;
import lucee.runtime.search.lucene2.docs.PDFDocument;
import lucee.runtime.search.lucene2.docs.WordDocument;

import org.apache.lucene.document.DateField;
import org.apache.lucene.document.Document;

/**
 * creates a matching Document Object to given File
 */
public final class DocumentUtil {

	public static Document toDocument(StringBuffer content,String root,URL url, HTTPResponse method) throws IOException {
        if(method.getStatusCode()!=200)return null;
        
		// get type and charset
		Document doc=null;
		ContentType ct = method.getContentType();
		long len=method.getContentLength();
		String charset=ct==null?"iso-8859-1":ct.getCharset();
        
        Runtime rt = Runtime.getRuntime();
        if(len>rt.freeMemory()){
        	Runtime.getRuntime().gc();
        	if(len>rt.freeMemory()) return null;
        }
        	
        //print.err("url:"+url+";chr:"+charset+";type:"+type);
        
        if(ct==null || ct.getMimeType()==null)  {}
        // HTML
        else if(ct.getMimeType().indexOf("text/html")!=-1) {
        	Reader r=null;
        	try{
        		r = IOUtil.getReader(method.getContentAsStream(), charset);
        		doc= HTMLDocument.getDocument(content,r);
        	}
        	finally{
        		IOUtil.closeEL(r);
        	}
        }
        // PDF
        else if(ct.getMimeType().indexOf("application/pdf")!=-1) {
        	InputStream is=null;
        	try{
        		is=IOUtil.toBufferedInputStream(method.getContentAsStream());
        		doc= PDFDocument.getDocument(content,is);
        	}
        	finally {
        		IOUtil.closeEL(is);
        	}
        }
        // DOC
        else if(ct.getMimeType().equals("application/msword")) {
        	InputStream is=null;
        	try{
        		is=IOUtil.toBufferedInputStream(method.getContentAsStream());
        		doc= WordDocument.getDocument(content,is);
        	}
        	finally {
        		IOUtil.closeEL(is);
        	}
            
        }
        // Plain
        else if(ct.getMimeType().indexOf("text/plain")!=-1) {
        	Reader r=null;
        	try{
        		r=IOUtil.toBufferedReader(IOUtil.getReader(method.getContentAsStream(), charset));
        		doc= FileDocument.getDocument(content,r);
        	}
        	finally {
        		IOUtil.closeEL(r);
        	}
        }
        
        if(doc!=null){
        	String strPath=url.toExternalForm();
    	   
    	    doc.add(FieldUtil.UnIndexed("url", strPath));
    	    doc.add(FieldUtil.UnIndexed("key", strPath));
    	    doc.add(FieldUtil.UnIndexed("path", strPath));
    	    //doc.add(FieldUtil.UnIndexed("size", Caster.toString(file.length())));
    	    //doc.add(FieldUtil.Keyword("modified",DateField.timeToString(file.lastModified())));
        }
        
        return doc;
        
    }
	
    /**
     * translate the file to a Document Object
     * @param file
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    public static Document toDocument(Resource file,String url,String charset) throws IOException {
        String ext = ResourceUtil.getExtension(file,null);
        
       
        Document doc=null;
        if(ext!=null) {
            ext=ext.toLowerCase();
            //String mimeType=new MimetypesFileTypeMap().getContentType(f);
            // HTML
            if(ext.equals("htm") || ext.equals("html") || ext.equals("cfm") || ext.equals("cfml") || ext.equals("php") || ext.equals("asp") || ext.equals("aspx")) {
                doc= HTMLDocument.getDocument(file,charset);
            }
            // PDF
            else if(ext.equals("pdf")) {
                doc= PDFDocument.getDocument(file);
            }
            // DOC
            else if(ext.equals("doc")) {
                doc= WordDocument.getDocument(file);
            }
        }
        else { 
        	ContentTypeImpl ct = (ContentTypeImpl) ResourceUtil.getContentType(file);
        	String type = ct.getMimeType();
        	String c=ct.getCharset();
        	if(c!=null) charset=c;
            //String type=ResourceUtil.getMimeType(file,"");
            if(type==null)  {}
            // HTML
            else if(type.equals("text/html")) {
                doc= HTMLDocument.getDocument(file,charset);
            }
            // PDF
            else if(type.equals("application/pdf")) {
                doc= PDFDocument.getDocument(file);
            }
            // DOC
            else if(type.equals("application/msword")) {
                doc= WordDocument.getDocument(file);
            }
        }
        if(doc==null) doc= FileDocument.getDocument(file,charset);
        
        String strPath=file.getPath().replace('\\', '/');
	    String strName=strPath.substring(strPath.lastIndexOf('/'));
	    
	    
	    doc.add(FieldUtil.UnIndexed("url", strName));
	    
	    doc.add(FieldUtil.UnIndexed("key", strPath));
	    doc.add(FieldUtil.UnIndexed("path", file.getPath()));
	    doc.add(FieldUtil.UnIndexed("size", Caster.toString(file.length())));
	    doc.add(FieldUtil.UnIndexed("modified",DateField.timeToString(file.lastModified())));
        
        
        return doc;
    }
    
}