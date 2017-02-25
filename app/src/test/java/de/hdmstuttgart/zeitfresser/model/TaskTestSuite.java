package de.hdmstuttgart.zeitfresser.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by patrick on 25.02.17.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({TaskBehaviorTest.class, TaskEqualsHashcodeTest.class, TaskStateTest.class})
public class TaskTestSuite {
}
