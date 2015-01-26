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
package lucee.commons.collection;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;

import lucee.aprint;
import lucee.commons.io.IOUtil;
import lucee.commons.io.res.Resource;
import lucee.commons.io.res.ResourceProvider;
import lucee.commons.io.res.ResourcesImpl;

public class RemoveLN {
	public static void main(String[] args) throws IOException {
		ResourceProvider frp = ResourcesImpl.getFileResourceProvider();
		Resource res = frp.getResource("/Users/mic/Projects/Lucee/Source/lucee/lucee-java/lucee-core/src/lucee/commons/util/mod/SyncMap");
		BufferedReader r = IOUtil.toBufferedReader(IOUtil.getReader(res, (Charset)null));
		String line;
		StringBuilder sb=new StringBuilder();
		while((line=r.readLine())!=null){
			sb.append(line.substring(5));
			sb.append('\n');
		}
		aprint.e(sb);
		
	}
}
