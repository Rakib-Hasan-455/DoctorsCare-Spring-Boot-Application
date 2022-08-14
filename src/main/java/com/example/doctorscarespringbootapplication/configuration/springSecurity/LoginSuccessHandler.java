package com.example.doctorscarespringbootapplication.configuration.springSecurity;

import com.example.doctorscarespringbootapplication.dao.UserRepository;
import com.example.doctorscarespringbootapplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = this.userRepository.getUserByEmailNative(customUserDetails.getUsername());
        String redirectURL = request.getContextPath() + "/login";
        if (user.getRole().toLowerCase(Locale.ROOT).equals("role_admin")) {
            redirectURL = "/admin/index";
        } else if (user.getRole().toLowerCase(Locale.ROOT).equals("role_doctor")) {
            redirectURL = "/doctor/index";
        } else if (user.getRole().toLowerCase(Locale.ROOT).equals("role_patient")) {
            redirectURL = "/patient/index";
        }
        response.sendRedirect(redirectURL);
    }

}