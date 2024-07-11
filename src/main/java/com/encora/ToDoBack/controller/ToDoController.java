package com.encora.ToDoBack.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.encora.ToDoBack.model.ToDo;
import com.encora.ToDoBack.model.ToDo.Priority;
import com.encora.ToDoBack.service.ToDoService;
import com.encora.ToDoBack.model.Pagination;

import java.util.Optional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jakarta.validation.Valid;

import org.json.JSONObject;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;


@CrossOrigin(origins = "*")
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

    private boolean filterByName(ToDo todo, String filterName){
        if (filterName != null) {
            return todo.getText().toLowerCase().contains(filterName.toLowerCase());
        }
        return true;
    }

    private boolean filterByPriority(ToDo todo, String filterPriority){
        if (filterPriority!=null && !filterPriority.equalsIgnoreCase("all")) {
            return todo.getPriority() == Priority.valueOf(filterPriority.toUpperCase());
        }
        return true;
    }

    private boolean filterByDone(ToDo todo, String filterDone){
        if (filterDone != null && !filterDone.equalsIgnoreCase("all")) {
            if (filterDone.equals("done")){
                return todo.isDone();
            }else{
                return !todo.isDone();
            }
        }
        return true;
    }

    //@GetMapping("/todos?{pagesize}&{pageNumber}&{sortByDone}&{sortByDate}&{sortByPriority}&{nameFilter}&{priorityFilter}&{doneFilter}")
    @GetMapping("/todos")
    public Collection<ToDo> getCustom(
        @RequestParam int pageSize,
        @RequestParam int pageNumber,
        @RequestParam(required = false, defaultValue = "false") boolean sortByDone,
        @RequestParam(required = false, defaultValue = "false") boolean sortByDate,
        @RequestParam(required = false, defaultValue = "false") boolean sortByPriority,
        @RequestParam(required = false, defaultValue = "") String nameFilter,
        @RequestParam(required = false, defaultValue = "all") String priorityFilter,
        @RequestParam(required = false, defaultValue = "all") String doneFilter
    ){

        Collection<ToDo> todosCollection = toDoService.get(); 
        List<ToDo> todos = new ArrayList<>(todosCollection);

        Pagination pagination = new Pagination(pageSize, pageNumber);
        
        todos = todos.stream()
            .filter(todo -> filterByName(todo, nameFilter))
            .filter(todo -> filterByPriority(todo, priorityFilter))
            .filter(todo -> filterByDone(todo, doneFilter))
            .collect(java.util.stream.Collectors.toList());
        
        todos.sort((t1, t2) -> t2.getCreationDate().compareTo(t1.getCreationDate()));   // order by creation date, new tasks at the beggining
        if (sortByDate) {
            todos.sort((t1, t2) -> t1.forceDueDate().compareTo(t2.forceDueDate())); // sort by due date
        }

        if (sortByPriority) {
            todos.sort((t1, t2) -> t1.getPriority().compareTo(t2.getPriority())); // sort by priority
        }

        if (sortByDone) {
            todos.sort((t1, t2) -> Boolean.compare(t2.isDone(), t1.isDone())); // sort by done, done tasks at the beginning
        } else {
            todos.sort((t1, t2) -> Boolean.compare(t1.isDone(), t2.isDone())); // sort by done, done tasks at the end
        }


        int fromIndex = pagination.getPageSize() * (pagination.getPageNumber() - 1);
        int toIndex = Math.min(fromIndex + pagination.getPageSize(), todos.size());

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
    public String update(@PathVariable String id, @RequestBody String entity) {

        JSONObject jsonObject = new JSONObject(entity);

        String text = null;
        if (jsonObject.getString("text").length() > 0){
            text = jsonObject.getString("text");
        }

        Priority priority = null;
        if (jsonObject.getString("priority").length() > 0){
            priority = Priority.valueOf(jsonObject.getString("priority"));
        }

        LocalDateTime dueDate = null;
        String date = jsonObject.getString("dueDate");
        if (date.length() > 0){
            dueDate =LocalDateTime.parse(date);
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

/*
    @PutMapping("todos/{id}/undone")
    public String putDone(@PathVariable String id) {

        ToDo todo = toDoService.update(id, false);
        
        if (todo == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        if(done){
            todo.setDoneDate(LocalDateTime.now());
        }else{
            todo.setDoneDate(null);
        }

        return entity;
    }
 */
}

