package com.encora.ToDoBack.model;

public class Pagination {
    
  private int pageSize;
  private int pageNumber;

  public Pagination(int size, int number){
    this.pageSize = size;
    this.pageNumber = number;
  }

  public int getPageSize(){
    return this.pageSize;
  }

  public int getPageNumber(){
    return this.pageNumber;
  }

  public void setPageSize(int size){
    this.pageSize = size;
  }

  public void setPageNumber(int number){
    this.pageNumber = number;
  }

}
