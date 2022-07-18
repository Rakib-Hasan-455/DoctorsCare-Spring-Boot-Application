package com.example.doctorscarespringbootapplication;

import com.example.doctorscarespringbootapplication.configuration.EmailSenderServiceJava;
import com.example.doctorscarespringbootapplication.dao.UserRepository;
import com.example.doctorscarespringbootapplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootApplication

public class DoctorsCareSpringBootApplication {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSenderServiceJava senderService;
    public static void main(String[] args) {
        SpringApplication.run(DoctorsCareSpringBootApplication.class, args);
    }

    /*@EventListener(ApplicationEvent.class)
    public void sendMail() {
        senderService.sendEmail("hasanrakib455@gmail.com",
                "Test Subject abc",
                "Test Body abc");
     }*/

/*
     Every day at 4 PM it will reset doctors_schedule entity..
*/
    @Scheduled(cron = "0 0 16 * * *")
    void resetDoctorsSchedule() {
        updateDoctorsScheduleAfter4PM();
    }


    private void updateDoctorsScheduleAfter4PM() {
        DateTimeFormatter dtfHour = DateTimeFormatter.ofPattern("HH");
        LocalDateTime now = LocalDateTime.now();
        int hour = Integer.parseInt(dtfHour.format(now));
        if (hour >= 16) {
            List<User> userList = userRepository.findByRole("ROLE_DOCTOR");
            for (User user : userList) {
                user.getDoctorsSchedule().set_10_00(true);
                user.getDoctorsSchedule().set_10_30(true);
                user.getDoctorsSchedule().set_11_00(true);
                user.getDoctorsSchedule().set_11_30(true);
                user.getDoctorsSchedule().set_12_00(true);
                user.getDoctorsSchedule().set_2_00(true);
                user.getDoctorsSchedule().set_2_30(true);
                user.getDoctorsSchedule().set_3_00(true);
                user.getDoctorsSchedule().set_3_30(true);
                this.userRepository.save(user);
            }
        }
    }
}


@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduling.enabled", matchIfMissing = true)
class SchedulingConfiguration {
}
