package com.restapi2017.resources;

import com.restapi2017.database.MysqlDB;
import com.restapi2017.database.UserDatabase;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Api(value = "/users", description = "Operations on users using mysql")
@Component
public class MysqlUsersResource extends AbstractUsersResource {

    private static final UserDatabase database = new MysqlDB();

    @Override
    protected UserDatabase getDatabase() {
        return database;
    }

}
