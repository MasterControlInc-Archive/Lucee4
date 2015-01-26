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
package lucee.runtime.config.ajax;

import lucee.commons.io.res.Resource;
import lucee.commons.io.res.util.ResourceUtil;
import lucee.runtime.config.ConfigFactory;

public class AjaxFactory {


/**
* this method deploy all ajax functions to the lucee enviroment and the helper files
* @param dir tag directory
* @param doNew redeploy even the file exist, this is set to true when a new version is started
*/
public static void deployFunctions(Resource dir, boolean doNew) {
Resource f = dir.getRealResource("ajaxOnLoad.cfm");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/function/ajaxOnLoad.cfm",f);
        
}

/**
* this functions deploy all ajax tags to the lucee enviroment and the helper files
* @param dir tag directory
* @param doNew redeploy even the file exist, this is set to true when a new version is started
*/
public static void deployTags(Resource dir, boolean doNew) {
// tags
        Resource f = dir.getRealResource("AjaxImport.cfc");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/AjaxImport.cfc",f);
        f = dir.getRealResource("AjaxProxy.cfc");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/AjaxProxy.cfc",f);
        f = dir.getRealResource("Div.cfc");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/Div.cfc",f);
        f = dir.getRealResource("Map.cfc");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/Map.cfc",f);
        f = dir.getRealResource("MapItem.cfc");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/MapItem.cfc",f);
        f = dir.getRealResource("Layout.cfc");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/Layout.cfc",f);
        f = dir.getRealResource("LayoutArea.cfc");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/LayoutArea.cfc",f);
        f = dir.getRealResource("Window.cfc");
        if(!f.exists() || doNew){
        	//String md5 = ConfigWebUtil.createMD5FromResource(f);
        	ConfigFactory.createFileFromResourceEL("/resource/library/tag/Window.cfc",f);
        }
        
        
        
        
        // helper files
        dir=dir.getRealResource("lucee/core/ajax/");
        if(!dir.isDirectory())dir.mkdirs();
        f = dir.getRealResource("AjaxBase.cfc");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/AjaxBase.cfc",f);
        f = dir.getRealResource("AjaxBinder.cfc");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/AjaxBinder.cfc",f);
        f = dir.getRealResource("AjaxProxyHelper.cfc");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/AjaxProxyHelper.cfc",f);
        f = dir.getRealResource("JSLoader.cfc");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/JSLoader.cfc",f);
        f = dir.getRealResource("LuceeJs.cfc");
        if(f.exists())f.delete();
        
        //js
        Resource jsDir = dir.getRealResource("js");
        if(!jsDir.isDirectory())jsDir.mkdirs();
        f = jsDir.getRealResource("LuceeAjax.js");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/js/LuceeAjax.js",f);
        f = jsDir.getRealResource("LuceeMap.js");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/js/LuceeMap.js",f);
        f = jsDir.getRealResource("LuceeWindow.js");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/js/LuceeWindow.js",f);
        f = jsDir.getRealResource("LuceeLayout.js");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/js/LuceeLayout.js",f);
        
        // delete wrong directory comes with 3.1.2.015
        Resource gDir = dir.getRealResource("google");
        if(gDir.isDirectory())ResourceUtil.removeEL(gDir, true);
        
        // create google/... again
        gDir = jsDir.getRealResource("google");
        if(!gDir.isDirectory())gDir.mkdirs();
        f = gDir.getRealResource("google-map.js");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/js/google/google-map.js",f);
        
        
        //jquery resources
        Resource jqDir = jsDir.getRealResource("jquery");
        if(!jqDir.isDirectory())jqDir.mkdirs();
        f = jqDir.getRealResource("jquery-1.4.2.js");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/js/jquery/jquery-1.4.2.js",f);
        f = jqDir.getRealResource("jquery-ui-1.8.2.js");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/js/jquery/jquery-ui-1.8.2.js",f);
        f = jqDir.getRealResource("jquery.layout.js");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/js/jquery/jquery.layout.js",f);
        f = jqDir.getRealResource("jquery.window.js");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/js/jquery/jquery.window.js",f);
  
        //css Lucee Skin
        Resource cssDir = dir.getRealResource("css/jquery");
        if(!cssDir.isDirectory())cssDir.mkdirs();
        f = cssDir.getRealResource("LuceeSkin.css.cfm");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/css/jquery/LuceeSkin.css.cfm",f);
        
        //css images
        Resource imgDir = cssDir.getRealResource("images");
        if(!imgDir.isDirectory())imgDir.mkdirs();
        f = imgDir.getRealResource("ui-anim_basic_16x16.gif.cfm");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/css/jquery/images/ui-anim_basic_16x16.gif.cfm",f);
        f = imgDir.getRealResource("ui-bg_flat_0_aaaaaa_40x100.png.cfm");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/css/jquery/images/ui-bg_flat_0_aaaaaa_40x100.png.cfm",f);
        f = imgDir.getRealResource("ui-bg_flat_75_ffffff_40x100.png.cfm");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/css/jquery/images/ui-bg_flat_75_ffffff_40x100.png.cfm",f);
        f = imgDir.getRealResource("ui-bg_glass_55_fbf9ee_1x400.png.cfm");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/css/jquery/images/ui-bg_glass_55_fbf9ee_1x400.png.cfm",f);
        f = imgDir.getRealResource("ui-bg_glass_65_ffffff_1x400.png.cfm");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/css/jquery/images/ui-bg_glass_65_ffffff_1x400.png.cfm",f);
        f = imgDir.getRealResource("ui-bg_glass_75_dadada_1x400.png.cfm");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/css/jquery/images/ui-bg_glass_75_dadada_1x400.png.cfm",f);
        f = imgDir.getRealResource("ui-bg_glass_75_e6e6e6_1x400.png.cfm");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/css/jquery/images/ui-bg_glass_75_e6e6e6_1x400.png.cfm",f);
        f = imgDir.getRealResource("ui-bg_glass_95_fef1ec_1x400.png.cfm");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/css/jquery/images/ui-bg_glass_95_fef1ec_1x400.png.cfm",f);
        f = imgDir.getRealResource("ui-bg_highlight-soft_75_cccccc_1x100.png.cfm");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/css/jquery/images/ui-bg_highlight-soft_75_cccccc_1x100.png.cfm",f);
        f = imgDir.getRealResource("ui-icons_222222_256x240.png.cfm");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/css/jquery/images/ui-icons_222222_256x240.png.cfm",f);
        f = imgDir.getRealResource("ui-icons_2e83ff_256x240.png.cfm");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/css/jquery/images/ui-icons_2e83ff_256x240.png.cfm",f);
        f = imgDir.getRealResource("ui-icons_454545_256x240.png.cfm");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/css/jquery/images/ui-icons_454545_256x240.png.cfm",f);
        f = imgDir.getRealResource("ui-icons_888888_256x240.png.cfm");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/css/jquery/images/ui-icons_888888_256x240.png.cfm",f);
        f = imgDir.getRealResource("ui-icons_cd0a0a_256x240.png.cfm");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/css/jquery/images/ui-icons_cd0a0a_256x240.png.cfm",f);
       
        
        //image loader
        dir = dir.getRealResource("loader");
        if(!dir.isDirectory())dir.mkdirs();
        f = dir.getRealResource("loading.gif.cfm");
        if(!f.exists() || doNew)ConfigFactory.createFileFromResourceEL("/resource/library/tag/lucee/core/ajax/loader/loading.gif.cfm",f);
}

}

