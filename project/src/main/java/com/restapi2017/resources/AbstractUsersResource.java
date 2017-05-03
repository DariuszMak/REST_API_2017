package com.restapi2017.resources;

import com.restapi2017.database.UserDatabase;
import com.restapi2017.exceptions.UserException;
import com.restapi2017.model.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Collection;

@RestController
public abstract class AbstractUsersResource {

    protected abstract UserDatabase getDatabase();

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ApiOperation(value = "Get users collection", notes = "Get users collection", response = User.class, responseContainer = "LIST")
    public Collection<User> list() {
        return getDatabase().getUsers();
    }


    @RequestMapping(value="/{userId}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ApiOperation(value = "Get user by id", notes = "[note]Get user by id", response = User.class)
    public User getUser(@PathVariable("userId") String userId) throws Exception {
        User user = getDatabase().getUser(userId);

        if (userId.equals("db")) {
            throw new Exception("Database error");
        }

        if (user == null) {
            throw new UserException("User not found", "Użytkownik nie został znaleziony");
        }

        return user;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create user", notes = "Create user", response = User.class)
    public ResponseEntity createUser(@RequestBody User user, HttpServletRequest request) {
        User dbUser = new User(
                "",
                user.getFirstName(),
                user.getLastName(),
                user.getPesel(),
                user.getAddress(),
                user.getCity()
        );

        User createdUser = getDatabase().createUser(dbUser);

        return ResponseEntity.created(URI.create(request.getRequestURL().toString() + "/" + createdUser.getId())).body(createdUser);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update user", notes = "Update user", response = User.class)
    public User updateUser(@RequestBody User user){
        User updatedUser = getDatabase().updateUser(user);

        return updatedUser;
    }

    @RequestMapping(value="/{userId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete user", notes = "Delete user")
    public void deleteUser(@PathVariable("userId") String userId){
        User user = getDatabase().getUser(userId);

        if (user == null) {
            throw new UserException("User not found", "Użytkownik nie został znaleziony");
        }

        getDatabase().deleteUser(userId);

    }

}
