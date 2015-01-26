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

import java.util.Iterator;
import java.util.Set;

import lucee.runtime.component.Member;
import lucee.runtime.component.Property;
import lucee.runtime.dump.DumpData;
import lucee.runtime.dump.DumpProperties;
import lucee.runtime.exp.PageException;
import lucee.runtime.type.Collection;
import lucee.runtime.type.Struct;

public interface ComponentPro extends Component {
	public Property[] getProperties(boolean onlyPeristent, boolean includeBaseProperties, boolean preferBaseProperties, boolean inheritedMappedSuperClassOnly);
	
	public boolean isPersistent();
	public boolean isAccessors();
	public void setEntity(boolean entity);
	public boolean isEntity();
	public Component getBaseComponent();
	public Object getMetaStructItem(Collection.Key name);
    
	// access
    Set<Key> keySet(int access);
    Object call(PageContext pc, int access, Collection.Key name, Object[] args) throws PageException;
	Object callWithNamedValues(PageContext pc, int access, Collection.Key name, Struct args) throws PageException;
	int size(int access);
	Collection.Key[] keys(int access);

	Iterator<Collection.Key> keyIterator(int access);
	Iterator<String> keysAsStringIterator(int access);
	

	Iterator<Entry<Key, Object>> entryIterator(int access);
	Iterator<Object> valueIterator(int access);
	
	Object get(int access, Collection.Key key) throws PageException;
	Object get(int access, Collection.Key key, Object defaultValue);
	DumpData toDumpData(PageContext pageContext, int maxlevel, DumpProperties dp, int access);
	boolean contains(int access,Key name);
	Member getMember(int access,Collection.Key key, boolean dataMember,boolean superAccess);
}
