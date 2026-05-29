package com.example.demo.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final Map<String, String> otpStore = new ConcurrentHashMap<>();

    public String genetateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStore.put(email, otp);
        return otp;
    }

    public boolean verifyotp(String email, String inputOtp) {
        String storedOtp = otpStore.get(email);

        if (storedOtp != null && storedOtp.equals(inputOtp)) {
            otpStore.remove(email);
            return true;
        }

        return false;
    }
}
