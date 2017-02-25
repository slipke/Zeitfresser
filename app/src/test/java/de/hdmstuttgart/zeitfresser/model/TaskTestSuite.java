package de.hdmstuttgart.zeitfresser.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A test suite for all test classes related to {@link Task}.
 *
 * @author patrick.kleindienst
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({TaskBehaviorTest.class, TaskEqualsHashcodeTest.class, TaskStateTest.class})
public class TaskTestSuite {
}
