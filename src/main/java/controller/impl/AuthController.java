package controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import config.HibernateConfig;
import dao.impl.AuthDao;
import exception.ApiException;
import exception.AuthorizationException;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import model.User;
import security.TokenFactory;

import java.util.Set;

public class AuthController {

    private final AuthDao authDao;
    private final TokenFactory tokenFactory = TokenFactory.getInstance();

    public AuthController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        authDao = AuthDao.getInstance(emf);
    }

    public void login(Context ctx) throws ApiException, AuthorizationException {
        String[] userInfos = getUserInfos(ctx, true);
        User user = getVerfiedOrRegisterUser(userInfos[0], userInfos[1], "", false);
        String token = getToken(userInfos[0], user.getRolesAsStrings());

        // Create response
        ctx.status(200);
        ctx.result(createResponse(userInfos[0], token));
    }

    public void register(Context ctx) throws ApiException, AuthorizationException {
        String[] userInfos = getUserInfos(ctx, false);
        User user = getVerfiedOrRegisterUser(userInfos[0], userInfos[1], userInfos[2], true);
        String token = getToken(userInfos[0], user.getRolesAsStrings());

        // Create response
        ctx.res().setStatus(201);
        ctx.result(createResponse(userInfos[0], token));
    }

    private String createResponse(String username, String token) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseJson = mapper.createObjectNode();
        responseJson.put("username", username);
        responseJson.put("token", token);
        return responseJson.toString();
    }

    private String[] getUserInfos(Context ctx, boolean tryLogin) throws ApiException, AuthorizationException {
        String request = ctx.body();
        return tokenFactory.parseJsonObject(request, tryLogin);
    }

    private User getVerfiedOrRegisterUser(String username, String password, String role, boolean isCreate) throws AuthorizationException {
        return isCreate ? authDao.registerUser(username, password, role) : authDao.getVerifiedUser(username, password);
    }

    private String getToken(String username, Set<String> userRoles) throws ApiException {
        return tokenFactory.createToken(username, userRoles);
    }
}
