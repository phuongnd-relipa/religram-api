/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.util.email;

import com.relipa.religram.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@Async
public class ResetPasswordMail {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(User user, String resetToken) throws MessagingException {

        MimeMessage msg = javaMailSender.createMimeMessage();

        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setFrom("Religram <no-reply@religram.com>");
        helper.setTo(user.getEmail());

        helper.setSubject("Reset Your Password");

        StringBuilder mailBuilder = new StringBuilder();

        mailBuilder.append("<!DOCTYPE html>");
        mailBuilder.append("<html lang=\"en\">");
        mailBuilder.append("<head>");
        mailBuilder.append("    <meta charset=\"UTF-8\">");
        mailBuilder.append("    <title>Title</title>");
        mailBuilder.append("</head>");
        mailBuilder.append("<body>");
        mailBuilder.append("<style>");
        mailBuilder.append("    .button {");
        mailBuilder.append("        font: bold 11px Arial;");
        mailBuilder.append("        text-decoration: none;");
        mailBuilder.append("        background-color: #2185e2;");
        mailBuilder.append("        color: #ffffff;");
        mailBuilder.append("        padding: 12px 16px 12px 16px;");
        mailBuilder.append("        border-top: 1px solid #CCCCCC;");
        mailBuilder.append("        border-right: 1px solid #333333;");
        mailBuilder.append("        border-bottom: 1px solid #333333;");
        mailBuilder.append("        border-left: 1px solid #CCCCCC;");
        mailBuilder.append("    }");
        mailBuilder.append("</style>");

        mailBuilder.append("<p>Hi ");
        mailBuilder.append(user.getFullname());
        mailBuilder.append(",</p>");
        mailBuilder.append("<p>We got a request to reset your Religram password.</p><br>");

        mailBuilder.append("<div style=\"margin-left: 10px;\">");
        mailBuilder.append("    <a href=\"https://religram.relipa-test.online/resetpassword/");
        mailBuilder.append(resetToken);
        mailBuilder.append("\" class=\"button\">Reset Password</a>");
        mailBuilder.append("</div><br>");

        mailBuilder.append("<p>If you ignore this message, your password will not be changed.</p>");
        mailBuilder.append("</body>");
        mailBuilder.append("</html>");

        helper.setText(mailBuilder.toString(), true);

        javaMailSender.send(msg);

    }
}
