package com.example.doctorscarespringbootapplication.controller.admin;

import com.example.doctorscarespringbootapplication.dao.UserRepository;
import com.example.doctorscarespringbootapplication.dto.*;
import com.example.doctorscarespringbootapplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminPatientDoctorListEditDelete {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/process-patient-delete")
//    @Transactional
    public ResponseEntity<Object> doDeletePatient(@RequestBody DeleteUserDTO deleteUserDTO) {
        userRepository.deleteById(Integer.parseInt(deleteUserDTO.getUserId()));
        ServiceResponse<String> response = new ServiceResponse<String>("success", "Patient Deleted Successfully!");
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    @PostMapping("/process-doctor-delete")
//    @Transactional
    public ResponseEntity<Object> doDeleteDoctor(@RequestBody DeleteUserDTO deleteUserDTO) {
        userRepository.deleteById(Integer.parseInt(deleteUserDTO.getUserId()));
        ServiceResponse<String> response = new ServiceResponse<String>("success", "Doctor Deleted Successfully!");
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    @PostMapping("/edit-patient-details")
    public String editPatientDetails(@ModelAttribute EditUserDTO editUserDTO, Model model, Principal principal) {
        User patientUser = this.userRepository.findById(Integer.parseInt(editUserDTO.getUserId()));
        model.addAttribute("patientUser", patientUser);
        model.addAttribute("page", editUserDTO.getPageNo());
        addCommonData(model, principal);
        return "admin/admin_patient_edit";
    }

    @PostMapping("/edit-doctor-details")
    public String editDoctorDetails(@ModelAttribute EditUserDTO editUserDTO, Model model, Principal principal) {
        System.out.println(editUserDTO.getPageNo());
        System.out.println(editUserDTO.getUserId());
        addCommonData(model, principal);
        return "admin/admin_doctor_edit";
    }

    @PostMapping("/process-patient-edit")
    public String processPatientEdit(@ModelAttribute AdminPatientEditDTO adminPatientEditDTO) {
        User user = userRepository.findById(Integer.parseInt(adminPatientEditDTO.getId()));
        user.setName(adminPatientEditDTO.getName());
        user.setDOB(adminPatientEditDTO.getDOB());
        user.setEmail(adminPatientEditDTO.getEmail());
        user.setPhone(adminPatientEditDTO.getPhone());
        user.setAddress(adminPatientEditDTO.getAddress());
        user.setAbout(adminPatientEditDTO.getAbout());
        userRepository.save(user);
        return "redirect:/admin/patients-list/"+adminPatientEditDTO.getPage();
    }

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userEmail = principal.getName();
        User user = this.userRepository.getUserByEmailNative(userEmail);
        model.addAttribute("user", user);
    }

}
