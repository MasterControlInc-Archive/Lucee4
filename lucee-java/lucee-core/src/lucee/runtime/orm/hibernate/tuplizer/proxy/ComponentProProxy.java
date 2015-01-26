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
package lucee.runtime.orm.hibernate.tuplizer.proxy;

import java.util.Iterator;
import java.util.Set;

import lucee.runtime.Component;
import lucee.runtime.ComponentPro;
import lucee.runtime.PageContext;
import lucee.runtime.component.Member;
import lucee.runtime.component.Property;
import lucee.runtime.dump.DumpData;
import lucee.runtime.dump.DumpProperties;
import lucee.runtime.exp.PageException;
import lucee.runtime.type.Struct;

/*
 * this implementation "simulates" all ComponentPro methods from core 
 */

public abstract class ComponentProProxy extends ComponentProxy implements ComponentPro {

	private static final long serialVersionUID = -7646935560408716588L;
	
	public abstract ComponentPro getComponentPro();
	

	public Property[] getProperties(boolean onlyPeristent, boolean includeBaseProperties, boolean overrideProperties, boolean inheritedMappedSuperClassOnly) {
		return getComponentPro().getProperties(onlyPeristent, includeBaseProperties, overrideProperties, inheritedMappedSuperClassOnly);
	}

	public static Property[] getProperties(Component c,boolean onlyPeristent, boolean includeBaseProperties, boolean overrideProperties, boolean inheritedMappedSuperClassOnly) {
		return ((ComponentPro)c).getProperties(onlyPeristent, includeBaseProperties, overrideProperties, inheritedMappedSuperClassOnly);
	}
	
	public boolean isPersistent() {
		return getComponentPro().isPersistent();
	}

	public static boolean isPersistent(Component c) {
		return ((ComponentPro)c).isPersistent();
	}

	public boolean isAccessors() {
		return getComponentPro().isAccessors();
	}

	public Object getMetaStructItem(Key name) {
		return getComponentPro().getMetaStructItem(name);
	}
	public static Object getMetaStructItem(Component c,Key name) {
		return ((ComponentPro)c).getMetaStructItem(name);
	}

	public Set<Key> keySet(int access) {
		return getComponentPro().keySet(access);
	}

	public Object call(PageContext pc, int access, Key name, Object[] args) throws PageException {
		return getComponentPro().call(pc, access, name, args);
	}

	public Object callWithNamedValues(PageContext pc, int access, Key name, Struct args) throws PageException {
		return getComponentPro().callWithNamedValues(pc, access, name, args);
	}

	public int size(int access) {
		return getComponentPro().size(access);
	}

	public Key[] keys(int access) {
		return getComponentPro().keys(access);
	}

	public Iterator<Entry<Key, Object>> entryIterator(int access) {
		return getComponentPro().entryIterator(access);
	}

	public Iterator<Object> valueIterator(int access) {
		return getComponentPro().valueIterator(access);
	}

	public Object get(int access, Key key) throws PageException {
		return getComponentPro().get(access, key);
	}

	public Object get(int access, Key key, Object defaultValue) {
		return getComponentPro().get(access, key, defaultValue);
	}

	public Iterator<Key> keyIterator(int access) {
		return getComponentPro().keyIterator(access);
	}
	
	public Iterator<String> keysAsStringIterator(int access) {
		return getComponentPro().keysAsStringIterator(access);
	}

	public DumpData toDumpData(PageContext pageContext, int maxlevel, DumpProperties dp, int access) {
		return getComponentPro().toDumpData(pageContext, maxlevel, dp, access);
	}

	public boolean contains(int access, Key name) {
		return getComponentPro().contains(access, name);
	}

	public Member getMember(int access, Key key, boolean dataMember, boolean superAccess) {
		return getComponentPro().getMember(access, key, dataMember, superAccess);
	}
	
	public void setEntity(boolean entity) {
		getComponentPro().setEntity(entity);
	}
	
	public static void setEntity(Component c,boolean entity) {
		((ComponentPro)c).setEntity(entity);
	}

	public boolean isEntity() {
		return getComponentPro().isEntity();
	}

	public Component getBaseComponent() {
		return getComponentPro().getBaseComponent();
	}
}
