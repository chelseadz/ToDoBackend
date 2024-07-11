package com.encora.ToDoBack;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import com.encora.ToDoBack.controller.ToDoController;
import com.encora.ToDoBack.model.Pagination;
import com.encora.ToDoBack.model.ToDo;
import com.encora.ToDoBack.model.Pagination;

import com.encora.ToDoBack.model.ToDo.Priority;
import com.encora.ToDoBack.service.ToDoService;

class ToDoControllerTest {

    @Mock
    private ToDoService toDoService;

    @InjectMocks
    private ToDoController toDoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void get_shouldReturnAllToDos() {
        List<ToDo> todos = new ArrayList<>();
        when(toDoService.get()).thenReturn(todos);

        Collection<ToDo> result = toDoController.get();

        assertSame(todos, result);
    }

    @Test
    void get_shouldReturnToDoById() {
        String id = "1";
        ToDo todo = new ToDo("Task 1", Priority.HIGH);;
        when(toDoService.get(id)).thenReturn(todo);

        ToDo result = toDoController.get(id);

        assertSame(todo, result);
    }

    @Test
    void get_shouldThrowNotFoundIfToDoDoesNotExist() {
        String id = "1";
        when(toDoService.get(id)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> toDoController.get(id));
    }

    @Test
    void delete_shouldRemoveToDoById() {
        String id = "1";
        ToDo todo = new ToDo("Task 1", Priority.HIGH);;
        when(toDoService.remove(id)).thenReturn(todo);

        assertDoesNotThrow(() -> toDoController.delete(id));
    }

    @Test
    void delete_shouldThrowNotFoundIfToDoDoesNotExist() {
        String id = "1";
        when(toDoService.remove(id)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> toDoController.delete(id));
    }

    @Test
    void create_shouldCreateToDo() {
        String jsonString = "{ \"text\": \"test\", \"priority\": \"LOW\", \"dueDate\": \"2024-07-07T10:15:30\" }";
        try {
			JSONObject jsonObject = new JSONObject(jsonString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        ToDo todo = new ToDo("Task 1", Priority.HIGH);

        when(toDoService.save(anyString(), any(Priority.class), any())).thenReturn(todo);

        ToDo result = toDoController.create(jsonString);

        assertSame(todo, result);
        verify(toDoService).save("test", Priority.LOW, Optional.of(LocalDateTime.parse("2024-07-07T10:15:30")));
    }

    @Test
    void create_shouldCreateToDoWithNoDueDate() throws JSONException {
        String jsonString = "{ \"text\": \"test\", \"priority\": \"LOW\", \"dueDate\": \"\" }";
        JSONObject jsonObject = new JSONObject(jsonString);
        ToDo todo = new ToDo("Task 1", Priority.HIGH);

        when(toDoService.save(anyString(), any(Priority.class), any())).thenReturn(todo);

        ToDo result = toDoController.create(jsonString);

        assertSame(todo, result);
        verify(toDoService).save("test", Priority.LOW, Optional.empty());
    }

    @Test
    void put_shouldUpdateToDo() {
        String id = "1";
        String entity = "{ \"text\": \"updated text\", \"priority\": \"HIGH\", \"dueDate\": \"2024-07-07T10:15:30\" }";
        ToDo todo = new ToDo("Task 1", Priority.HIGH);

        when(toDoService.update(eq(id), any(), any(), any())).thenReturn(todo);

        String result = toDoController.update(id, entity);

        assertEquals(entity, result);
        verify(toDoService).update(eq(id), eq("updated text"), eq(Priority.HIGH), eq(LocalDateTime.parse("2024-07-07T10:15:30")));
    }

    @Test
    void putDone_shouldUpdateToDoDoneStatus() {
        String id = "1";
        String entity = "{ \"done\": true }";
        ToDo todo = new ToDo("Task 1", Priority.HIGH);

        when(toDoService.update(eq(id), eq(true))).thenReturn(todo);

        String result = toDoController.putDone(id, entity);

        assertEquals(entity, result);
        verify(toDoService).update(eq(id), eq(true));
    }

    @Test
    void getCustom_shouldReturnFilteredAndSortedToDos() {
        List<ToDo> todos = new ArrayList<>();
        ToDo todo1 = new ToDo("Task 1", Priority.HIGH);
        ToDo todo2 = new ToDo("Task 2", Priority.MEDIUM);
        todos.add(todo1);
        todos.add(todo2);

        when(toDoService.get()).thenReturn(todos);

        Collection<ToDo> result = toDoController.getCustom(
            1,1, true, true, true, "task", "all", "all" );

        assertEquals(1, result.size());
    } 
}
