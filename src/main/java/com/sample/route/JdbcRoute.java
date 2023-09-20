package com.sample.route;

import com.sample.model.User;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class JdbcRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("undertow")
                .bindingMode(RestBindingMode.json)
                .contextPath("/api")
                .port(8088)
                .dataFormatProperty("prettyPrint", "true");

        rest("/users")
                .post().type(User.class).outType(User.class).to("direct:insertUser")
                .get("/").outType(User[].class).to("direct:selectAllUsers")
                .get("/{id}").outType(User.class).to("direct:selectUserById")
                .put().type(User.class).to("direct:updateUser")
                .delete("/{id}").to("direct:deleteUser");

        from("direct:insertUser")
                .log("Inserting User: ${body}")
                .to("sql:INSERT INTO users (first_name, last_name, email) VALUES (:#${body.first_name}, :#${body.last_name}, :#${body.email})?dataSource=#dataSource")
                .log("User Inserted: ${body}");

        from("direct:selectAllUsers")
                .log("Fetching all users")
                .to("sql:SELECT * FROM users?dataSource=#dataSource")
                .log("All Users Fetched: ${body}");

        from("direct:selectUserById")
                .log("Fetching user with ID: ${header.id}")
                .to("sql:SELECT * FROM users WHERE id = :#${header.id}?dataSource=#dataSource")
                .log("User with ID ${header.id} Fetched: ${body}");

        from("direct:updateUser")
                .log("Updating User with ID: ${body.id}")
                .to("sql:UPDATE users SET first_name=:#${body.first_name}, last_name=:#${body.last_name}, email=:#${body.email} WHERE id = :#${body.id}?dataSource=#dataSource")
                .log("User with ID ${body.id} Updated");

        from("direct:deleteUser")
                .log("Deleting User with ID: ${header.id}")
                .to("sql:DELETE FROM users WHERE id = :#${header.id}?dataSource=#dataSource")
                .log("User with ID ${header.id} Deleted");
    }
}
