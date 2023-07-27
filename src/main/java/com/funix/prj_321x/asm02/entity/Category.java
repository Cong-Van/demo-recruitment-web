package com.funix.prj_321x.asm02.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "number_choose")
    private int numberChoose;

    public Category() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberChoose() {
        return numberChoose;
    }

    public void setNumberChoose(int numberChoose) {
        this.numberChoose = numberChoose;
    }

    @Override
    public String toString() {
        return "com.funix.prj_321x.asm02.entity.Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", numberChoose=" + numberChoose +
                '}';
    }
}
