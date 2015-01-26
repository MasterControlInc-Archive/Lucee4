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
package lucee.runtime.search.lucene2.query;

import org.apache.lucene.search.BooleanClause.Occur;


public class OccurUtil {

	public static Occur toOccur(boolean required, boolean prohibited) {
		if(required && !prohibited)		return Occur.MUST;
		if(!required && !prohibited)	return Occur.SHOULD;
		if(!required && prohibited)		return Occur.MUST_NOT;
		throw new RuntimeException("invalid Occur definition (required and prohibited)");
	}

}
