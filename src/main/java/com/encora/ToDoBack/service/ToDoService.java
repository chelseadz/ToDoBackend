package com.encora.ToDoBack.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.encora.ToDoBack.model.ToDo;
import com.encora.ToDoBack.model.ToDo.Priority;

@Service
public class ToDoService {

    private HashMap<String,ToDo> db = new HashMap<>(){{

        for(int i = 0; i<25; i++){
            ToDo todo =new ToDo("tarea " + Integer.toString(i), Priority.HIGH);
            if(i%3 == 0){
                todo.setDone(true);
                todo.setDoneDate(LocalDateTime.now());
            }
            String key = todo.getId();
            put(key, todo);
        }
    }};

    public Collection<ToDo> get(){
        return db.values();
    }

    public ToDo get(String id){
        return db.get(id);
    }

    
    public ToDo remove(String id){
        return db.remove(id);
    }

    public ToDo save(String text, Priority priority, Optional<LocalDateTime> dueDate){
        ToDo todo;
        if (dueDate.isPresent()){
            todo = new ToDo(text, priority, dueDate.get());
        }
        else{
            todo = new ToDo(text, priority);
        }

        db.put(todo.getId(), todo);
        return todo;
        
    }

    public ToDo update(String id, String text, Priority priority, LocalDateTime dueDate){
        ToDo todo = db.get(id);
        if(todo!= null){
            if (text != null) { todo.setText(text); };
            if (priority != null) {todo.setPriority(priority);};
            if (dueDate != null) {todo.setDueDate(dueDate);};

            db.put(id, todo);

            return todo;
        }else{
            return null;
        }
        
    }

    public ToDo update(String id, boolean done){
        ToDo todo = db.get(id);

        todo.setDone(done);
        
        db.put(id, todo);
        return todo;
        
    }
   
}
