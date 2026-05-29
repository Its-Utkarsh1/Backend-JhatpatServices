package com.example.demo.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendOtpByEmail(String toEmail, String otp){
        String subject = "JhatPat Service - Email Verification OTP";
        String body = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto;">
                    <h2 style="color: #e44d26;">JhatpatService</h2>
                    <p>Your OTP for email verification is:</p>
                    <div style="font-size: 32px; font-weight: bold; letter-spacing: 8px;
                                padding: 16px; background: #f5f5f5; text-align: center;
                                border-radius: 8px; margin: 20px 0;">
                        %s
                    </div>
                    <p>This OTP is valid for <strong>5 minutes</strong>.</p>
                    <p style="color: #999; font-size: 12px;">
                        If you did not request this, please ignore this email.
                    </p>
                </div>
                """.formatted(otp);
        sendHtmlEmail(toEmail, subject, body);
    }

    @Async
    public void sendBookingConfirmation(String toEmail, String customerName,
                                        String serviceName, String scheduledAt) {
        String subject = "Booking Confirmed - JhatpatService";
        String body = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto;">
                    <h2 style="color: #e44d26;">Booking Confirmed!</h2>
                    <p>Hi <strong>%s</strong>,</p>
                    <p>Your booking for <strong>%s</strong> has been confirmed.</p>
                    <p>Scheduled at: <strong>%s</strong></p>
                    <p>Our service provider will reach you at the scheduled time.</p>
                    <br>
                    <p>Thank you for choosing JhatpatService!</p>
                </div>
                """.formatted(customerName, serviceName, scheduledAt);
        sendHtmlEmail(toEmail, subject, body);
    }



    private void sendHtmlEmail(String toEmail, String subject, String body) {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body,true);
            mailSender.send(message);
            log.info("Email sent to: {}", toEmail);
        }catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", toEmail, e.getMessage());
        }
    }

}
