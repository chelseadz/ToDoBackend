package com.encora.ToDoBack.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.encora.ToDoBack.model.ToDo;
import com.encora.ToDoBack.model.ToDo.Priority;
import com.encora.ToDoBack.service.ToDoService;
import java.util.Optional;
import java.time.LocalDateTime;


import jakarta.validation.Valid;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;



@CrossOrigin(origins = "http://192.168.1.69:8080/")
@RestController
public class ToDoController {

    private final ToDoService toDoService;

    public ToDoController(ToDoService toDoService){
        this.toDoService = toDoService;
    }

    @GetMapping("/todos/")
    public Collection<ToDo> get() {
        Collection<ToDo> todos = toDoService.get();
        return todos;
    }
 
    @GetMapping("/todos/{id}")
    public ToDo get(@PathVariable String id) {
        ToDo todo = toDoService.get(id);
        if (todo == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return todo;
    }

    @GetMapping("/todos")
    public Collection<ToDo> getCustom(
        @RequestParam int pageSize,
        @RequestParam int pageNumber,
        @RequestParam(required = false, defaultValue = "false") boolean sortByDone,
        @RequestParam(required = false, defaultValue = "false") boolean sortByDate,
        @RequestParam(required = false, defaultValue = "false") boolean sortByPrior,
        @RequestParam(required = false, defaultValue = "") String filterName,
        @RequestParam(required = false, defaultValue = "all") String filterPriority,
        @RequestParam(required = false, defaultValue = "all") String filterDone

    ) {
        Collection<ToDo> todosCollection = toDoService.get(); 
        List<ToDo> todos = new ArrayList<>(todosCollection);

        if (filterName.length()>0) {
            todos.removeIf(todo -> !todo.getText().toLowerCase().contains(filterName.toLowerCase()));
        }
    
        if (!filterPriority.equals("all") && !filterPriority.equals("ALL")) {
            todos.removeIf(todo -> todo.getPriority() != Priority.valueOf(filterPriority.toUpperCase()));
        }
    
        if (!filterDone.equals("all") && !filterDone.equals("ALL")) {
            boolean isDone = (filterDone == "done");
            if (isDone){
                todos.removeIf(todo -> todo.isDone() != isDone);
            }else{
                todos.removeIf(todo -> todo.isDone() == isDone);
            }
        }

        if (sortByDone) {
            todos.sort((t1, t2) -> Boolean.compare(t2.isDone(), t1.isDone()));
        }else{
            todos.sort((t1, t2) -> Boolean.compare(t1.isDone(), t2.isDone()));
        }
        if (sortByDate) {
            todos.sort((t1, t2) -> (t1.forceDueDate()).compareTo(t2.forceDueDate()));
        } else{
            todos.sort((t1, t2) -> (t2.forceDueDate()).compareTo(t1.forceDueDate()));
        }
        if (sortByPrior) {
            todos.sort((t1, t2) -> t1.getPriority().compareTo(t2.getPriority()));
        } 

        int fromIndex = pageSize * (pageNumber - 1);
        int toIndex = Math.min(fromIndex + pageSize, todos.size());

        if (fromIndex > todos.size()) {
            return Collections.emptyList();
        }

        List<ToDo> paginatedTodos = todos.subList(fromIndex, toIndex);

        return paginatedTodos;
    }
         
    
    @DeleteMapping("/todos/{id}")
    public void  delete(@PathVariable String id) {
        ToDo todo = toDoService.remove(id);
        if (todo == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }


    @PostMapping("/todos")
    public ToDo create(@RequestBody @Valid String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);

        String text = jsonObject.getString("text");
        Priority priority = Priority.valueOf(jsonObject.getString("priority"));

        Optional<LocalDateTime> dueDate;
        String date = jsonObject.getString("dueDate");
        if (date.length() > 1){
            dueDate = Optional.of(LocalDateTime.parse(date));
        }else{
            dueDate = Optional.empty();
        }

        ToDo todo = toDoService.save(text, priority, dueDate);

        todo.setCreationDate(LocalDateTime.now());
        
        return todo;
    }

    @PutMapping("todos/{id}")
    public String put(@PathVariable String id, @RequestBody String entity) {

        JSONObject jsonObject = new JSONObject(entity);

        Optional<String> text;
        String str = jsonObject.getString("text");
        if (str.length() != 0){
            text = Optional.of(str);
        }else{
            text = Optional.empty();
        }

        Optional<Priority> priority;
        String prior = jsonObject.getString("priority");
        if (prior.length() != 0){
            priority = Optional.of(Priority.valueOf(jsonObject.getString("priority")));
        }else{
            priority= Optional.empty();
        }

        Optional<LocalDateTime> dueDate;
        String date = jsonObject.getString("dueDate");
        if (date.length() != 0){
            dueDate = Optional.of(LocalDateTime.parse(date));
        }else{
            dueDate = Optional.empty();
        }

        ToDo todo = toDoService.update(id, text, priority, dueDate);
        if (todo == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return entity;
    }

    @PutMapping("todos/{id}/done")
    public String putDone(@PathVariable String id, @RequestBody String entity) {

        JSONObject jsonObject = new JSONObject(entity);

        boolean done = jsonObject.getBoolean("done");
        
        ToDo todo = toDoService.update(id, done);
        
        if (todo == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        if(done){
            todo.setDoneDate(LocalDateTime.now());
        }else{
            todo.setDoneDate(null);
        }

        return entity;
    }

}

