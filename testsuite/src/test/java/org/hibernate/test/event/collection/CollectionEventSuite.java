//$Id: $
/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2007, Red Hat Middleware LLC or third-party contributors as
 * indicated by the @author tags or express copyright attribution statements
 * applied by the authors.
 *
 * All third-party contributions are distributed under license by Red Hat
 * Middleware LLC.  This copyrighted material is made available to anyone
 * wishing to use, modify, copy, or redistribute it subject to the terms
 * and conditions of the GNU Lesser General Public License, as published by
 * the Free Software Foundation.  This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details.  You should
 * have received a copy of the GNU Lesser General Public License along with
 * this distribution; if not, write to: Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor Boston, MA  02110-1301  USA
 */
package org.hibernate.test.event.collection;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.test.event.collection.association.bidirectional.manytomany.BidirectionalManyToManyBagToSetCollectionEventTest;
import org.hibernate.test.event.collection.association.bidirectional.manytomany.BidirectionalManyToManySetToSetCollectionEventTest;
import org.hibernate.test.event.collection.association.bidirectional.onetomany.BidirectionalOneToManyBagCollectionEventTest;
import org.hibernate.test.event.collection.association.bidirectional.onetomany.BidirectionalOneToManySetCollectionEventTest;
import org.hibernate.test.event.collection.association.unidirectional.manytomany.UnidirectionalManyToManyBagCollectionEventTest;
import org.hibernate.test.event.collection.association.unidirectional.onetomany.UnidirectionalOneToManyBagCollectionEventTest;
import org.hibernate.test.event.collection.association.unidirectional.onetomany.UnidirectionalOneToManySetCollectionEventTest;
import org.hibernate.test.event.collection.values.ValuesBagCollectionEventTest;

/**
 *
 * @author Gail Badner
 */
public class CollectionEventSuite {
	public static Test suite() {
		TestSuite suite = new TestSuite( "Collection event test suite" );
		suite.addTest( BrokenCollectionEventTest.suite() );
		suite.addTest( BidirectionalManyToManyBagToSetCollectionEventTest.suite() );
		suite.addTest( BidirectionalManyToManySetToSetCollectionEventTest.suite() );
		suite.addTest( BidirectionalOneToManyBagCollectionEventTest.suite() );
		suite.addTest( BidirectionalOneToManySetCollectionEventTest.suite() );
		suite.addTest( UnidirectionalManyToManyBagCollectionEventTest.suite() );
		suite.addTest( UnidirectionalOneToManyBagCollectionEventTest.suite() );
		suite.addTest( UnidirectionalOneToManySetCollectionEventTest.suite() );
		suite.addTest( ValuesBagCollectionEventTest.suite() );
		return suite;
	}
}