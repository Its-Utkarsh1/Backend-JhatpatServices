package com.example.demo.Service;

import com.example.demo.Dto.Response.AuthResponse;
import com.example.demo.Dto.Request.LoginRequest;
import com.example.demo.Dto.Request.RegisterRequest;
import com.example.demo.Model.Role;
import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final OtpService otpService;
    private final ModelMapper modelMapper;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public String register(RegisterRequest request){

        if(userRepository.existsByEmail(request.getEmail())){
            throw  new IllegalArgumentException("Email already registered");
        }
        //Create user with hashed password
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(request.getRole() !=null ? request.getRole(): Role.USER)
                .isVerified(false)
                .build();

        userRepository.save(user);

        // Generate OTP and send email
        String otp = otpService.genetateOtp(request.getEmail());
        emailService.sendOtpByEmail(request.getEmail(),otp);

        log.info("User Registered: {}",request.getEmail());
        return "Registration successful. Check your email for the OTP.";
    }

    @Transactional
    public String verifyotp(String email, String otp){

        if(!otpService.verifyotp(email,otp)){
            throw new IllegalArgumentException("Invalid or expired OTP");
        }

        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not Found"));

        user.setVerified(true);
        userRepository.save(user);

        log.info("Email verified for: {}", email);
        return "Email verified successfully. You can now log in.";
    }

    public String resendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.isVerified()) {
            throw new IllegalArgumentException("Email already verified");
        }

        String otp = otpService.genetateOtp(email);
        emailService.sendOtpByEmail(email, otp);
        return "OTP resent to " + email;
    }

    public AuthResponse login(LoginRequest request){

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if(!user.isVerified()){
            throw new IllegalArgumentException("Please verify your email first");
        }

        //Authenticating
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        String token =  jwtUtils.generateTokenFromUsername(userDetails);

        log.info("Login: {}", request.getEmail());

        return AuthResponse.builder()
                .fullName(user.getFullName())
                .token(token)
                .role(user.getRole())
                .email(user.getEmail())
                .build();


    }

}
