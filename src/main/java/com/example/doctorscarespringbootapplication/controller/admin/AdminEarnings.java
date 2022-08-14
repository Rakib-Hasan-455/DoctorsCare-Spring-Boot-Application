package com.example.doctorscarespringbootapplication.controller.admin;

import com.example.doctorscarespringbootapplication.dao.*;
import com.example.doctorscarespringbootapplication.entity.AppointDoctor;
import com.example.doctorscarespringbootapplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/admin")
public class AdminEarnings {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdditionalDoctorsRepository additionalDoctorsRepository;
    @Autowired
    private AppointDoctorRepository appointDoctorRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private SavedPostsRepository savedPostsRepository;

    @GetMapping("/earnings")
    public String earnings(Model model, Principal principal) {
        model.addAttribute("title", "Earnings");
        addCommonData(model, principal);

        //        Income Short Stats 4 Cards
        String totalEarnings = appointDoctorRepository.sumAllAppointEarningNative();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDateTime localDateTime = LocalDateTime.now();
        String dateToday = localDateTime.format(dateTimeFormatter);
        String totalEarnToday = appointDoctorRepository.sumTodaysEarning(Date.valueOf(dateToday));
        if (totalEarnToday != null && totalEarnings != null) {
            double earnUpdriftPercent = (Double.parseDouble(totalEarnToday) / Double.parseDouble(totalEarnings)) * 100;
            DecimalFormat dec = new DecimalFormat("#0.00");
            String earnUpDriftPercentString = dec.format(earnUpdriftPercent) + "%";
            model.addAttribute("totalEarnUpdrift", earnUpDriftPercentString);
        } else {
            model.addAttribute("totalEarnUpdrift", "0.00%");
        }
        if (totalEarnings != null) {
            model.addAttribute("totalEarnings", totalEarnings);
        } else {
            model.addAttribute("totalEarnings", "0.00");
        }


        LocalDateTime yesterdayDate = localDateTime.minusDays(1);
        String yesterdayDateFormatted = yesterdayDate.format(dateTimeFormatter);
        String totalEarnYesterday = appointDoctorRepository.sumTodaysEarning(Date.valueOf(yesterdayDateFormatted));
        if (totalEarnYesterday != null && totalEarnToday != null) {
            double earnUpdriftPercent = (Double.parseDouble(totalEarnToday) / Double.parseDouble(totalEarnYesterday)) * 100;
            DecimalFormat dec = new DecimalFormat("#0.00");
            String earnUpDriftPercentString = dec.format(earnUpdriftPercent) + "%";
            model.addAttribute("totalEarnTodayUpdrift", earnUpDriftPercentString);
        } else {
            model.addAttribute("totalEarnTodayUpdrift", "0.00%");
        }
        if (totalEarnToday != null) {
            model.addAttribute("totalEarnToday", totalEarnToday);
        } else {
            model.addAttribute("totalEarnToday", "0.00");
        }

        LocalDateTime weeklyStartingDate = localDateTime.minusDays(7);
        String weeklyStartingDateFormatted = weeklyStartingDate.format(dateTimeFormatter);
        String totalEarnWeekly = appointDoctorRepository.sumWeeklyEarningNative(Date.valueOf(weeklyStartingDateFormatted), Date.valueOf(dateToday));
        if (totalEarnWeekly != null && totalEarnToday != null) {
            double earnUpdriftPercent = (Double.parseDouble(totalEarnToday) / Double.parseDouble(totalEarnWeekly)) * 100;
            DecimalFormat dec = new DecimalFormat("#0.00");
            String earnUpDriftPercentString = dec.format(earnUpdriftPercent) + "%";
            model.addAttribute("totalEarnWeeklyUpdrift", earnUpDriftPercentString);
        } else {
            model.addAttribute("totalEarnWeeklyUpdrift", "0.00%");
        }
        if (totalEarnWeekly != null) {
            model.addAttribute("totalEarnWeekly", totalEarnWeekly);
        } else {
            model.addAttribute("totalEarnWeekly", "0.00");
        }

        LocalDateTime monthlyStartingDate = localDateTime.minusDays(30);
        String monthlyStartingDateFormatted = monthlyStartingDate.format(dateTimeFormatter);
        String totalEarnMonthly = appointDoctorRepository.sumWeeklyEarningNative(Date.valueOf(monthlyStartingDateFormatted), Date.valueOf(dateToday));
        if (totalEarnMonthly != null && totalEarnToday != null) {
            double earnUpdriftPercent = (Double.parseDouble(totalEarnToday) / Double.parseDouble(totalEarnMonthly)) * 100;
            DecimalFormat dec = new DecimalFormat("#0.00");
            String earnUpDriftPercentString = dec.format(earnUpdriftPercent) + "%";
            model.addAttribute("totalEarnMonthlyUpdrift", earnUpDriftPercentString);
        } else {
            model.addAttribute("totalEarnMonthlyUpdrift", "0.00%");
        }
        if (totalEarnMonthly != null) {
            model.addAttribute("totalEarnMonthly", totalEarnMonthly);
        } else {
            model.addAttribute("totalEarnMonthly", "0.00");
        }


//        Recent Appointment Table
        Pageable pageable = PageRequest.of(0, 5);
        Page<AppointDoctor> appointDoctorList = appointDoctorRepository.findAllByOrderByIdDesc(pageable);
        model.addAttribute("appointDoctorList", appointDoctorList);


//        User by role chart
        int totalUser = (int) userRepository.count();
        int patientTotalUser = (int) userRepository.countByRole("ROLE_PATIENT");
        int doctorTotalUser = (int) userRepository.countByRole("ROLE_DOCTOR");
        int adminTotalUser = (int) userRepository.countByRole("ROLE_ADMIN");

        DecimalFormat dec = new DecimalFormat("#0.0");
        //        Patient
        double patientUserPercent = patientTotalUser / (double) totalUser * 100.0;
        String patientUserPercentFormatted = dec.format(patientUserPercent);
        model.addAttribute("patientUserPercent", patientUserPercentFormatted);
        //        Doctor
        double doctorUserPercent = doctorTotalUser / (double) totalUser * 100.0;
        String doctorUserPercentFormatted = dec.format(doctorUserPercent);
        model.addAttribute("doctorUserPercent", doctorUserPercentFormatted);
        //        Admin
        double adminUserPercent = adminTotalUser / (double) totalUser * 100.0;
        String adminUserPercentFormatted = dec.format(adminUserPercent);
        model.addAttribute("adminUserPercent", adminUserPercentFormatted);


        // Doctor Type Percent Chart
        int totalDoctor = (int) additionalDoctorsRepository.count();
        int pediatricsTotalDoctor = additionalDoctorsRepository.countByDoctortype("Pediatrics");
        int skinTotalDoctor = additionalDoctorsRepository.countByDoctortype("Skin");
        int medicineTotalDoctor = additionalDoctorsRepository.countByDoctortype("Medicine");
        int covidSupportTotalDoctor = additionalDoctorsRepository.countByDoctortype("Covid Support");
        int mentalHealthTotalDoctor = additionalDoctorsRepository.countByDoctortype("Mental Health");
        int urologyTotalDoctor = additionalDoctorsRepository.countByDoctortype("Urology");
        int eyeTotalDoctor = additionalDoctorsRepository.countByDoctortype("Eye");
        int cardiologyTotalDoctor = additionalDoctorsRepository.countByDoctortype("Cardiology");

        //        Pediatrics
        double pediatricsDoctorPercent = pediatricsTotalDoctor / (double) totalDoctor * 100.0;
        String pediatricsDoctorPercentFormatted = dec.format(pediatricsDoctorPercent);
        model.addAttribute("pediatricsDoctorPercent", pediatricsDoctorPercentFormatted);
        //        Skin
        double skinDoctorPercent = skinTotalDoctor / (double) totalDoctor * 100.0;
        String skinDoctorPercentFormatted = dec.format(skinDoctorPercent);
        model.addAttribute("skinDoctorPercent", skinDoctorPercentFormatted);
        //        Medicine
        double medicineDoctorPercent = medicineTotalDoctor / (double) totalDoctor * 100.0;
        String medicineDoctorPercentFormatted = dec.format(medicineDoctorPercent);
        model.addAttribute("medicineDoctorPercent", medicineDoctorPercentFormatted);
        //        Covid Support
        double covidSupportDoctorPercent = covidSupportTotalDoctor / (double) totalDoctor * 100.0;
        String covidSupportDoctorPercentFormatted = dec.format(covidSupportDoctorPercent);
        model.addAttribute("covidSupportDoctorPercent", covidSupportDoctorPercentFormatted);
        //        Mental Health
        double mentalHealthDoctorPercent = mentalHealthTotalDoctor / (double) totalDoctor * 100.0;
        String mentalHealthDoctorPercentFormatted = dec.format(mentalHealthDoctorPercent);
        model.addAttribute("mentalHealthDoctorPercent", mentalHealthDoctorPercentFormatted);
        //        Urology
        double urologyDoctorPercent = urologyTotalDoctor / (double) totalDoctor * 100.0;
        String urologyDoctorPercentFormatted = dec.format(urologyDoctorPercent);
        model.addAttribute("urologyDoctorPercent", urologyDoctorPercentFormatted);
        //        Eye
        double eyeDoctorPercent = eyeTotalDoctor / (double) totalDoctor * 100.0;
        String eyeDoctorPercentFormatted = dec.format(eyeDoctorPercent);
        model.addAttribute("eyeDoctorPercent", eyeDoctorPercentFormatted);
        //        Cardiology
        double cardiologyDoctorPercent = cardiologyTotalDoctor / (double) totalDoctor * 100.0;
        String cardiologyDoctorPercentFormatted = dec.format(cardiologyDoctorPercent);
        model.addAttribute("cardiologyDoctorPercent", cardiologyDoctorPercentFormatted);

        // Earning Chart By Month
        String januaryEarn = appointDoctorRepository.sumEarnByMonthNative(1) == null ? "0" : appointDoctorRepository.sumEarnByMonthNative(1);
        String februaryEarn = appointDoctorRepository.sumEarnByMonthNative(2) == null ? "0" : appointDoctorRepository.sumEarnByMonthNative(2);
        String marchEarn = appointDoctorRepository.sumEarnByMonthNative(3) == null ? "0" : appointDoctorRepository.sumEarnByMonthNative(3);
        String aprilEarn = appointDoctorRepository.sumEarnByMonthNative(4) == null ? "0" : appointDoctorRepository.sumEarnByMonthNative(4);
        String mayEarn = appointDoctorRepository.sumEarnByMonthNative(5) == null ? "0" : appointDoctorRepository.sumEarnByMonthNative(5);
        String juneEarn = appointDoctorRepository.sumEarnByMonthNative(6) == null ? "0" : appointDoctorRepository.sumEarnByMonthNative(6);
        String julyEarn = appointDoctorRepository.sumEarnByMonthNative(7) == null ? "0" : appointDoctorRepository.sumEarnByMonthNative(7);
        String augustEarn = appointDoctorRepository.sumEarnByMonthNative(8) == null ? "0" : appointDoctorRepository.sumEarnByMonthNative(8);
        String septemberEarn = appointDoctorRepository.sumEarnByMonthNative(9) == null ? "0" : appointDoctorRepository.sumEarnByMonthNative(9);
        String octoberEarn = appointDoctorRepository.sumEarnByMonthNative(10) == null ? "0" : appointDoctorRepository.sumEarnByMonthNative(10);
        String novemberEarn = appointDoctorRepository.sumEarnByMonthNative(11) == null ? "0" : appointDoctorRepository.sumEarnByMonthNative(11);
        String decemberEarn = appointDoctorRepository.sumEarnByMonthNative(12) == null ? "0" : appointDoctorRepository.sumEarnByMonthNative(12);

        model.addAttribute("januaryEarn", januaryEarn);
        model.addAttribute("februaryEarn", februaryEarn);
        model.addAttribute("marchEarn", marchEarn);
        model.addAttribute("aprilEarn", aprilEarn);
        model.addAttribute("mayEarn", mayEarn);
        model.addAttribute("juneEarn", juneEarn);
        model.addAttribute("julyEarn", julyEarn);
        model.addAttribute("augustEarn", augustEarn);
        model.addAttribute("septemberEarn", septemberEarn);
        model.addAttribute("octoberEarn", octoberEarn);
        model.addAttribute("novemberEarn", novemberEarn);
        model.addAttribute("decemberEarn", decemberEarn);


        return "admin/admin_earnings";
    }

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userEmail = principal.getName();
        User user = this.userRepository.getUserByEmailNative(userEmail);
        model.addAttribute("user", user);
    }
}
