package com.example.hospitaldeliveryinterface.model;

public class Employee {
    private String firstName;
    private String lastName;
    private int employeeId;
    private String email;


    public Employee(String firstName, String lastName, int employeeId){
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeId = employeeId;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setEmployeeId() {
        this.employeeId = employeeId;
    }

    public void setEmail(String email) {
        this.email = email;
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
    public String getEmail() {
        return email;
    }
}
