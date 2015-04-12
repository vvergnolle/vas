package org.vas.domain.repository;

import java.util.List;

public interface UserService {

  User fetch(int id);

  List<User> list();
}
