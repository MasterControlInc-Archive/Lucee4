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
package lucee.runtime.type.trace;

import java.util.Map;
import java.util.Set;

import lucee.runtime.debug.Debugger;
import lucee.runtime.op.Duplicator;
import lucee.runtime.type.Collection;
import lucee.runtime.type.Struct;

public class TOStruct extends TOCollection implements Struct {

	private static final long serialVersionUID = 4868199372417392722L;
	private Struct sct;

	protected TOStruct(Debugger debugger,Struct sct,int type,String category,String text) {
		super(debugger,sct, type, category, text);
		this.sct=sct;
	}
	
	public boolean isEmpty() {
		log(null);
		return sct.isEmpty();
	}

	public boolean containsKey(Object key) {
		log(null);
		return sct.containsKey(key);
	}

	public boolean containsValue(Object value) {
		log(null);
		return sct.containsValue(value);
	}

	public Object get(Object key) {
		log(null);
		return sct.get(key);
	}

	public Object put(Object key, Object value) {
		log(null);
		return sct.put(key, value);
	}

	public Object remove(Object key) {
		log(null);
		return sct.remove(key);
	}

	public void putAll(Map m) {
		log(null);
		sct.putAll(m);
	}

	public Set keySet() {
		log(null);
		return sct.keySet();
	}

	public java.util.Collection values() {
		log(null);
		return sct.values();
	}

	public Set entrySet() {
		log(null);
		return sct.entrySet();
	}

	public Collection duplicate(boolean deepCopy) {
		log(null);
		return new TOStruct(debugger,(Struct)Duplicator.duplicate(sct,deepCopy), type, category, text);
	}

	@Override
	public long sizeOf() {
		log(null);
		return sct.sizeOf();
	}

	@Override
	public java.util.Iterator<String> getIterator() {
    	return keysAsStringIterator();
    } 

}
