package com.encora.ToDoBack.model;
import com.encora.ToDoBack.model.Pagination;

public class ToDoRequest {

    private Pagination pagination;
    private boolean sortByDone;
    private boolean sortByDate;
    private boolean sortByPriority;
    private String nameFilter;
    private String priorityFilter;
    private String doneFilter;

    // Constructor
    public ToDoRequest(int pageSize, int pageNumber, boolean sortByDone, boolean sortByDate, boolean sortByPriority, String nameFilter, String priorityFilter, String doneFilter) {
        this.pagination = new Pagination(pageSize, pageNumber);
        this.sortByDone = sortByDone;
        this.sortByDate = sortByDate;
        this.sortByPriority = sortByPriority;
        this.nameFilter = nameFilter;
        this.priorityFilter = priorityFilter;
        this.doneFilter = doneFilter;
    }

    // Getter and Setter for pagination
    public Pagination getPagination(){
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public boolean isSortByDone() {
        return sortByDone;
    }

    public void setSortByDone(boolean sortByDone) {
        this.sortByDone = sortByDone;
    }

    public boolean isSortByDate() {
        return sortByDate;
    }

    public void setSortByDate(boolean sortByDate) {
        this.sortByDate = sortByDate;
    }

    public boolean isSortByPriority() {
        return sortByPriority;
    }

    public void setSortByPriority(boolean sortByPriority) {
        this.sortByPriority = sortByPriority;
    }

    public String getNameFilter() {
        return nameFilter;
    }

    public void setNameFilter(String nameFilter) {
        this.nameFilter = nameFilter;
    }

    public String getPriorityFilter() {
        return priorityFilter;
    }

    public void setPriorityFilter(String priorityFilter) {
        this.priorityFilter = priorityFilter;
    }

    public String getDoneFilter() {
        return doneFilter;
    }

    public void setDoneFilter(String doneFilter) {
        this.doneFilter = doneFilter;
    }
}

