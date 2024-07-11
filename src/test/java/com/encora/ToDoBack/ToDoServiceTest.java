package com.encora.ToDoBack;

import com.encora.ToDoBack.model.ToDo;
import com.encora.ToDoBack.model.ToDo.Priority;
import com.encora.ToDoBack.service.ToDoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ToDoServiceTest {

    private ToDoService toDoService;

    @BeforeEach
    void setUp() {
        toDoService = new ToDoService();
    }

    @Test
    void get_shouldReturnAllTodos() {
        Collection<ToDo> todos = toDoService.get();
        assertEquals(45, todos.size());
    }

    @Test
    void get_withId_shouldReturnTodo() {
        ToDo expectedToDo = toDoService.get().iterator().next();
        ToDo actualToDo = toDoService.get(expectedToDo.getId());

        assertNotNull(actualToDo);
        assertEquals(expectedToDo.getId(), actualToDo.getId());
    }

    @Test
    void get_withInvalidId_shouldReturnNull() {
        ToDo actualToDo = toDoService.get("non-existent-id");
        assertNull(actualToDo);
    }

    @Test
    void remove_shouldRemoveTodo() {
        ToDo toDo = toDoService.get().iterator().next();
        ToDo removedToDo = toDoService.remove(toDo.getId());

        assertNotNull(removedToDo);
        assertEquals(toDo.getId(), removedToDo.getId());
        assertNull(toDoService.get(toDo.getId()));
    }

    @Test
    void remove_withInvalidId_shouldReturnNull() {
        ToDo removedToDo = toDoService.remove("non-existent-id");
        assertNull(removedToDo);
    }

    @Test
    void save_shouldCreateAndReturnNewTodo() {
        String text = "New Task";
        Priority priority = Priority.MEDIUM;
        Optional<LocalDateTime> dueDate = Optional.of(LocalDateTime.now().plusDays(3));

        ToDo toDo = toDoService.save(text, priority, dueDate);

        assertNotNull(toDo.getId());
        assertEquals(text, toDo.getText());
        assertEquals(priority, toDo.getPriority());
        assertEquals(dueDate.get(), toDo.getDueDate());
        assertNotNull(toDoService.get(toDo.getId()));
    }

    @Test
    void save_withoutDueDate_shouldCreateAndReturnNewTodo() {
        String text = "New Task";
        Priority priority = Priority.LOW;
        Optional<LocalDateTime> dueDate = Optional.empty();

        ToDo toDo = toDoService.save(text, priority, dueDate);

        assertNotNull(toDo.getId());
        assertEquals(text, toDo.getText());
        assertEquals(priority, toDo.getPriority());
        assertNull(toDo.getDueDate());
        assertNotNull(toDoService.get(toDo.getId()));
    }

    @Test
    void update_shouldUpdateExistingTodo() {
        ToDo toDo = toDoService.get().iterator().next();
        String newText = "Updated Task";
        Priority newPriority = Priority.LOW;
        LocalDateTime newDueDate = LocalDateTime.now().plusDays(2);

        ToDo updatedToDo = toDoService.update(toDo.getId(), newText, newPriority, newDueDate);

        assertEquals(newText, updatedToDo.getText());
        assertEquals(newPriority, updatedToDo.getPriority());
        assertEquals(newDueDate, updatedToDo.getDueDate());
    }

    @Test
    void update_withInvalidId_shouldReturnNull() {
        ToDo updatedToDo = toDoService.update("non-existent-id", "Invalid Update", Priority.LOW, LocalDateTime.now().plusDays(2));
        assertNull(updatedToDo);
    }

    @Test
    void updateDoneStatus_shouldUpdateDoneField() {
        ToDo toDo = toDoService.get().iterator().next();

        ToDo updatedToDo = toDoService.update(toDo.getId(), true);
        assertTrue(updatedToDo.isDone());

        updatedToDo = toDoService.update(toDo.getId(), false);
        assertFalse(updatedToDo.isDone());
    }
}
  
