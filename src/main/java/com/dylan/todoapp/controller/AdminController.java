package com.dylan.todoapp.controller;

import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;
import com.auth0.net.TokenRequest;
import com.auth0.client.auth.AuthAPI;
import com.auth0.json.auth.TokenHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Value("${spring.security.oauth2.client.registration.auth0.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.auth0.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.provider.auth0.issuer-uri}")
    private String issuer;

    @Value("${app.roles.admin-id}")
    private String adminRoleId;

    @Value("${app.roles.user-id}")
    private String userRoleId;

    private String getManagementApiToken() throws Auth0Exception {

        String domain = issuer.replace("https://", "").replace("/", "");

        AuthAPI auth = AuthAPI.newBuilder(domain, clientId, clientSecret).build();

        TokenRequest request = auth.requestToken("https://" + domain + "/api/v2/");
        TokenHolder holder = request.execute().getBody();
        return holder.getAccessToken();
    }

    @GetMapping("/manage-users")
    public String showUserManagement(Model model) {
        try {
            String domain = issuer.replace("https://", "").replace("/", "");
            String apiToken = getManagementApiToken();

            ManagementAPI mgmt = ManagementAPI.newBuilder(domain, apiToken).build();

            List<User> userList = mgmt.users().list(null).execute().getBody().getItems();
            model.addAttribute("users", userList);

        } catch (Auth0Exception e) {
            e.printStackTrace();
            return "redirect:/?error=mgmt_api_failed";
        }
        return "admin/users";
    }

    @PostMapping("/assign-role")
    public String assignRole(@RequestParam String userId, @RequestParam String roleName) {
        String actualRoleId = roleName.equals("admin") ? adminRoleId : userRoleId;

        try {
            String domain = issuer.replace("https://", "").replace("/", "");
            String apiToken = getManagementApiToken();
            ManagementAPI mgmt = ManagementAPI.newBuilder(domain, apiToken).build();

            mgmt.users().addRoles(userId, List.of(actualRoleId)).execute();
            return "redirect:/admin/manage-users?success";
        } catch (Auth0Exception e) {
            return "redirect:/admin/manage-users?error";
        }
    }
}