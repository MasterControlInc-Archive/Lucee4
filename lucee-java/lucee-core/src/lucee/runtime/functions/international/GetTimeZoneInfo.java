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
 * Implements the CFML Function gettimezoneinfo
 */
package lucee.runtime.functions.international;

import java.util.Calendar;
import java.util.TimeZone;

import lucee.commons.date.JREDateTimeUtil;
import lucee.runtime.PageContext;
import lucee.runtime.ext.function.Function;
import lucee.runtime.type.Struct;
import lucee.runtime.type.StructImpl;
import lucee.runtime.type.util.KeyConstants;

public final class GetTimeZoneInfo implements Function {

	
	public synchronized static lucee.runtime.type.Struct call(PageContext pc ) {
		
        //Date date = ;
        TimeZone timezone = pc.getTimeZone();
        Calendar c = JREDateTimeUtil.getThreadCalendar(timezone);
        c.setTimeInMillis(System.currentTimeMillis());

    	int dstOffset=c.get(Calendar.DST_OFFSET);
        int total = c.get(Calendar.ZONE_OFFSET) / 1000 + dstOffset / 1000;
        total *= -1;
        int j = total / 60;
        int hour = total / 60 / 60;
        int minutes = j % 60;
        
        Struct struct = new StructImpl();
        struct.setEL("utcTotalOffset", new Double(total));
        struct.setEL("utcHourOffset", new Double(hour));
        struct.setEL("utcMinuteOffset", new Double(minutes));
        struct.setEL("isDSTon", (dstOffset > 0)?Boolean.TRUE:Boolean.FALSE);
        struct.setEL(KeyConstants._id, timezone.getID());
        
       
        return struct;
		
        //return new StructImpl();
	}
}