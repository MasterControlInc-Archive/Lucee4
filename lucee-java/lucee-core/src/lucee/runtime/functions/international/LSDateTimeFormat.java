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
package lucee.runtime.functions.international;


import lucee.commons.date.TimeZoneUtil;
import lucee.runtime.PageContext;
import lucee.runtime.engine.ThreadLocalPageContext;
import lucee.runtime.exp.ExpressionException;
import lucee.runtime.ext.function.Function;
import lucee.runtime.functions.displayFormatting.DateTimeFormat;
import lucee.runtime.i18n.LocaleFactory;

/**
 * Implements the CFML Function dateformat
 */
public final class LSDateTimeFormat implements Function {

	private static final long serialVersionUID = -1677384484943178492L;

	public static String call(PageContext pc , Object object) throws ExpressionException {
		return DateTimeFormat.invoke(pc,object, null,pc.getLocale(),ThreadLocalPageContext.getTimeZone(pc));
	}
	
	public static String call(PageContext pc , Object object, String mask) throws ExpressionException {
		return DateTimeFormat.invoke(pc,object, mask,pc.getLocale(),ThreadLocalPageContext.getTimeZone(pc));
	}

	public static String call(PageContext pc , Object object, String mask,String strLocale) throws ExpressionException {
		return DateTimeFormat.invoke(pc,object, mask,LocaleFactory.getLocale(strLocale),ThreadLocalPageContext.getTimeZone(pc));
	}

	public static String call(PageContext pc , Object object, String mask,String strLocale,String strTimezone) throws ExpressionException {
		return DateTimeFormat.invoke(
				pc,object,mask, 
				strLocale==null?pc.getLocale():LocaleFactory.getLocale(strLocale),
				strTimezone==null?ThreadLocalPageContext.getTimeZone(pc):TimeZoneUtil.toTimeZone(strTimezone));
	}
	
}