package com.nawid.ToDo.repository;

import com.nawid.ToDo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepo extends JpaRepository<Todo,Long> {




}
