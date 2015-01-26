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
package lucee.transformer.bytecode.expression.var;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.Type;

import lucee.runtime.type.scope.Scope;
import lucee.runtime.type.scope.ScopeFactory;
import lucee.transformer.bytecode.BytecodeContext;
import lucee.transformer.bytecode.BytecodeException;
import lucee.transformer.bytecode.Literal;
import lucee.transformer.bytecode.expression.ExprString;
import lucee.transformer.bytecode.expression.Expression;
import lucee.transformer.bytecode.expression.ExpressionBase;
import lucee.transformer.bytecode.literal.Identifier;
import lucee.transformer.bytecode.literal.LitString;

public final class VariableString extends ExpressionBase implements ExprString {

	private Expression expr;

	public VariableString(Expression expr) {
		super(expr.getStart(),expr.getEnd());
		this.expr=expr;
	}
 
	public Type _writeOut(BytecodeContext bc, int mode) throws BytecodeException {
		return translateVariableToExprString(expr,false).writeOut(bc, mode);
	}

	public static ExprString toExprString(Expression expr) {
		if(expr instanceof ExprString) return (ExprString) expr;
		return new VariableString(expr);
	}
	
	public static ExprString translateVariableToExprString(Expression expr, boolean rawIfPossible) throws BytecodeException {
		if(expr instanceof ExprString) return (ExprString) expr;
		return LitString.toExprString(translateVariableToString(expr,rawIfPossible), expr.getStart(),expr.getEnd());
	}
	
	private static String translateVariableToString(Expression expr, boolean rawIfPossible) throws BytecodeException {
		if(!(expr instanceof Variable)) throw new BytecodeException("can't translate value to a string",expr.getStart());
		return variableToString((Variable) expr,rawIfPossible);
	}
		

	public static String variableToString(Variable var, boolean rawIfPossible) throws BytecodeException {
		return lucee.runtime.type.util.ListUtil.arrayToList(variableToStringArray(var,rawIfPossible),".");
	}
	public static String[] variableToStringArray(Variable var, boolean rawIfPossible) throws BytecodeException {
		List members = var.getMembers();
			
		List<String> arr=new ArrayList<String>();
		if(var.getScope()!=Scope.SCOPE_UNDEFINED)arr.add(ScopeFactory.toStringScope(var.getScope(),"undefined"));
		Iterator it = members.iterator();
		DataMember dm;
		Expression n;
		while(it.hasNext()) {
			Object o = it.next();
			if(!(o instanceof DataMember)) throw new BytecodeException("can't translate Variable to a String",var.getStart());
			dm=(DataMember) o;
			n=dm.getName();
			if(n instanceof Literal) {
				if(rawIfPossible && n instanceof Identifier) {
					arr.add(((Identifier) n).getRaw());
				}
				else {
					arr.add(((Literal) n).getString());
				}
			}
			else throw new BytecodeException("argument name must be a constant value",var.getStart());
		}
		return arr.toArray(new String[arr.size()]);
	}
	
	public String castToString() throws BytecodeException{
		return translateVariableToString(expr,false);
	}
}
