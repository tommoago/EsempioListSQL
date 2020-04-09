package com.example.esempiolistsql.database;

public class User {
    private int _id;
    private String name;
    private String surname;
    private String username;

    public User(int _id, String name, String surname, String username) {
        this._id = _id;
        this.name = name;
        this.surname = surname;
        this.username = username;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

