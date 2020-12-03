package org.hibernate.exception;

import java.lang.reflect.Field;

import junit.framework.TestCase;

public class NestableRuntimeExceptionTest extends TestCase {

    public void testShouldReturnNullCause() {
        NestableRuntimeException exception = new NestableRuntimeException();

        assertNull(exception.getCause());
    }

    public void testShouldReturnNonNullCause() {
        Exception cause = new Exception();
        NestableRuntimeException exception = new NestableRuntimeException(cause);

        assertEquals(cause, exception.getCause());
    }

    public void testGetCauseReturnNullForSelfCause() throws NoSuchFieldException, IllegalAccessException {
        NestableRuntimeException exception = new NestableRuntimeException();

        Field field = NestableRuntimeException.class.getDeclaredField("cause");
        field.setAccessible(true);
        field.set(exception, exception);

        assertNull(exception.getCause());
    }
}
