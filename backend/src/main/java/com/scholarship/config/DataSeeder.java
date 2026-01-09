package com.scholarship.config;

import com.scholarship.entity.Admin;
import com.scholarship.entity.Advisor;
import com.scholarship.entity.User;
import com.scholarship.entity.Reviewer;
import com.scholarship.repository.AdvisorRepository;
import com.scholarship.repository.ReviewerRepository;
import com.scholarship.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdvisorRepository advisorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private com.scholarship.repository.AdminRepository adminRepository;

    @Autowired
    private ReviewerRepository reviewerRepository;

    @Override
    public void run(String... args) throws Exception {
        createAdvisor("advisor1", "password", "Lin Mei-ling", "meiling@example.com", "Computer Science");
        createAdmin("admin1", "password", "System Admin", "admin@example.com");
        createReviewer("reviewer1", "password", "Default Reviewer", "reviewer1@example.com", "Academic Affairs Office");
    }

    private void createAdvisor(String account, String password, String name, String email, String department) {
        if (userRepository.findByAccount(account).isPresent()) {
            // System.out.println("Advisor " + account + " already exists.");
            return;
        }

        User user = new User();
        user.setAccount(account);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setEmail(email);
        user.setRole("ADVISOR");

        userRepository.save(user);

        Advisor advisor = new Advisor();
        advisor.setUserId(user.getUserId());
        advisor.setDepartment(department);
        // Default office/title
        advisor.setOffice("Room 101");
        advisor.setTitle("Professor");

        advisorRepository.save(advisor);
        System.out.println("Created Advisor: " + account);
    }

    private void createAdmin(String account, String password, String name, String email) {
        if (userRepository.findByAccount(account).isPresent()) {
            // System.out.println("Admin " + account + " already exists.");
            return;
        }

        User user = new User();
        user.setAccount(account);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setEmail(email);
        user.setRole("ADMIN");

        userRepository.save(user);

        Admin admin = new Admin();
        admin.setUserId(user.getUserId());
        admin.setLastLoginTime(java.time.LocalDateTime.now());

        adminRepository.save(admin);
        System.out.println("Created Admin: " + account);
    }

    private void createReviewer(String account, String password, String name, String email, String reviewUnit) {
        if (userRepository.findByAccount(account).isPresent()) {
            return;
        }

        User user = new User();
        user.setAccount(account);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setEmail(email);
        user.setRole("REVIEWER");

        userRepository.save(user);

        Reviewer reviewer = new Reviewer();
        reviewer.setUserId(user.getUserId());
        reviewer.setReviewUnit(reviewUnit);

        reviewerRepository.save(reviewer);
        System.out.println("Created Reviewer: " + account);
    }
}
