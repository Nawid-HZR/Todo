package com.nawid.ToDo.service.impl;

import com.nawid.ToDo.dto.TodoDto;
import com.nawid.ToDo.entity.Todo;
import com.nawid.ToDo.exception.ResourceNotFoundException;
import com.nawid.ToDo.repository.TodoRepo;
import com.nawid.ToDo.service.TodoService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class TodoServiceImpl implements TodoService {


    @Autowired
    private TodoRepo todoRepo;

    @Autowired
    private ModelMapper modelMapper;



    @Override
    public TodoDto addTodo(TodoDto todoDto) {

        // TO CONVERT TODO_DTO INTO TODO JPA ENTITY
//        Todo todo = new Todo();
//        todo.setTitle(todoDto.getTitle());
//        todo.setDescription(todoDto.getDescription());
//        todo.setCompleted(todoDto.isCompleted());

        Todo todo = modelMapper.map(todoDto, Todo.class);


        // TODO JPA ENTITY
        Todo savedTodo = todoRepo.save(todo);

        //CONVERT SAVED TODO JPA ENTITY OBJECT INTO
        // TODO_DTO OBJECT
//        TodoDto savedTodoDto = new TodoDto();
//        savedTodoDto.setId(savedTodo.getId());
//        savedTodoDto.setTitle(savedTodo.getTitle());
//        savedTodoDto.setDescription(savedTodo.getDescription());
//        savedTodoDto.setCompleted(savedTodo.isCompleted());

        TodoDto savedTodoDto = modelMapper.map(savedTodo, TodoDto.class);

        return savedTodoDto;
    }

    @Override
    public TodoDto getTodo(Long id) {

        Todo todo = todoRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Todo Not Found with id: "+ id));

        return modelMapper.map(todo, TodoDto.class);
    }

    @Override
    public List<TodoDto> getAllTodos() {
        List<Todo> todos = todoRepo.findAll();
        return todos.stream().map((todo -> modelMapper.map(todo, TodoDto.class)))
                .collect(Collectors.toList());
    }
}
