package com.example.demo.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final StringRedisTemplate stringRedisTemplate;
    private static final long OTP_EXPIRY = 5;
    private static final String OTP_PREFIX = "otp:";


    public String genetateOtp(String email){
        String otp = String.format("%06d", new Random().nextInt(999999));
        String key = OTP_PREFIX + email;
        stringRedisTemplate.opsForValue().set(key, otp, Duration.ofMinutes(OTP_EXPIRY));

        return otp;
    }

    public boolean verifyotp(String email, String inputOtp){
        String key = OTP_PREFIX + email;
        String storedOtp = stringRedisTemplate.opsForValue().get(key);
        if(storedOtp == null) return false;
        if(storedOtp.equals(inputOtp)){
            stringRedisTemplate.delete(key);
            return true;
        }
        return false;
    }



}
