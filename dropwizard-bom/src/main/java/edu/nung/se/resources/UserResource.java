//package edu.nung.se.resources;
//
//import jakarta.ws.rs.*;
//import jakarta.ws.rs.core.MediaType;
//import jakarta.ws.rs.core.Response;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//@Path("/users")
//@Produces(MediaType.APPLICATION_JSON)
//public class UserResource {
//
//    private final List<User> users = new ArrayList<>();
//
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response createUser(User user) {
//        users.add(user);  // Зберігаємо користувача в списку
//        return Response.ok(user).build();
//    }
//
//    @GET
//    public List<User> getUsers() {
//        return users;
//    }
//}

package edu.nung.se.resources;

import jakarta.ws.rs.*;
        import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UserResource {

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(@FormParam("name") String name, @FormParam("email") String email) {
        User user = new User(name, email);

        return Response.ok(user).build();
    }
}
