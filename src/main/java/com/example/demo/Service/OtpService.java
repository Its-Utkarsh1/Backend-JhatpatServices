package com.example.demo.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final Map<String, String> otpStore = new ConcurrentHashMap<>();
    private static final long OTP_EXPIRY = 5;
    private static final String OTP_PREFIX = "otp:";


    public String genetateOtp(String email){
        String otp = String.format("%06d", new Random().nextInt(999999));
        String key = OTP_PREFIX + email;
        otpStore.put(email, otp);

        return otp;
    }

    public boolean verifyotp(String email, String inputOtp){
       String storedOtp = otpStore.get(email);

        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStore.remove(email);
            return true;
        }

        return false;;
    }



}
