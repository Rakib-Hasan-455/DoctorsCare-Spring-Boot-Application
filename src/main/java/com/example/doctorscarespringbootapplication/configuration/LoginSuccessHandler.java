package com.example.doctorscarespringbootapplication.configuration;

import java.io.IOException;
import java.security.Principal;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.doctorscarespringbootapplication.dao.UserRepository;
import com.example.doctorscarespringbootapplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();


        System.out.println(customUserDetails.getUsername());

        User user = this.userRepository.getUserByEmailNative(customUserDetails.getUsername());

        System.out.println(user.getRole());


        String redirectURL = request.getContextPath();
        if (user.getRole().toLowerCase(Locale.ROOT).equals("role_admin")) {
            redirectURL = "/admin/index";
        } else if (user.getRole().toLowerCase(Locale.ROOT).equals("role_doctor") ) {
            redirectURL = "/doctor/index";
        } else if (user.getRole().toLowerCase(Locale.ROOT).equals("role_patient") ) {
            redirectURL = "/patient/index";
        }

        response.sendRedirect(redirectURL);

    }

}