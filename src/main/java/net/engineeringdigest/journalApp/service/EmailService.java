package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class EmailService {

//app password :aqte snmd jdxs xeck
@Autowired
private JavaMailSender javaMailSender;
    @PostConstruct
    public void test() {
        System.out.println("JavaMailSender Bean = " + javaMailSender);
    }

public void sendEmail(String to ,String subject ,String body){
    try{
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(body);
        javaMailSender.send(mail);
    } catch(Exception e){
        log.error("Exception while SendEmail ",e);
    }
}
}
