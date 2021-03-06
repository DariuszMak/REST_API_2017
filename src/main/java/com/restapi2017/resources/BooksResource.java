package com.restapi2017.resources;

import com.restapi2017.model.ErrorMessage;
import com.restapi2017.model.Book;
import com.restapi2017.repository.BookRepository;
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
@Path("/books")
@Api(value = "/books", description = "Operations on books using mysql")
public class BooksResource {

    private BookRepository bookDatabase;

    private boolean checkParameter(String name, int min, int max) {
        return (name == null || name.length() < min || name.length() > max);
    }

    private Response badValue(String value) {
        ErrorMessage error = new ErrorMessage(400, "Bad Request", "Wartość: " + value + ": zła długość lub pominięta", null);
        return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
    }

    @Autowired
    public BooksResource(BookRepository bookDatabase) {
        this.bookDatabase = bookDatabase;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get books collection", notes = "Get books collection", response = Book.class, responseContainer = "LIST")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book collection found")
    })
    public Response list() {
        return Response.status(Response.Status.OK).entity(bookDatabase.getBooks()).build();
    }

    @GET
    @Path("/{bookId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get book by id", notes = "[note]Get book by id", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book found"),
            @ApiResponse(code = 404, message = "Book not found")
    })
    public Response getBook(@PathParam("bookId") String bookId) {
        Book book = bookDatabase.getBook(bookId);

        if (book == null) {
            ErrorMessage error = new ErrorMessage(404, "Not Found", "Książka nie została znaleziona", "/books/" + bookId);
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

        return Response.status(Response.Status.OK).entity(book).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create book", notes = "Create book", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Book created"),
            @ApiResponse(code = 400, message = "Bad request"),
    })
    public Response createBook(@Valid @NotNull Book book) {
        Book dbBook = new Book(
                "",
                book.getTitle(),
                book.getAuthors(),
                book.getDescription(),
                book.getPrice()

        );

        if (checkParameter(book.getTitle(), 1, 40))
            return badValue("title");
        if (checkParameter(book.getAuthors(), 1, 30))
            return badValue("authors");
        if (checkParameter(book.getDescription(), 1, 100))
            return badValue("description");

        Book createdBook = bookDatabase.createBook(dbBook);

        return Response.created(URI.create("/books/" + createdBook.getId())).entity(createdBook).build();

    }

    @PUT
    @Path("/{bookId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update book", notes = "Update book", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book edited"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Book not found")
    })
    public Response updateBook(@PathParam("bookId") String bookId, Book book) {

        if (checkParameter(book.getTitle(), 1, 40))
            return badValue("title");
        if (checkParameter(book.getAuthors(), 1, 30))
            return badValue("authors");
        if (checkParameter(book.getDescription(), 1, 100))
            return badValue("description");

        Book update = bookDatabase.getBook(bookId);

        if (update == null) {
            ErrorMessage error = new ErrorMessage(404, "Not Found", "Książka z podanym ID nie istnieje w bazie", null);
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        } else {
            Book updatedBook = bookDatabase.updateBook(book, bookId);
            return Response.status(Response.Status.OK).entity(updatedBook).build();
        }

    }

    @DELETE
    @Path("/{bookId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Delete book", notes = "Delete book")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book deleted"),
            @ApiResponse(code = 404, message = "Book not found")
    })
    public Response deleteBook(@PathParam("bookId") String bookId) {
        Book book = bookDatabase.getBook(bookId);

        if (book == null) {
            ErrorMessage error = new ErrorMessage(404, "Not Found", "Książka nie została znaleziona", "/books/" + bookId);
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

        bookDatabase.deleteBook(bookId);
        return Response.status(Response.Status.OK).entity(book).build();

    }

}
