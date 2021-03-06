/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 */
package org.hibernate.proxy.pojo.cglib;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.hibernate.HibernateException;
import org.hibernate.type.AbstractComponentType;

/**
 * Serializable placeholder for <tt>CGLIB</tt> proxies
 */
public final class SerializableProxy implements Serializable {

	private String entityName;
	private Class persistentClass;
	private Class[] interfaces;
	private Serializable id;
	private Class getIdentifierMethodClass;
	private Class setIdentifierMethodClass;
	private String getIdentifierMethodName;
	private String setIdentifierMethodName;
	private Class[] setIdentifierMethodParams;
	private AbstractComponentType componentIdType;

	public SerializableProxy() {}

	public SerializableProxy(
		final String entityName,
		final Class persistentClass,
		final Class[] interfaces,
		final Serializable id,
		final Method getIdentifierMethod,
		final Method setIdentifierMethod,
		AbstractComponentType componentIdType
	) {
		this.entityName = entityName;
		this.persistentClass = persistentClass;
		this.interfaces = interfaces;
		this.id = id;
		if (getIdentifierMethod!=null) {
			getIdentifierMethodClass = getIdentifierMethod.getDeclaringClass();
			getIdentifierMethodName = getIdentifierMethod.getName();
		}
		if (setIdentifierMethod!=null) {
			setIdentifierMethodClass = setIdentifierMethod.getDeclaringClass();
			setIdentifierMethodName = setIdentifierMethod.getName();
			setIdentifierMethodParams = setIdentifierMethod.getParameterTypes();
		}
		this.componentIdType = componentIdType;
	}

	private Object readResolve() {
		try {
			return CGLIBLazyInitializer.getProxy(
				entityName,
				persistentClass,
				interfaces,
				getIdentifierMethodName==null ?
					null :
					getIdentifierMethodClass.getDeclaredMethod(getIdentifierMethodName, null),
				setIdentifierMethodName==null ?
					null :
					setIdentifierMethodClass.getDeclaredMethod(setIdentifierMethodName, setIdentifierMethodParams),
					componentIdType,
				id,
				null
			);
		}
		catch (NoSuchMethodException nsme) {
			throw new HibernateException("could not create proxy for entity: " + entityName, nsme);
		}
	}

}
