package com.restapi2017.database;

import com.restapi2017.model.User;

import java.util.Collection;

public interface UserDatabase {

    User getUser(String id);

    User createUser(User user);

    Collection<User> getUsers();

    User updateUser(User user);

    void deleteUser(String id);

}

