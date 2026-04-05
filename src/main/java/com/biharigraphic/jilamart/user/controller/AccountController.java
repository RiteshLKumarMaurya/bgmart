package com.biharigraphic.jilamart.user.controller;

import com.biharigraphic.jilamart.user.dto.request.DeleteAccountRequestDto;
import com.biharigraphic.jilamart.user.entity.DeleteAccountRequest;
import com.biharigraphic.jilamart.user.entity.User;
import com.biharigraphic.jilamart.user.mapper.DeleteAccountRequestMapper;
import com.biharigraphic.jilamart.user.repository.DeleteAccountRequestRepository;
import com.biharigraphic.jilamart.user.repository.UserRepository;
import com.biharigraphic.jilamart.utils.PhoneNumberUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
    public class AccountController {

        private final DeleteAccountRequestRepository deleteAccountRequestRepository;
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final DeleteAccountRequestMapper deleteAccountRequestMapper;

        public AccountController(DeleteAccountRequestRepository deleteAccountRequestRepository,
                                 UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 DeleteAccountRequestMapper deleteAccountRequestMapper) {
            this.deleteAccountRequestRepository = deleteAccountRequestRepository;
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
            this.deleteAccountRequestMapper = deleteAccountRequestMapper;
        }

        // Show delete account form
        @GetMapping("/delete-account")
        public String showDeleteAccountForm(Model model) {
            model.addAttribute("deleteRequest", new DeleteAccountRequestDto());
            return "delete-account"; // Thymeleaf template
        }

        // Handle delete account form submission
        @PostMapping("/delete-account")
        public String handleDeleteAccount(@ModelAttribute("deleteRequest") DeleteAccountRequestDto request,
                                          Model model) {

            User user = userRepository.findByUsername(request.getUsername()).orElse(null);

            if (user == null || user.getDeletedAt() != null) {
                model.addAttribute("error", "Username not found or already deleted!");
                return "delete-account";
            }

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                model.addAttribute("error", "Incorrect password.");
                return "delete-account";
            }

            if (!deleteAccountRequestRepository.existsByPhone(PhoneNumberUtil.extractTenDigitPhone(request.getPhone()))) {
                if (request.getPhone().length() > 10) {
                    request.setPhone(PhoneNumberUtil.extractTenDigitPhone(request.getPhone()));
                }
                DeleteAccountRequest deleteAccountRequest = deleteAccountRequestMapper.toEntity(request);
                deleteAccountRequest.setUser(user);
                deleteAccountRequestRepository.save(deleteAccountRequest);
            }

            model.addAttribute("success", "Your account deletion request was submitted successfully!");
            return "delete-account";
        }
    }