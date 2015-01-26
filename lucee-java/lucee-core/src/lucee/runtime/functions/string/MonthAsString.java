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
 * Implements the CFML Function monthasstring
 */
package lucee.runtime.functions.string;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import lucee.commons.date.JREDateTimeUtil;
import lucee.runtime.PageContext;
import lucee.runtime.exp.ExpressionException;
import lucee.runtime.ext.function.Function;
import lucee.runtime.i18n.LocaleFactory;

public final class MonthAsString implements Function {
	
	private static final int MONTH=1000*60*60*24*32;
	private static Date[] dates=new Date[12];
	static {
		Calendar cal=JREDateTimeUtil.getThreadCalendar();
		cal.setTimeInMillis(0);
		dates[0]=cal.getTime();
		for(int i=1;i<12;i++) {
			cal.add(Calendar.MONTH,1);
			dates[i]=cal.getTime();
		}
	}

	public static String call(PageContext pc , double month) throws ExpressionException {
		return call(month, pc.getLocale());
	}
	public static String call(PageContext pc , double month, String strLocale) throws ExpressionException {
		return call(month, strLocale==null?pc.getLocale():LocaleFactory.getLocale(strLocale));
	}
	
	private static String call(double month, Locale locale) throws ExpressionException {
		int m=(int)month;
		if(m>=1 && m<=12) {
			return new DateFormatSymbols(locale).getMonths()[m-1];
		}
		throw new ExpressionException("invalid month definition in function monthAsString, must be between 1 and 12 now ["+month+"]");
		
	}
}