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
package lucee.runtime.cache.tag;

import lucee.runtime.PageContext;
import lucee.runtime.exp.PageException;

public interface CacheHandler {
	public String label() throws PageException;
	public CacheItem get(PageContext pc, String id) throws PageException;
	public boolean remove(PageContext pc, String id) throws PageException;
	public void set(PageContext pc, String id, Object cachedwithin, CacheItem value) throws PageException;
	public void clear(PageContext pc) throws PageException;
	public void clear(PageContext pc, CacheHandlerFilter filter) throws PageException;
	public void clean(PageContext pc) throws PageException;
	public int size(PageContext pc) throws PageException;
     
}
