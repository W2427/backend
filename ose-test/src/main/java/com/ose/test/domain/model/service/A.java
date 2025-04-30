package com.ose.test.domain.model.service;

public class A {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void sayA() {
        System.out.println("sayA");
    }

    @Override
    public String toString() {
        return "A [name=" + name + "]";
    }
}
