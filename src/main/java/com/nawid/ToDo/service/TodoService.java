package com.nawid.ToDo.service;

import com.nawid.ToDo.dto.TodoDto;
import com.nawid.ToDo.entity.Todo;

import java.util.List;

public interface TodoService {

    TodoDto addTodo(TodoDto todoDto);

    TodoDto getTodo(Long id);

    List<TodoDto> getAllTodos();
    TodoDto updateTodo(TodoDto todoDto, long id);
    void deleteTodo(long id);
    TodoDto completeTodo(long id);
    TodoDto incompleteTodo(long id);
}
