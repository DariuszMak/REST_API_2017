package com.restapi2017.resources;

import com.restapi2017.database.MysqlDB;
import com.restapi2017.model.ErrorMessage;
import com.restapi2017.model.Book;
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
@Path("/books")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/books", description = "Operations on books using mysql")
public class BooksResource {

    private MysqlDB bookDatabase;

    @Autowired
    public BooksResource(MysqlDB bookDatabase) {
        this.bookDatabase = bookDatabase;
    }

    @GET
    @ApiOperation(value = "Get books collection", notes = "Get books collection", response = Book.class, responseContainer = "LIST")
    public Collection<Book> list() {
        return bookDatabase.getBooks();
    }

    @GET
    @Path("/{bookId}")
    @ApiOperation(value = "Get book by id", notes = "[note]Get book by id", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book found"),
            @ApiResponse(code = 404, message = "Book not found")
    })
    public Response getBook(@PathParam("bookId") String bookId) {
        Book book = bookDatabase.getBook(bookId);

        if (book == null) {
            ErrorMessage error = new ErrorMessage(404, "Not Found", "Użytkownik nie został znaleziony", "/books/" + bookId);
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

        return Response.status(Response.Status.OK).entity(book).build();
    }

    @POST
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

       /* if (!book.getPesel().matches("[0-9]{11}")) {
            ErrorMessage error = new ErrorMessage(400,"Bad Request", "Wartość 'pesel' musi zawierać 11 cyfr", null);
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }*/

        Book createdBook = bookDatabase.createBook(dbBook);

        return Response.created(URI.create("/books/" + createdBook.getId())).entity(createdBook).build();

    }

    @PUT
    @ApiOperation(value = "Update book", notes = "Update book", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book edited"),
    })
    public Book updateBook(Book book){

        Book updatedBook = bookDatabase.updateBook(book);

        return updatedBook;
    }

    @DELETE
    @Path("/{bookId}")
    @ApiOperation(value = "Delete book", notes = "Delete book")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book deleted"),
            @ApiResponse(code = 404, message = "Book not found")
    })
    public Response deleteBook(@PathParam("bookId") String bookId){
        Book book = bookDatabase.getBook(bookId);

        if (book == null) {
            ErrorMessage error = new ErrorMessage(404, "Not Found", "Użytkownik nie został znaleziony", "/books/" + bookId);
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

        bookDatabase.deleteBook(bookId);
        return Response.status(Response.Status.OK).entity(book).build();

    }

}
