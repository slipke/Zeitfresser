package de.hdmstuttgart.zeitfresser.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * This is a base class for our test cases concerning the {@link Task} class.
 *
 * @author patrick.kleindienst
 */

public class TaskBaseTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  protected Task classUnderTest;

  @Before
  public void setUp() throws Exception {
    this.classUnderTest = Task.withName("TestTask");
  }


  protected Field getFieldFromTestClass(String fieldName) throws NoSuchFieldException {
    if (fieldName != null && !fieldName.isEmpty()) {
      Field field = Task.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field;
    }
    throw new IllegalArgumentException("Field name must not be null or empty!");
  }

  protected Method getMethodFromTestClass(String methodName, Class<?>[] params) throws
      NoSuchMethodException {
    if (methodName != null && !methodName.isEmpty()) {
      Method method = Task.class.getDeclaredMethod(methodName, params);
      method.setAccessible(true);
      return method;
    }
    throw new IllegalArgumentException("Method name must not be null or empty!");
  }
}
