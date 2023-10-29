package routes;


import controller.impl.AuthController;
import io.javalin.apibuilder.EndpointGroup;
import security.RouteRoles;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class AuthRoutes {
    private final AuthController userController = new AuthController();

    protected EndpointGroup getRoutes() {

        return () -> {
            path("/auth", () -> {
                post("/login", userController::login, RouteRoles.ANYONE);
                post("/register", userController::register, RouteRoles.ANYONE);
            });
        };
    }

}
