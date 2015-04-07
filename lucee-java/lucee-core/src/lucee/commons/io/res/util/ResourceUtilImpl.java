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
package lucee.commons.io.res.util;

import java.io.IOException;

import lucee.commons.io.IOUtil;
import lucee.commons.io.res.Resource;
import lucee.commons.io.res.filter.ResourceFilter;
import lucee.commons.io.res.filter.ResourceNameFilter;
import lucee.runtime.PageContext;
import lucee.runtime.exp.PageException;
import lucee.runtime.functions.system.ContractPath;

public class ResourceUtilImpl implements lucee.runtime.util.ResourceUtil {

	private ResourceUtilImpl(){}
	private static ResourceUtilImpl impl=new ResourceUtilImpl();
	

	public static ResourceUtilImpl getInstance() {
		return impl;
	}
	
	@Override
	public void checkCopyToOK(Resource source, Resource target) throws IOException {
		ResourceUtil.checkCopyToOK(source, target);
	}

	@Override
	public void checkCreateDirectoryOK(Resource resource,boolean createParentWhenNotExists) throws IOException {
		ResourceUtil.checkCreateDirectoryOK(resource, createParentWhenNotExists);
	}

	@Override
	public void checkCreateFileOK(Resource resource,boolean createParentWhenNotExists) throws IOException {
		ResourceUtil.checkCreateFileOK(resource, createParentWhenNotExists);
	}

	@Override
	public void checkGetInputStreamOK(Resource resource) throws IOException {
		ResourceUtil.checkGetInputStreamOK(resource);
	}

	@Override
	public void checkGetOutputStreamOK(Resource resource) throws IOException {
		ResourceUtil.checkGetOutputStreamOK(resource);
	}

	@Override
	public void checkMoveToOK(Resource source, Resource target)throws IOException {
		ResourceUtil.checkMoveToOK(source, target);
	}

	@Override
	public void checkRemoveOK(Resource resource) throws IOException {
		ResourceUtil.checkRemoveOK(resource);
	}

	@Override
	public void copyRecursive(Resource src, Resource trg) throws IOException {
		ResourceUtil.copyRecursive(src, trg);
	}

	@Override
	public void copyRecursive(Resource src, Resource trg, ResourceFilter filter) throws IOException {
		ResourceUtil.copyRecursive(src, trg,filter);
	}

	@Override
	public Resource createResource(Resource res, short level, short type) {
		return ResourceUtil.createResource(res, level, type);
	}

	@Override
	public String getExtension(Resource res) {
		return ResourceUtil.getExtension(res,null);
	}

	@Override
	public String getExtension(Resource res, String defaultValue) {
		return ResourceUtil.getExtension(res,defaultValue);
	}

	@Override
	public String getExtension(String strFile) {
		return ResourceUtil.getExtension(strFile,null);
	}

	@Override
	public String getExtension(String strFile, String defaultValue) {
		return ResourceUtil.getExtension(strFile,defaultValue);
	}

	@Override
	public String getMimeType(Resource res, String defaultValue) {
		return ResourceUtil.getMimeType(res, defaultValue);
	}

	@Override
	public String getMimeType(byte[] barr, String defaultValue) {
		return IOUtil.getMimeType(barr, defaultValue);
	}

	@Override
	public String getPathToChild(Resource file, Resource dir) {
		return ResourceUtil.getPathToChild(file, dir);
	}

	@Override
	public boolean isChildOf(Resource file, Resource dir) {
		return ResourceUtil.isChildOf(file, dir);
	}

	@Override
	public boolean isEmpty(Resource res) {
		return ResourceUtil.isEmpty(res);
	}

	@Override
	public boolean isEmptyDirectory(Resource res) {
		return ResourceUtil.isEmptyDirectory(res,null); // FUTURE add to interface with filter
	}

	@Override
	public boolean isEmptyFile(Resource res) {
		return ResourceUtil.isEmptyFile(res);
	}

	@Override
	public String merge(String parent, String child) {
		return ResourceUtil.merge(parent, child);
	}

	@Override
	public void moveTo(Resource src, Resource dest) throws IOException {
		ResourceUtil.moveTo(src, dest,true);
	}

	@Override
	public void removeChildren(Resource res) throws IOException {
		ResourceUtil.removeChildren(res);
	}

	@Override
	public void removeChildren(Resource res, ResourceNameFilter filter)throws IOException {
		ResourceUtil.removeChildren(res, filter);
	}

	@Override
	public void removeChildren(Resource res, ResourceFilter filter) throws IOException {
		ResourceUtil.removeChildren(res, filter);
	}

	@Override
	public String removeScheme(String scheme, String path) {
		return ResourceUtil.removeScheme(scheme, path);
	}

	@Override
	public void setAttribute(Resource res, String attributes) throws IOException {
		ResourceUtil.setAttribute(res, attributes);
	}

	@Override
	public Resource toResourceExisting(PageContext pc, String path) throws PageException {
		return ResourceUtil.toResourceExisting(pc, path);
	}

	@Override
	public Resource toResourceExistingParent(PageContext pc, String destination)throws PageException {
		return ResourceUtil.toResourceExistingParent(pc, destination);
	}

	@Override
	public Resource toResourceNotExisting(PageContext pc, String destination) {
		return ResourceUtil.toResourceNotExisting(pc, destination);
	}

	@Override
	public String translatePath(String path, boolean slashAdBegin,boolean slashAddEnd) {
		return ResourceUtil.translatePath(path, slashAdBegin, slashAddEnd);
	}

	@Override
	public String[] translatePathName(String path) {
		return ResourceUtil.translatePathName(path);
	}

	public String toString(Resource r, String charset) throws IOException {
		return IOUtil.toString(r, charset);
	}

	@Override
	public String contractPath(PageContext pc, String path) {
		return ContractPath.call(pc, path);
	}
}
