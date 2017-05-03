package com.restapi2017.database;

import com.restapi2017.entity.UserEntity;
import com.restapi2017.model.User;
import jersey.repackaged.com.google.common.collect.Lists;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class MysqlDB implements UserDatabase {

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

    @Override
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

    @Override
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

    @Override
    public User createUser(final User user) {
        UserEntity entity = buildUserEntity(user);

        transaction(entity);

        return new User(String.valueOf(entity.getId()), entity.getFirstName(), entity.getLastName(), entity.getPesel(),
                entity.getAddress(), entity.getCity());
    }

    @Override
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

    @Override
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

}
