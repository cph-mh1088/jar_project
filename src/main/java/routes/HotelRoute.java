package routes;


import controller.impl.HotelController;
import io.javalin.apibuilder.EndpointGroup;
import security.RouteRoles;

import static io.javalin.apibuilder.ApiBuilder.*;

public class HotelRoute {

    private final HotelController hotelController = new HotelController();

    protected EndpointGroup getRoutes() {

        return () -> {
            path("/hotels", () -> {
                post("/", hotelController::create, RouteRoles.ADMIN, RouteRoles.MANAGER);
                get("/", hotelController::readAll, RouteRoles.ANYONE);
                get("/{id}", hotelController::read, RouteRoles.ANYONE);
                put("/{id}", hotelController::update, RouteRoles.ADMIN, RouteRoles.MANAGER);
                delete("/{id}", hotelController::delete, RouteRoles.ADMIN);
            });
        };
    }
}
