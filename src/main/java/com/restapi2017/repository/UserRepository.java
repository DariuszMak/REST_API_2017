package com.restapi2017.repository;

import com.restapi2017.database.DatabaseConfiguration;
import com.restapi2017.entity.UserEntity;
import com.restapi2017.model.User;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class UserRepository {

    private DatabaseConfiguration databaseConfiguration;

    @Autowired
    public UserRepository(DatabaseConfiguration databaseConfiguration) {
        this.databaseConfiguration = databaseConfiguration;
    }

    public Collection<User> getUsers() {
        Query query = databaseConfiguration.getEntityManager().createNamedQuery("users.findAll");
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

    public Collection<User> getUsersByCity(String city) {
        List<UserEntity> resultList = databaseConfiguration.getEntityManager().createQuery("SELECT u FROM UserEntity u where u.city = :city")
                .setParameter("city", city)
                .getResultList();

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

        UserEntity userEntity = databaseConfiguration.getEntityManager()
                .find(UserEntity.class, id);

        if (userEntity != null) {
            return buildUserResponse(userEntity);
        }

        return null;
    }

    public User getUserByPesel(String pesel) {
        List<UserEntity> resultList = databaseConfiguration.getEntityManager().createQuery("SELECT u FROM UserEntity u where u.pesel = :pesel")
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

        databaseConfiguration.transaction(entity);

        return new User(String.valueOf(entity.getId()), entity.getFirstName(), entity.getLastName(), entity.getPesel(),
                entity.getAddress(), entity.getCity());
    }

    public User updateUser(User user, String userId) {
        Long id = null;

        try {
            id = Long.valueOf(userId);
        } catch (NumberFormatException e) {
            return null;
        }

        UserEntity userEntity = databaseConfiguration.getEntityManager()
                .find(UserEntity.class, id);

        if (userEntity != null) {
            userEntity.setFirstName(user.getFirstName());
            userEntity.setLastName(user.getLastName());
            userEntity.setPesel(user.getPesel());
            userEntity.setAddress(user.getAddress());
            userEntity.setCity(user.getCity());

            databaseConfiguration.transaction(userEntity);

        }

        return buildUserResponse(userEntity);
    }

    public void deleteUser(String sid) {
        Long id = null;

        try {
            id = Long.valueOf(sid);
        } catch (NumberFormatException e) {
            return;
        }

        UserEntity userEntity = databaseConfiguration.getEntityManager()
                .find(UserEntity.class, id);

        if (userEntity != null) {
            databaseConfiguration.removeTransaction(userEntity);
        }

    }

    private User buildUserResponse(UserEntity userEntity) {
        return new User(userEntity.getId().toString(), userEntity.getFirstName(), userEntity.getLastName(), userEntity.getPesel(),
                userEntity.getAddress(), userEntity.getCity());
    }

    private UserEntity buildUserEntity(User user) {
        return new UserEntity(user.getFirstName(), user.getLastName(), user.getPesel(), user.getAddress(), user.getCity());
    }

}
