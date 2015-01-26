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



import lucee.commons.io.res.Resource;
import lucee.commons.io.res.util.ResourceUtil;
import lucee.commons.io.res.filter.ResourceFilter;

/**
 * FilFilter that only allow filter with given extensions 
 * by constructor or directory, if constructor variable recurse is true
 */
public final class LuceneExtensionFileFilter implements ResourceFilter {

    private String[] extensions;
    private boolean recurse;
    private boolean noExtension;
    private boolean allExtension;

    /**
     * constructor of the class
     * @param extensions
     * @param recurse
     */
    public LuceneExtensionFileFilter(String[] extensions, boolean recurse) {
        
        this.extensions=extensions;
        
        for(int i=0;i<extensions.length;i++) {
            String ext = extensions[i].trim();
            
            if(ext.equals("*."))	{
                noExtension=true;
                continue;
            }
            if(ext.equals(".*") || ext.equals("*.*"))	{
                allExtension = true;
                continue;
            }
            
            // asterix
            int startIndex=ext.indexOf('*');
            if(startIndex==0) ext=ext.substring(1);
            
            // dot
            int startDot=ext.indexOf('.');
            if(startDot==0) ext=ext.substring(1);
            
            if(ext.equals("*"))ext="";
            //print.ln(ext);
            extensions[i]=ext.toLowerCase();
        }
        this.recurse=recurse;
    }

    @Override
    public boolean accept(Resource res) {
        if(res.isDirectory()) return recurse;
        else if(res.isFile()) {
            String ext=ResourceUtil.getExtension(res,null);
            if(ext==null) return noExtension;
            else if(allExtension) return true;
                        
            for(int i=0;i<extensions.length;i++) {
                if(extensions[i].equalsIgnoreCase(ext)) return true;
            }
            return false;
        }
        return false;
    }
}