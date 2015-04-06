package org.vas.domain.repository;

import java.sql.SQLException;

public interface Database {

  void createTables() throws SQLException;
}
