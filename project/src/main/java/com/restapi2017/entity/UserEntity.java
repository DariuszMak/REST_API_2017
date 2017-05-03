package com.restapi2017.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = "users.findAll", query = "SELECT u FROM UserEntity u")
})
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "firstName")
    @NotNull @Size(min = 1, max = 15)
    private String firstName;

    @Column(name = "lastName")
    @NotNull @Size(min = 1, max = 30)
    private String lastName;

    @Column(name = "pesel")
    @NotNull @Pattern(regexp ="[0-9]{11}")
    private String pesel;

    @Column(name = "address")
    @NotNull @Size(min = 1, max = 30)
    private String address;

    @Column(name = "city")
    @NotNull @Size(min = 1, max = 15)
    private String city;

    public UserEntity() {
    }

    public UserEntity(String firstName, String lastName, String pesel, String address, String city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.address = address;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", pesel='" + pesel + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                '}';
    }




}
