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
package org.hibernate.cache;

import java.io.Serializable;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.hibernate.EntityMode;
import org.hibernate.engine.QueryParameters;
import org.hibernate.engine.RowSelection;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.Type;
import org.hibernate.util.EqualsHelper;

/**
 * A key that identifies a particular query with bound parameter values
 * @author Gavin King
 */
public class QueryKey implements Serializable {
	private final String sqlQueryString;
	private final Type[] types;
	private final Object[] values;
	private final Integer firstRow;
	private final Integer maxRows;
	private final Map namedParameters;
	private final EntityMode entityMode;
	private final Set filters;
	
	// the user provided resulttransformer, not the one used with "select new". Here to avoid mangling transformed/non-transformed results.
	private final ResultTransformer customTransformer;

	/**
	 * For performance reasons, the hashCode is cached; however, it is marked transient so that it can be
	 * recalculated as part of the serialization process which allows distributed query caches to work properly.
	 */
	private transient int hashCode;
	
	public QueryKey(String queryString, QueryParameters queryParameters, Set filters, EntityMode entityMode) {
		this.sqlQueryString = queryString;
		this.types = queryParameters.getPositionalParameterTypes();
		this.values = queryParameters.getPositionalParameterValues();
		RowSelection selection = queryParameters.getRowSelection();
		if (selection!=null) {
			firstRow = selection.getFirstRow();
			maxRows = selection.getMaxRows();
		}
		else {
			firstRow = null;
			maxRows = null;
		}
		this.namedParameters = queryParameters.getNamedParameters();
		this.entityMode = entityMode;
		this.filters = filters;
		this.customTransformer = queryParameters.getResultTransformer();
		this.hashCode = generateHashCode();
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		this.hashCode = generateHashCode();
	}

	private int generateHashCode() {
		int result = 13;
		result = 37 * result + ( firstRow==null ? 0 : firstRow.hashCode() );
		result = 37 * result + ( maxRows==null ? 0 : maxRows.hashCode() );
		for ( int i=0; i<values.length; i++ ) {
			result = 37 * result + ( values[i]==null ? 0 : types[i].getHashCode( values[i], entityMode ) );
		}
		result = 37 * result + ( namedParameters==null ? 0 : namedParameters.hashCode() );
		result = 37 * result + ( filters==null ? 0 : filters.hashCode() );
		result = 37 * result + ( customTransformer==null ? 0 : customTransformer.hashCode() );
		result = 37 * result + sqlQueryString.hashCode();
		return result;
	}

	public int hashCode() {
		return hashCode;
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof QueryKey)) return false;
		QueryKey that = (QueryKey) other;
		if ( !sqlQueryString.equals(that.sqlQueryString) ) return false;
		if ( !EqualsHelper.equals(firstRow, that.firstRow) || !EqualsHelper.equals(maxRows, that.maxRows) ) return false;
		if ( !EqualsHelper.equals(customTransformer, that.customTransformer) ) return false;
		if (types==null) {
			if (that.types!=null) return false;
		}
		else {
			if (that.types==null) return false;
			if ( types.length!=that.types.length ) return false;
			for ( int i=0; i<types.length; i++ ) {
				if ( types[i].getReturnedClass() != that.types[i].getReturnedClass() ) return false;
				if ( !types[i].isEqual( values[i], that.values[i], entityMode ) ) return false;
			}
		}
		if ( !EqualsHelper.equals(filters, that.filters) ) return false;
		if ( !EqualsHelper.equals(namedParameters, that.namedParameters) ) return false;
		return true;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer()
			.append("sql: ")
			.append(sqlQueryString);
		if (values!=null) {
			buf.append("; parameters: ");
			for (int i=0; i<values.length; i++) {
				buf.append( values[i] )
					.append(", ");
			}
		}
		if (namedParameters!=null) {
			buf.append("; named parameters: ")
				.append(namedParameters);
		}
		if (filters!=null) {
			buf.append("; filters: ")
				.append(filters);
		}
		if (firstRow!=null) buf.append("; first row: ").append(firstRow);
		if (maxRows!=null) buf.append("; max rows: ").append(maxRows);
		if (customTransformer!=null) buf.append("; transformer: ").append(customTransformer);
		return buf.toString();
	}
	
}
