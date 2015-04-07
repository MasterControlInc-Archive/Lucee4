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
package lucee.runtime.orm.hibernate.event;

import java.io.Serializable;

import lucee.runtime.Component;
import lucee.runtime.exp.PageException;
import lucee.runtime.orm.ORMUtil;
import lucee.runtime.orm.hibernate.CommonUtil;
import lucee.runtime.orm.hibernate.HibernateCaster;
import lucee.runtime.type.Collection;
import lucee.runtime.type.Struct;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

public class InterceptorImpl extends EmptyInterceptor {

	private static final long serialVersionUID = 7992972603422833660L;

	private final AllEventListener listener;
	private final boolean hasPreInsert;
	private final boolean hasPreUpdate;

	public InterceptorImpl(AllEventListener listener) {
		this.listener=listener;
		if(listener!=null) {
			Component cfc = listener.getCFC();
			hasPreInsert=EventListener.hasEventType(cfc, CommonUtil.PRE_INSERT);
			hasPreUpdate=EventListener.hasEventType(cfc, CommonUtil.PRE_UPDATE);
		}
		else {
			hasPreInsert=false;
			hasPreUpdate=false;
		}
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		
		return on(entity, id, state, null, propertyNames, types, CommonUtil.PRE_INSERT, hasPreInsert);
		//return super.onSave(entity, id, state, propertyNames, types);
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		return on(entity, id, currentState, toStruct(propertyNames, previousState), propertyNames, types, CommonUtil.PRE_UPDATE, hasPreUpdate);
		//return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
	}
	

	private boolean on(Object entity, Serializable id,
			Object[] state, Struct data,
			String[] propertyNames, Type[] types, Collection.Key eventType, boolean hasMethod) {
		
		Component cfc = CommonUtil.toComponent(entity,null);
		if(cfc!=null && EventListener.hasEventType(cfc, eventType)){
			EventListener.invoke(eventType, cfc, data, null);
		}
		if(hasMethod) 
			EventListener.invoke(eventType, listener.getCFC(), data, entity);
		
		
		boolean rtn=false;
		String prop;
		Object before,current;
		/* jira2049
		ORMSession session = null;
		try {
			session=ORMUtil.getSession(ThreadLocalPageContext.get());
		} catch (PageException pe) {}*/
		
        for(int i = 0; i < propertyNames.length; i++)	{
            prop = propertyNames[i];
            before = state[i];
            current = ORMUtil.getPropertyValue(/* jira2049 session,*/cfc, prop,null);
            
            if(before != current && (current == null || !CommonUtil.equalsComplexEL(before, current))) {
            	try {
					state[i] = HibernateCaster.toSQL(types[i], current,null);
				} catch (PageException e) {
					state[i] = current;
				}
				rtn = true;
            }
        }
        return rtn;	
	}
	
    
	
	
	

    private static Struct toStruct(String propertyNames[], Object state[])  {
        Struct sct = CommonUtil.createStruct();
        if(state!=null && propertyNames!=null){
        	for(int i = 0; i < propertyNames.length; i++) {
        		sct.setEL(CommonUtil.createKey(propertyNames[i]), state[i]);
        	}
    	}
        return sct;
    }
}
