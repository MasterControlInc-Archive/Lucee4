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
package lucee.runtime.tag;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.jsp.tagext.Tag;

import lucee.commons.lang.ClassUtil;
import lucee.runtime.config.ConfigWeb;
import lucee.runtime.exp.ExpressionException;
import lucee.runtime.exp.PageException;
import lucee.runtime.op.Caster;

// TODO kann man nicht auf context ebene

/**
 * Pool to Handle Tags
 */
public final class TagHandlerPool {
	private ConcurrentHashMap<String,Queue<Tag>> map=new ConcurrentHashMap<String,Queue<Tag>>();
	private ConfigWeb config;
	
	public TagHandlerPool(ConfigWeb config) { 
		this.config=config;
	}

	/**
	 * return a tag to use from a class
	 * @param tagClass
	 * @return Tag
	 * @throws PageException
	 */
	public Tag use(String tagClass) throws PageException {
		Queue<Tag> queue = getQueue(tagClass);
		Tag tag = queue.poll();
		if(tag!=null) return tag;
		return loadTag(tagClass);
	}

	/**
	 * free a tag for reusing
	 * @param tag
	 * @throws ExpressionException
	 */
	public void reuse(Tag tag) {
		tag.release();
		Queue<Tag> queue = getQueue(tag.getClass().getName());
		queue.add(tag);
	}
	
	
	private Tag loadTag(String tagClass) throws PageException {
		try {
			Class<Tag> clazz = ClassUtil.loadClass(config.getClassLoader(),tagClass);
			return clazz.newInstance();
		} 
		catch (Exception e) {
			throw Caster.toPageException(e);
		}
	}

	private Queue<Tag> getQueue(String tagClass) {
		Queue<Tag> queue = map.get(tagClass);// doing get before, do avoid constructing ConcurrentLinkedQueue Object all the time
        if(queue!=null) return queue;
        Queue<Tag> nq,oq;
        oq=map.putIfAbsent(tagClass, nq=new ConcurrentLinkedQueue<Tag>());
        if(oq!=null) return oq;
        return nq;
        
	}

	public void reset() {
		map.clear();
	}
}