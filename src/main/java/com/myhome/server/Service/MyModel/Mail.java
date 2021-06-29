package com.myhome.server.Service.MyModel;

import com.myhome.server.Service.FileUpload;
import com.myhome.server.Service.MailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class Mail {

    private final JavaMailSender javaMailSender;
    private final MailService mailService;
    private Templates templates = new Templates();
    private String to;
    private String subject;

    public Mail(JavaMailSender javaMailSender, MailService mailService) {
        this.javaMailSender = javaMailSender;
        this.mailService = mailService;
    }

    public void sendMail(String template ,MyModel myModel) throws MessagingException, UnsupportedEncodingException {
        String from = "MyDomus@mydomus.home";
        StringBuilder body = new StringBuilder();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
        FileUpload fileUpload = new FileUpload();
        String am = mailService.addTemplates(templates.MEMBER_REGISTRATION_AUTHENTICATION,myModel);
        body.append(am);
        mimeMessageHelper.setFrom(from,"MyDomus");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body.toString(), true);
        javaMailSender.send(message);
    }
    public void sendMail(MyModel myModel) throws MessagingException, UnsupportedEncodingException {
        String from = "MyDomus@mydomus.home";
        StringBuilder body = new StringBuilder();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
        FileUpload fileUpload = new FileUpload();
        String am = mailService.addTemplates(templates.MEMBER_REGISTRATION_AUTHENTICATION,myModel);
        body.append(am);
        mimeMessageHelper.setFrom(from,"MyDomus");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body.toString(), true);
        javaMailSender.send(message);
    }
}
