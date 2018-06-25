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
/**
 * Implements the CFML Function isquery
 */
package lucee.runtime.functions.query;

import lucee.commons.lang.ExceptionUtil;
import lucee.runtime.PageContext;
import lucee.runtime.exp.FunctionException;
import lucee.runtime.exp.PageException;
import lucee.runtime.functions.BIF;
import lucee.runtime.op.Caster;
import lucee.runtime.tag.TagUtil;
import lucee.runtime.type.Struct;
import lucee.transformer.library.tag.TagLibTag;

public final class QueryExecute extends BIF {

	private static final long serialVersionUID = -4714201927377662500L;

	public static Object call(PageContext pc , String sql) throws PageException {
		return call(pc, sql, null, null);
	}
	public static Object call(PageContext pc , String sql, Object params) throws PageException {
		return call(pc, sql, params, null);
	}
	public static Object call(PageContext pc , String sql, Object params, Struct options) throws PageException {
		
		lucee.runtime.tag.Query qry = (lucee.runtime.tag.Query) pc.use(lucee.runtime.tag.Query.class.getName());
		try { 
			try {
				// set attributes
				qry.setReturnVariable(true);
				qry.setName("QueryExecute");
				if(options!=null)TagUtil.setAttributeCollection(pc, qry, null, options, TagLibTag.ATTRIBUTE_TYPE_FIXED); 
				qry.setParams(params);
			
				int res=qry.doStartTag();
				pc.initBody(qry, res);
				pc.forceWrite(sql);
				qry.doAfterBody();
			}
			catch(Throwable t){
				ExceptionUtil.rethrowIfNecessary(t);
				try {
					qry.doCatch(t);
				}
				catch (Throwable t2) {
					throw Caster.toPageException(t2);
				}
			}
			finally {
				pc.popBody();
				qry.doFinally();
			}
			qry.doEndTag();
			return qry.getReturnVariable();
		}
		finally {
			pc.reuse(qry);
		}
		
	}

	@Override
	public Object invoke(PageContext pc, Object[] args) throws PageException {
		if(args.length<1 || args.length>3)
			throw new FunctionException(pc, "QueryExecute", 1, 3, args.length);

		if(args.length==3)
			return call(pc,Caster.toString(args[0]),args[1],Caster.toStruct(args[2]));
		if(args.length==2)
			return call(pc,Caster.toString(args[0]),args[1]);
		return call(pc,Caster.toString(args[0]));
	}
}