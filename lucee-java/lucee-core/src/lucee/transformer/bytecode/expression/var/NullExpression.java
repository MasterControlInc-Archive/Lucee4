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

import org.objectweb.asm.Type;

import lucee.transformer.bytecode.BytecodeContext;
import lucee.transformer.bytecode.BytecodeException;
import lucee.transformer.bytecode.expression.ExpressionBase;
import lucee.transformer.bytecode.util.ASMConstants;
import lucee.transformer.bytecode.util.Types;

public class NullExpression extends ExpressionBase {

	public static final NullExpression NULL_EXPRESSION=new NullExpression();
	
	private NullExpression() {
		super(null,null);
	}

	public Type _writeOut(BytecodeContext bc, int mode)
			throws BytecodeException {
		ASMConstants.NULL(bc.getAdapter());
		return Types.OBJECT;
	}

}
