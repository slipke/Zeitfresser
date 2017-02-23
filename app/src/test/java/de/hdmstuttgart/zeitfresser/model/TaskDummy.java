package de.hdmstuttgart.zeitfresser.model;

import java.util.List;

/**
 * The TaskDummy class is used for testing. It extends the current Task by some constructors for
 * easier access and the setRecord() method.
 */
class TaskDummy extends Task {
  TaskDummy(String name) {
    this.name = name;
  }

  TaskDummy(String name, List<Record> records) {
    this(name);
    this.setRecords(records);
  }

  void setRecords(List<Record> records) {
    this.records = records;
  }
}
