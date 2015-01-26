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
 * Implements the CFML Function createtime
 */
package lucee.runtime.functions.dateTime;

import java.util.TimeZone;

import lucee.commons.date.DateTimeUtil;
import lucee.commons.date.TimeZoneUtil;
import lucee.runtime.PageContext;
import lucee.runtime.engine.ThreadLocalPageContext;
import lucee.runtime.exp.ExpressionException;
import lucee.runtime.ext.function.Function;
import lucee.runtime.type.dt.DateTime;
import lucee.runtime.type.dt.TimeImpl;

public final class CreateTime implements Function {
	public static DateTime call(PageContext pc , double hour, double minute, double second) {
		return _call(pc, hour, minute, second, 0,pc.getTimeZone());
	}

	public static DateTime call(PageContext pc , double hour, double minute, double second,double millis) {
		return _call(pc, hour, minute, second, millis,pc.getTimeZone());
	}
	public static DateTime call(PageContext pc , double hour, double minute, double second,double millis,String strTimezone) throws ExpressionException {
		return _call(pc, hour, minute, second, millis,strTimezone==null?pc.getTimeZone():TimeZoneUtil.toTimeZone(strTimezone));
	}
	

	private static DateTime _call(PageContext pc , double hour, double minute, double second,double millis,TimeZone tz) {
		// TODO check this looks wrong
		if(tz==null)tz=ThreadLocalPageContext.getTimeZone(pc);
		return new TimeImpl(
				DateTimeUtil.getInstance().toTime(tz,1899,12,30,(int)hour,(int)minute,(int)second,(int)millis,0)
				,false);
	}
}