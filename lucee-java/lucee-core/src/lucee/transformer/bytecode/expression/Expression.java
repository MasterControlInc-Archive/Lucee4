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

import lucee.runtime.exp.TemplateException;
import lucee.transformer.bytecode.BytecodeContext;
import lucee.transformer.bytecode.BytecodeException;
import lucee.transformer.bytecode.Position;

import org.objectweb.asm.Type;


/**
 * A Expression (Operation, Literal aso.)
 */
public interface Expression {

    /**
     * Field <code>MODE_REF</code>
     */
    public static final int MODE_REF=0;
    /**
     * Field <code>MODE_VALUE</code>
     */
    public static final int MODE_VALUE=1;
    
    /**
     * write out the stament to adapter
     * @param adapter
     * @param mode 
     * @return return Type of expression
     * @throws TemplateException
     */
    public Type writeOut(BytecodeContext bc, int mode) throws BytecodeException;

    public Position getStart();

    public Position getEnd();

    public void setStart(Position start);

    public void setEnd(Position end);
}
