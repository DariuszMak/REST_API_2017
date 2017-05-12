package com.restapi2017.resources;

import com.restapi2017.model.ErrorMessage;
import com.restapi2017.model.User;
import com.restapi2017.repository.UserRepository;
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

@RestController
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/users", description = "Operations on users using mysql")
public class UsersResource {

    private UserRepository userDatabase;

    private boolean checkParameter(String name, int min, int max) {
        return (name == null || name.length() < min || name.length() > max );
    }

    private Response badValue(String value) {
        ErrorMessage error = new ErrorMessage(400, "Bad Request", "Wartość: " + value +": zła długość lub pominięta", null);
        return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
    }

    @Autowired
    public UsersResource(UserRepository userDatabase) {
        this.userDatabase = userDatabase;
    }

    @GET
    @ApiOperation(value = "Get users collection", notes = "Get users collection", response = User.class, responseContainer = "LIST")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User collection found")
    })
    public Response list() {
        return Response.status(Response.Status.OK).entity(userDatabase.getUsers()).build();
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
            @ApiResponse(code = 409, message = "Conflict"),
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

        if(checkParameter(user.getFirstName(),1,15))
            return badValue("firstName");
        if(checkParameter(user.getLastName(),1,30))
            return badValue("lastName");
        if(checkParameter(user.getAddress(),1,30))
            return badValue("address");
        if(checkParameter(user.getCity(),1,15))
            return badValue("city");

        if (!user.getPesel().matches("[0-9]{11}")) {
            ErrorMessage error = new ErrorMessage(400,"Bad Request", "Wartość 'pesel' musi zawierać 11 cyfr", null);
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        User userByPesel = userDatabase.getUserByPesel(user.getPesel());

        if(userByPesel == null) {
            User createdUser = userDatabase.createUser(dbUser);
            return Response.created(URI.create("/users/" + createdUser.getId())).entity(createdUser).build();
        } else {
            ErrorMessage error = new ErrorMessage(409, "Conflict", "Użytkownik z takim numerem PESEL już istnieje", "/users/" + userByPesel.getId());
            return Response.status(Response.Status.CONFLICT).entity(error).build();
        }

    }

    @PUT
    @ApiOperation(value = "Update user", notes = "Update user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User edited"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 409, message = "Conflict"),
    })
    public Response updateUser(User user){

        if(checkParameter(user.getFirstName(),1,15))
            return badValue("firstName");
        if(checkParameter(user.getLastName(),1,30))
            return badValue("lastName");
        if(checkParameter(user.getAddress(),1,30))
            return badValue("address");
        if(checkParameter(user.getCity(),1,15))
            return badValue("city");

        if (!user.getPesel().matches("[0-9]{11}")) {
            ErrorMessage error = new ErrorMessage(400,"Bad Request", "Wartość 'pesel' musi zawierać 11 cyfr", null);
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        User userByPesel = userDatabase.getUserByPesel(user.getPesel());

        if(userByPesel != null) {
            ErrorMessage error = new ErrorMessage(409, "Conflict", "Użytkownik z takim numerem PESEL już istnieje", "/users/" + userByPesel.getId());
            return Response.status(Response.Status.CONFLICT).entity(error).build();
        }

        User update = userDatabase.getUser(user.getId());

        if(update == null) {
            ErrorMessage error = new ErrorMessage(404, "Not Found", "Użytkownik z podanym ID nie istnieje w bazie", null);
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        } else {
            User updatedUser = userDatabase.updateUser(user);
            return Response.status(Response.Status.OK).entity(updatedUser).build();
        }
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
