package de.hdmstuttgart.zeitfresser.model.manager;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import de.hdmstuttgart.zeitfresser.db.DbCalls;
import de.hdmstuttgart.zeitfresser.model.Record;
import de.hdmstuttgart.zeitfresser.model.Task;

import org.junit.Before;
import org.junit.Test;

public class DbTaskManagerTest {

  private DbTaskManager taskManager;
  private Context contextMock;

  @Before
  public void setUp() {
    contextMock = mock(Context.class);
    this.taskManager = DbTaskManager.createInstance(contextMock, "Test");
  }

  /**
   * Test whether the DbTaskManager successfully uses the DbCalls class.
   */
  @Test
  public void testGetTaskList() {
    DbCalls dbCallsMock = mock(DbCalls.class);
    this.taskManager.setDbCalls(dbCallsMock);
    this.taskManager.getTaskList();

    verify(dbCallsMock, times(1)).getTasks(contextMock);
  }

  /**
   * Test if the stopTask method works.
   */
  @Test
  public void testStopTask() {
    Task taskMock = mock(Task.class);
    DbCalls dbCallsMock = mock(DbCalls.class);
    Record activeRecordMock = mock(Record.class);
    when(taskMock.getActiveRecord()).thenReturn(activeRecordMock);
    this.taskManager.setDbCalls(dbCallsMock);
    this.taskManager.stopTask(taskMock);

    verify(taskMock, times(1)).stop();
    verify(dbCallsMock, times(1)).persistRecord(contextMock, activeRecordMock, taskMock);
  }
}
