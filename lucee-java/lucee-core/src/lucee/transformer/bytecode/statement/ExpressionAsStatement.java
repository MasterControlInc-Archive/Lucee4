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
package lucee.transformer.bytecode.statement;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;

import lucee.transformer.bytecode.BytecodeContext;
import lucee.transformer.bytecode.BytecodeException;
import lucee.transformer.bytecode.Literal;
import lucee.transformer.bytecode.expression.Expression;
import lucee.transformer.bytecode.util.ASMUtil;
import lucee.transformer.bytecode.util.Types;

public final class ExpressionAsStatement extends StatementBaseNoFinal {

	private Expression expr;


	/**
	 * Constructor of the class
	 * @param expr
	 */
	public ExpressionAsStatement(Expression expr) {
		super(expr.getStart(),expr.getEnd());
		this.expr=expr;
	}

	/**
	 *
	 * @see lucee.transformer.bytecode.statement.StatementBase#_writeOut(org.objectweb.asm.commons.GeneratorAdapter)
	 */
	public void _writeOut(BytecodeContext bc) throws BytecodeException {
		GeneratorAdapter adapter = bc.getAdapter();
		if(!(expr instanceof Literal)) {
			Type type = expr.writeOut(bc, Expression.MODE_VALUE);
			if(!type.equals(Types.VOID))ASMUtil.pop(adapter, type);
			//if(type.equals(Types.DOUBLE_VALUE))adapter.pop2();
			//else adapter.pop();
		}
	}
	
	/**
	 * @return the expr
	 */
	public Expression getExpr() {
		return expr;
	}
}
