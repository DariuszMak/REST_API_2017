package com.restapi2017.database;

import com.restapi2017.entity.BookEntity;
import com.restapi2017.entity.UserEntity;
import com.restapi2017.model.Book;
import com.restapi2017.model.User;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

@Component
public class MysqlDB {

    private static final String HOST = "localhost";
    private static final int PORT = 3306;
    private static final String DATABASE = "restapi2017";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "";

    private static EntityManager entityManager;

    public static EntityManager getEntityManager() {
        if (entityManager == null) {
            String dbUrl = "jdbc:mysql://" + HOST + ':' + PORT + "/" + DATABASE+"?serverTimezone=UTC";

            Map<String, String> properties = new HashMap<String, String>();

            properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
            properties.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");
            properties.put("hibernate.connection.url", dbUrl);
            properties.put("hibernate.connection.username", USER_NAME);
            properties.put("hibernate.connection.password", PASSWORD);
            properties.put("hibernate.show_sql", "true");
            properties.put("hibernate.format_sql", "true");

            properties.put("hibernate.connection.useUnicode", "true");
            properties.put("hibernate.connection.characterEncoding", "utf8");

            properties.put("hibernate.temp.use_jdbc_metadata_defaults", "false"); //PERFORMANCE TIP!
            properties.put("hibernate.hbm2ddl.auto", "update"); //update schema for entities (create tables if not exists)

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("myUnit", properties);
            entityManager = emf.createEntityManager();
        }

        return entityManager;
    }

    public Collection<User> getUsers() {
        Query query = getEntityManager().createNamedQuery("users.findAll");
        List<UserEntity> resultList = query.getResultList();

        List<User> list = Collections.emptyList();

        if (resultList != null && !resultList.isEmpty()) {
            list = Lists.newArrayListWithCapacity(resultList.size());

            for (UserEntity user : resultList) {
                list.add(buildUserResponse(user));
            }
        }

        return list;
    }

    public User getUser(String sid) {
        Long id = null;

        try {
            id = Long.valueOf(sid);
        } catch (NumberFormatException e) {
            return null;
        }

        UserEntity userEntity = getEntityManager()
                .find(UserEntity.class, id);

        if (userEntity != null) {
            return buildUserResponse(userEntity);
        }

        return null;
    }

    public User getUserByPesel(String pesel) {
        List<UserEntity> resultList = getEntityManager().createQuery("SELECT u FROM UserEntity u where u.pesel = :pesel")
                .setParameter("pesel", pesel)
                .getResultList();

        List<User> list = Collections.emptyList();

        if (resultList != null && !resultList.isEmpty()) {
            list = Lists.newArrayListWithCapacity(resultList.size());

            for (UserEntity user : resultList) {
                list.add(buildUserResponse(user));
            }

            User user = list.get(0);
            return user;
        }

        return null;

    }

    public User createUser(final User user) {
        UserEntity entity = buildUserEntity(user);

        transaction(entity);

        return new User(String.valueOf(entity.getId()), entity.getFirstName(), entity.getLastName(), entity.getPesel(),
                entity.getAddress(), entity.getCity());
    }

    public User updateUser(User user) {
        Long id = null;

        try {
            id = Long.valueOf(user.getId());
        } catch (NumberFormatException e) {
            return null;
        }

        UserEntity userEntity = getEntityManager()
                .find(UserEntity.class, id);

        if (userEntity != null) {
            userEntity.setFirstName(user.getFirstName());
            userEntity.setLastName(user.getLastName());
            userEntity.setPesel(user.getPesel());
            userEntity.setAddress(user.getAddress());
            userEntity.setCity(user.getCity());

            transaction(userEntity);

        }

        return buildUserResponse(userEntity);
    }

    public void deleteUser(String sid) {
        Long id = null;

        try {
            id = Long.valueOf(sid);
        } catch (NumberFormatException e) {
            return ;
        }

        UserEntity userEntity = getEntityManager()
                .find(UserEntity.class, id);

        if (userEntity != null) {
            try {
                getEntityManager().getTransaction().begin();

                // Operations that modify the database should come here.
                getEntityManager().remove(userEntity);

                getEntityManager().getTransaction().commit();
            } finally {
                if (getEntityManager().getTransaction().isActive()) {
                    getEntityManager().getTransaction().rollback();
                }
            }

        }

    }

    private User buildUserResponse(UserEntity userEntity) {
        return new User(userEntity.getId().toString(), userEntity.getFirstName(), userEntity.getLastName(), userEntity.getPesel(),
                userEntity.getAddress(), userEntity.getCity());
    }

    private UserEntity buildUserEntity(User user) {
        return new UserEntity(user.getFirstName(), user.getLastName(), user.getPesel(), user.getAddress(), user.getCity());
    }

    private void transaction(UserEntity userEntity){
        try {
            getEntityManager().getTransaction().begin();

            // Operations that modify the database should come here.
            getEntityManager().persist(userEntity);

            getEntityManager().getTransaction().commit();
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
    }





    public Collection<Book> getBooks() {
        Query query = getEntityManager().createNamedQuery("books.findAll");
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

        BookEntity bookEntity = getEntityManager()
                .find(BookEntity.class, id);

        if (bookEntity != null) {
            return buildBookResponse(bookEntity);
        }

        return null;
    }

    public Book createBook(final Book book) {
        BookEntity entity = buildBookEntity(book);

        transaction(entity);

        return new Book(String.valueOf(entity.getId()), entity.getTitle(), entity.getAuthors(), entity.getDescription(),
                entity.getPrice());
    }

    public Book updateBook(Book book) {
        Long id = null;

        try {
            id = Long.valueOf(book.getId());
        } catch (NumberFormatException e) {
            return null;
        }

        BookEntity bookEntity = getEntityManager()
                .find(BookEntity.class, id);

        if (bookEntity != null) {
            bookEntity.setTitle(book.getTitle());
            bookEntity.setAuthors(book.getAuthors());
            bookEntity.setDescription(book.getDescription());
            bookEntity.setPrice(book.getPrice());


            transaction(bookEntity);

        }

        return buildBookResponse(bookEntity);
    }

    public void deleteBook(String sid) {
        Long id = null;

        try {
            id = Long.valueOf(sid);
        } catch (NumberFormatException e) {
            return ;
        }

        BookEntity bookEntity = getEntityManager()
                .find(BookEntity.class, id);

        if (bookEntity != null) {
            try {
                getEntityManager().getTransaction().begin();

                // Operations that modify the database should come here.
                getEntityManager().remove(bookEntity);

                getEntityManager().getTransaction().commit();
            } finally {
                if (getEntityManager().getTransaction().isActive()) {
                    getEntityManager().getTransaction().rollback();
                }
            }

        }

    }

    private Book buildBookResponse(BookEntity bookEntity) {
        return new Book(bookEntity.getId().toString(), bookEntity.getTitle(), bookEntity.getAuthors(), bookEntity.getDescription(),
                bookEntity.getPrice());
    }

    private BookEntity buildBookEntity(Book book) {
        return new BookEntity(book.getTitle(), book.getAuthors(), book.getDescription(), book.getPrice());
    }

    private void transaction(BookEntity bookEntity){
        try {
            getEntityManager().getTransaction().begin();

            // Operations that modify the database should come here.
            getEntityManager().persist(bookEntity);

            getEntityManager().getTransaction().commit();
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
    }


}
