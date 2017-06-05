package com.restapi2017.repository;

import com.restapi2017.database.DatabaseConfiguration;
import com.restapi2017.entity.BookEntity;
import com.restapi2017.model.Book;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class BookRepository {

    private DatabaseConfiguration databaseConfiguration;

    @Autowired
    public BookRepository(DatabaseConfiguration databaseConfiguration) {
        this.databaseConfiguration = databaseConfiguration;
    }

    public Collection<Book> getBooks() {
        Query query = databaseConfiguration.getEntityManager().createNamedQuery("books.findAll");
        List<BookEntity> resultList = query.getResultList();

        List<Book> list = Collections.emptyList();

        if (resultList != null && !resultList.isEmpty()) {
            list = Lists.newArrayListWithCapacity(resultList.size());

            for (BookEntity book : resultList) {
                list.add(buildBookResponse(book));
            }
        }

        return list;
    }

    public Book getBook(String sid) {
        Long id = null;

        try {
            id = Long.valueOf(sid);
        } catch (NumberFormatException e) {
            return null;
        }

        BookEntity bookEntity = databaseConfiguration.getEntityManager()
                .find(BookEntity.class, id);

        if (bookEntity != null) {
            return buildBookResponse(bookEntity);
        }

        return null;
    }

    public Book createBook(final Book book) {
        BookEntity entity = buildBookEntity(book);

        databaseConfiguration.transaction(entity);

        return new Book(String.valueOf(entity.getId()), entity.getTitle(), entity.getAuthors(), entity.getDescription(),
                entity.getPrice());
    }

    public Book updateBook(Book book, String bookId) {
        Long id = null;

        try {
            id = Long.valueOf(bookId);
        } catch (NumberFormatException e) {
            return null;
        }

        BookEntity bookEntity = databaseConfiguration.getEntityManager()
                .find(BookEntity.class, id);

        if (bookEntity != null) {
            bookEntity.setTitle(book.getTitle());
            bookEntity.setAuthors(book.getAuthors());
            bookEntity.setDescription(book.getDescription());
            bookEntity.setPrice(book.getPrice());


            databaseConfiguration.transaction(bookEntity);

        }

        return buildBookResponse(bookEntity);
    }

    public void deleteBook(String sid) {
        Long id = null;

        try {
            id = Long.valueOf(sid);
        } catch (NumberFormatException e) {
            return;
        }

        BookEntity bookEntity = databaseConfiguration.getEntityManager()
                .find(BookEntity.class, id);

        if (bookEntity != null) {
            databaseConfiguration.removeTransaction(bookEntity);
        }
    }

    private Book buildBookResponse(BookEntity bookEntity) {
        return new Book(bookEntity.getId().toString(), bookEntity.getTitle(), bookEntity.getAuthors(), bookEntity.getDescription(),
                bookEntity.getPrice());
    }

    private BookEntity buildBookEntity(Book book) {
        return new BookEntity(book.getTitle(), book.getAuthors(), book.getDescription(), book.getPrice());
    }

}
