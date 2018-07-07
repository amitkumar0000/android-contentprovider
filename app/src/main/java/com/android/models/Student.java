package com.android.models;

public class Student {
    String name;
    int rollno;
    OperationType operationType;


    public Student(OperationType type, String name, int rollno) {
        this.name = name;
        this.rollno = rollno;
        this.operationType = type;
    }


    public String getName() {
        return name;
    }


    public int getRollno() {
        return rollno;
    }

    public OperationType getOperationType() {
        return operationType;
    }
}
