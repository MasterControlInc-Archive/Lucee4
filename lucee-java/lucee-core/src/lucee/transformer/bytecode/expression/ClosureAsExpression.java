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
package lucee.transformer.bytecode.expression;

import lucee.transformer.bytecode.BytecodeContext;
import lucee.transformer.bytecode.BytecodeException;
import lucee.transformer.bytecode.statement.udf.Closure;
import lucee.transformer.bytecode.util.Types;

import org.objectweb.asm.Type;

public class ClosureAsExpression extends ExpressionBase {

	private Closure closure;


	public ClosureAsExpression(Closure closure) {
		super(closure.getStart(),closure.getEnd());
		this.closure=closure;
	}
	
	public Type _writeOut(BytecodeContext bc, int mode) throws BytecodeException {
		closure._writeOut(bc);
		return Types.UDF_IMPL;
	}

	/**
	 * @return the closure
	 */
	public Closure getClosure() {
		return closure;
	}
}
