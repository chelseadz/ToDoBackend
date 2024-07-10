package com.encora.ToDoBack;

import org.junit.jupiter.api.Test;

import com.encora.ToDoBack.model.ToDo;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ToDoTest {

    @Test
    void constructor_shouldInitializeFieldsProperly() {
        String text = "Test task";
        ToDo.Priority priority = ToDo.Priority.HIGH;
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);

        ToDo toDo = new ToDo(text, priority, dueDate);

        assertEquals(text, toDo.getText());
        assertEquals(priority, toDo.getPriority());
        assertEquals(dueDate, toDo.getDueDate());
        assertNotNull(toDo.getId());
        assertNotNull(toDo.getCreationDate());
        assertFalse(toDo.isDone());
    }

    @Test
    void constructor_shouldInitializeWithoutDueDate() {
        String text = "Test task";
        ToDo.Priority priority = ToDo.Priority.HIGH;

        ToDo toDo = new ToDo(text, priority);

        assertEquals(text, toDo.getText());
        assertEquals(priority, toDo.getPriority());
        assertNull(toDo.getDueDate());
        assertNotNull(toDo.getId());
        assertNotNull(toDo.getCreationDate());
        assertFalse(toDo.isDone());
    }

    @Test
    void constructor_shouldThrowExceptionForInvalidText() {
        assertThrows(IllegalArgumentException.class, () -> new ToDo(null, ToDo.Priority.HIGH));
        assertThrows(IllegalArgumentException.class, () -> new ToDo("", ToDo.Priority.HIGH));
        assertThrows(IllegalArgumentException.class, () -> new ToDo("a".repeat(121), ToDo.Priority.HIGH));
    }

    @Test
    void setText_shouldUpdateText() {
        ToDo toDo = new ToDo("Initial text", ToDo.Priority.MEDIUM);
        String newText = "Updated text";

        toDo.setText(newText);

        assertEquals(newText, toDo.getText());
    }

    @Test
    void setText_shouldThrowExceptionForInvalidText() {
        ToDo toDo = new ToDo("Valid text", ToDo.Priority.MEDIUM);

        assertThrows(IllegalArgumentException.class, () -> toDo.setText(null));
        assertThrows(IllegalArgumentException.class, () -> toDo.setText("a".repeat(121)));
    }

    @Test
    void setDueDate_shouldUpdateDueDate() {
        ToDo toDo = new ToDo("Test task", ToDo.Priority.LOW);
        LocalDateTime newDueDate = LocalDateTime.now().plusDays(2);

        toDo.setDueDate(newDueDate);

        assertEquals(newDueDate, toDo.getDueDate());
    }

    @Test
    void setPriority_shouldUpdatePriority() {
        ToDo toDo = new ToDo("Test task", ToDo.Priority.MEDIUM);
        ToDo.Priority newPriority = ToDo.Priority.LOW;

        toDo.setPriority(newPriority);

        assertEquals(newPriority, toDo.getPriority());
    }

    @Test
    void setDone_shouldUpdateDoneStatus() {
        ToDo toDo = new ToDo("Test task", ToDo.Priority.MEDIUM);

        toDo.setDone(true);

        assertTrue(toDo.isDone());

        toDo.setDone(false);

        assertFalse(toDo.isDone());
    }

    @Test
    void setDoneDate_shouldUpdateDoneDate() {
        ToDo toDo = new ToDo("Test task", ToDo.Priority.MEDIUM);
        LocalDateTime doneDate = LocalDateTime.now();

        toDo.setDoneDate(doneDate);

        assertEquals(doneDate, toDo.getDoneDate());
    }

    @Test
    void setCreationDate_shouldUpdateCreationDate() {
        ToDo toDo = new ToDo("Test task", ToDo.Priority.LOW);
        LocalDateTime creationDate = LocalDateTime.now().minusDays(1);

        toDo.setCreationDate(creationDate);

        assertEquals(creationDate, toDo.getCreationDate());
    }

    @Test
    void forceDueDate_shouldReturnDefaultDateWhenDueDateIsNull() {
        ToDo toDo = new ToDo("Test task", ToDo.Priority.MEDIUM);

        LocalDateTime expectedDummyDate = LocalDateTime.of(2900, 1, 1, 1, 1, 1);

        assertEquals(expectedDummyDate, toDo.forceDueDate());
    }

    @Test
    void forceDueDate_shouldReturnDueDateWhenDueDateIsNotNull() {
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);
        ToDo toDo = new ToDo("Test task", ToDo.Priority.MEDIUM, dueDate);

        assertEquals(dueDate, toDo.forceDueDate());
    }
}
