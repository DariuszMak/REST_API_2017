package com.restapi2017.model;

public class User {

    private String id;

    private String firstName;

    private String lastName;

    private String pesel;

    private String address;

    private String city;

    public User() {
    }

    public User(String id, String firstName, String lastName, String pesel, String address, String city) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.address = address;
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPesel() {
        return pesel;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

}
