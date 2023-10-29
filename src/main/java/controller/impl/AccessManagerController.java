package controller.impl;


import dto.UserDTO;
import exception.ApiException;
import exception.AuthorizationException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.security.RouteRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import security.RouteRoles;
import security.TokenFactory;

import java.util.Set;

public class AccessManagerController {

    private final TokenFactory TOKEN_FACTORY = TokenFactory.getInstance();
    private final static Logger logger = LoggerFactory.getLogger(AccessManagerController.class);

    public void accessManagerHandler(Handler handler, Context ctx, Set<? extends RouteRole> permittedRoles) throws Exception {
        String path = ctx.path();
        boolean isAuthorized = false;

        if (path.equals("/api/v1/routes") || permittedRoles.contains(RouteRoles.ANYONE)) {
            handler.handle(ctx);
            return;
        } else {
            RouteRole[] userRole = getUserRole(ctx);
            for (RouteRole role : userRole) {
                if (permittedRoles.contains(role)) {
                    isAuthorized = true;
                    break;
                }
            }
        }

        if (isAuthorized) {
            handler.handle(ctx);
        } else {
            logger.error("You are not authorized to perform this action");
            throw new AuthorizationException(401, "You are not authorized to perform this action");
        }
    }

    private RouteRole[] getUserRole(Context ctx) throws AuthorizationException, ApiException {

        if (ctx.header("Authorization") == null) {
            throw new AuthorizationException(401, "No token provided");
        }

        String token = ctx.header("Authorization").split(" ")[1];
        UserDTO userDTO = TOKEN_FACTORY.verifyToken(token);

        if (userDTO == null) {
            logger.error("Invalid token");
            throw new ApiException(401, "Invalid token");
        }
        return userDTO.getRoles().stream().map(r -> RouteRoles.valueOf(r.toUpperCase())).toArray(RouteRole[]::new);
    }

}
