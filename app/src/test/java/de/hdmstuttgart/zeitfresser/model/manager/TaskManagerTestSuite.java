package de.hdmstuttgart.zeitfresser.model.manager;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by patrick on 22.02.17.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({TaskManagerBaseTest.class, TaskManagerFilterZeroDurationTasksTest.class,
    TaskManagerGetTasksWithRecordsEarlierThanTest.class,
    TaskManagerGetTasksWithRecordsLaterThanTest.class})
public class TaskManagerTestSuite {
}
