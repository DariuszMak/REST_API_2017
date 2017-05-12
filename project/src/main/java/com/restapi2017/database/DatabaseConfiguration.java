package com.restapi2017.database;

import com.restapi2017.entity.AbstractEntity;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

@Component
public class DatabaseConfiguration {

    public DatabaseConfiguration() {
    }

    private static final String HOST = "localhost";
    private static final int PORT = 3306;
    private static final String DATABASE = "restapi2017";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "";

    private static EntityManager entityManager;

    public EntityManager getEntityManager() {
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

    public void removeTransaction(AbstractEntity entity){
        try {
            getEntityManager().getTransaction().begin();

            getEntityManager().remove(entity);

            getEntityManager().getTransaction().commit();
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
    }

    public void transaction(AbstractEntity entity){
        try {
            getEntityManager().getTransaction().begin();

            getEntityManager().persist(entity);

            getEntityManager().getTransaction().commit();
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
    }

}
