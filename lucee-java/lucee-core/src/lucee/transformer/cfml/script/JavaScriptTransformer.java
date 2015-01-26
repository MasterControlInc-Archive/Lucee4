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
package lucee.transformer.cfml.script;

import lucee.runtime.exp.TemplateException;
import lucee.transformer.bytecode.Page;
import lucee.transformer.bytecode.ScriptBody;
import lucee.transformer.bytecode.statement.tag.Tag;
import lucee.transformer.cfml.TransfomerSettings;
import lucee.transformer.cfml.evaluator.EvaluatorPool;
import lucee.transformer.cfml.expression.CFMLExprTransformer;
import lucee.transformer.cfml.tag.CFMLTransformer;
import lucee.transformer.cfml.tag.TagDependentBodyTransformer;
import lucee.transformer.library.function.FunctionLib;
import lucee.transformer.library.tag.TagLib;
import lucee.transformer.library.tag.TagLibTag;
import lucee.transformer.util.CFMLString;


/**	
 * Innerhalb des Tag script kann in CFML eine eigene Scriptsprache verwendet werden, 
 * welche sich an Javascript orientiert. 
 * Da der data.cfml Transformer keine Spezialfaelle zulaesst, 
 * also Tags einfach anhand der eingegeben TLD einliest und transformiert, 
 * aus diesem Grund wird der Inhalt des Tag script einfach als Zeichenkette eingelesen.
 * Erst durch den Evaluator (siehe 3.3), der fuer das Tag script definiert ist, 
 * wird der Inhalt des Tag script uebersetzt.
 * 
 */
public final class JavaScriptTransformer extends CFMLExprTransformer implements TagDependentBodyTransformer {
	
	@Override
	public void transform(Page page,CFMLTransformer parent, EvaluatorPool ep,
			TagLib[][] tlibs, FunctionLib[] flibs, Tag tag, TagLibTag tagLibTag,TagLibTag[] scriptTags, CFMLString cfml,TransfomerSettings settings)
			throws TemplateException {
		
		StringBuilder sb=new StringBuilder();
		//MUST add again int startline=cfml.getLine();
		while(!cfml.isAfterLast() && !cfml.isCurrent("</",tagLibTag.getFullName())){
			sb.append(cfml.getCurrent());
			cfml.next();
		}
		//int endline=cfml.getLine();
		if(cfml.isAfterLast())
			throw new TemplateException(cfml,"missing end tag"); // TODO better error message
		
		
		if(true) throw new RuntimeException("not implemented");
		//MUST add again String dummyStart="public class Susi {public static void code(){"+StringUtil.repeatString("\n", startline-1);
		
		//MUST add again String dummyEnd="}}";
		//MUST add again String src=dummyStart+sb+dummyEnd;
		//MUST add again Label start=new Label();
		//MUST add again Label end=new Label();
		
		//MUST add again ByteArrayInputStream bais = new ByteArrayInputStream(src.getBytes());
		
		try {
			//MUST add again CompilationUnit cu = JavaParser.parse(bais);
			//MUST add again DataBag db = new DataBag();
			ScriptBody body=new ScriptBody();
			tag.setBody(body);
			//MUST add again new JavaParserVisitor(body,start,end).visit(cu, db);
			
		} 
		catch (Exception e) {
			throw new TemplateException(cfml,e);
		}
	}
}