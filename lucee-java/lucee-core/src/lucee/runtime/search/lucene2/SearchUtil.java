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
package lucee.runtime.search.lucene2;

import java.util.Map;

import lucee.commons.collection.MapFactory;
import lucee.commons.lang.ClassUtil;
import lucee.commons.lang.StringUtil;
import lucee.runtime.search.SearchException;
import lucee.runtime.search.lucene2.analyzer.DanishAnalyzer;
import lucee.runtime.search.lucene2.analyzer.ItalianAnalyzer;
import lucee.runtime.search.lucene2.analyzer.NorwegianAnalyzer;
import lucee.runtime.search.lucene2.analyzer.PortugueseAnalyzer;
import lucee.runtime.search.lucene2.analyzer.SpanishAnalyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.apache.lucene.analysis.cz.CzechAnalyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.el.GreekAnalyzer;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.analysis.nl.DutchAnalyzer;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.th.ThaiAnalyzer;
public final class SearchUtil {

	private static Map<String,Analyzer> analyzers=MapFactory.<String,Analyzer>getConcurrentMap();
	
	public static Analyzer getAnalyzer(String language) throws SearchException {
        if(language==null)language="english";
        else language=language.toLowerCase().trim();
        language=lucee.runtime.search.SearchUtil.translateLanguage(language);
		
        Analyzer analyzer=analyzers.get(language);
        if(analyzer!=null) return analyzer;
        
        
        if(language.equals("english"))
        	analyzer= new StandardAnalyzer();
        else if(language.equals("german")) 		
        	analyzer= new GermanAnalyzer();
        else if(language.equals("russian")) 	
        	analyzer= new RussianAnalyzer();
        else if(language.equals("dutch")) 		
        	analyzer= new DutchAnalyzer();
        else if(language.equals("french")) 		
        	analyzer= new FrenchAnalyzer();
        else if(language.equals("norwegian")) 	
        	analyzer= new NorwegianAnalyzer();
        else if(language.equals("portuguese")) 	
        	analyzer= new PortugueseAnalyzer();
        else if(language.equals("spanish")) 	
        	analyzer= new SpanishAnalyzer();
        else if(language.equals("brazilian")) 	
        	analyzer= new BrazilianAnalyzer();
        else if(language.equals("chinese")) 	
        	analyzer= new ChineseAnalyzer();
        else if(language.startsWith("czech")) 	
        	analyzer= new CzechAnalyzer();
        else if(language.equals("greek")) 		
        	analyzer= new GreekAnalyzer();
        else if(language.equals("thai"))
        	analyzer= new ThaiAnalyzer();
        else if(language.equals("japanese"))
        	analyzer= new CJKAnalyzer();
        else if(language.equals("korean"))
        	analyzer= new CJKAnalyzer();

        else if(language.equals("italian"))
        	analyzer= new ItalianAnalyzer();
        else if(language.equals("danish"))
        	analyzer= new DanishAnalyzer();
        else if(language.equals("norwegian"))
        	analyzer= new NorwegianAnalyzer();
        else if(language.equals("finnish"))
        	analyzer= new SnowballAnalyzer( "Finnish" );
        else if(language.equals("swedish"))
        	analyzer= new SnowballAnalyzer( "Swedish" );
        
        
        else {
        	String clazzName="org.apache.lucene.analysis.el."+StringUtil.ucFirst(language.trim().toLowerCase())+"Analyzer;";
        	Object o=ClassUtil.loadInstance(clazzName,(Object)null);
            if(o==null){
            	clazzName="lucee.runtime.search.lucene2.analyzer."+StringUtil.ucFirst(language.trim().toLowerCase())+"Analyzer";
            	o=ClassUtil.loadInstance(clazzName,(Object)null);//Class.orName(clazzName).newInstance();
        	}
            if(o instanceof Analyzer) analyzer=(Analyzer) o;
            else if(o==null) 
            	 throw new SearchException("can't create Language Analyzer for Lanuage "+language+", make Analyzer ["+clazzName+"] available");
            else 
                throw new SearchException( "can't create Language Analyzer for Lanuage "+language+", Analyzer ["+clazzName+"] is of invalid type");
        }        
        analyzers.put(language, analyzer);
        return analyzer;
    }
}
