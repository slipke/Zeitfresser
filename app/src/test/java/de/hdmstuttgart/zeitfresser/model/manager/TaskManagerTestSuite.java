package de.hdmstuttgart.zeitfresser.model.manager;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A test suite for all test classes related to {@link TaskManager}.
 *
 * @author patrick.kleindienst
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({TaskManagerFilterZeroDurationTasksTest.class,
    TaskManagerGetTasksWithRecordsEarlierThanTest.class,
    TaskManagerGetTasksWithRecordsLaterThanTest.class, TaskManagerPublicApiTest.class})
public class TaskManagerTestSuite {
}
