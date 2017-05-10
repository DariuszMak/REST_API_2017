package com.restapi2017.resources;

import com.restapi2017.database.MysqlDB;
import com.restapi2017.model.ErrorMessage;
import com.restapi2017.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Collection;

@RestController
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/users", description = "Operations on users using mysql")
public class UsersResource {

    private MysqlDB userDatabase;

    @Autowired
    public UsersResource(MysqlDB userDatabase) {
        this.userDatabase = userDatabase;
    }

    @GET
    @ApiOperation(value = "Get users collection", notes = "Get users collection", response = User.class, responseContainer = "LIST")
    public Collection<User> list() {
        return userDatabase.getUsers();
    }

    @GET
    @Path("/{userId}")
    @ApiOperation(value = "Get user by id", notes = "[note]Get user by id", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User found"),
            @ApiResponse(code = 404, message = "User not found")
    })
    public Response getUser(@PathParam("userId") String userId) {
        User user = userDatabase.getUser(userId);

        if (user == null) {
            ErrorMessage error = new ErrorMessage(404, "Not Found", "Użytkownik nie został znaleziony", "/users/" + userId);
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

        return Response.status(Response.Status.OK).entity(user).build();
    }

    @POST
    @ApiOperation(value = "Create user", notes = "Create user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created"),
            @ApiResponse(code = 400, message = "Bad request"),
    })
    public Response createUser(@Valid @NotNull User user) {
        User dbUser = new User(
                "",
                user.getFirstName(),
                user.getLastName(),
                user.getPesel(),
                user.getAddress(),
                user.getCity()
        );

        if (!user.getPesel().matches("[0-9]{11}")) {
            ErrorMessage error = new ErrorMessage(400,"Bad Request", "Wartość 'pesel' musi zawierać 11 cyfr", null);
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        User createdUser = userDatabase.createUser(dbUser);

        return Response.created(URI.create("/users/" + createdUser.getId())).entity(createdUser).build();

    }

    @PUT
    @ApiOperation(value = "Update user", notes = "Update user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User edited"),
    })
    public User updateUser(User user){

        User updatedUser = userDatabase.updateUser(user);

        return updatedUser;
    }

    @DELETE
    @Path("/{userId}")
    @ApiOperation(value = "Delete user", notes = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User deleted"),
            @ApiResponse(code = 404, message = "User not found")
    })
    public Response deleteUser(@PathParam("userId") String userId){
        User user = userDatabase.getUser(userId);

        if (user == null) {
            ErrorMessage error = new ErrorMessage(404, "Not Found", "Użytkownik nie został znaleziony", "/users/" + userId);
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

        userDatabase.deleteUser(userId);
        return Response.status(Response.Status.OK).entity(user).build();

    }

}
