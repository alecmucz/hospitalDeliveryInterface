package com.example.hospitaldeliveryinterface.model;

public class Employee {
    private String firstName;
    private String lastName;
    private int employeeId;

    public Employee(String firstName, String lastName, int employeeId){
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeId = employeeId;
    }
    public void setFirstName(String fristName){
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setEmployeeId() {
        this.employeeId = employeeId;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public int getEmployeeId() {
        return employeeId;
    }

}
