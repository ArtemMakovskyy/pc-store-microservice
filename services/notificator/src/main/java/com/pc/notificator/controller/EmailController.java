package com.pc.notificator.controller;

import com.pc.notificator.entity.Mail;
import com.pc.notificator.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mail")
public class EmailController {

    private final EmailService emailService;

    @GetMapping
    public String test(){
        return "test";
    }

    /** POST
         {
         "to" : ["mail@gmail.com"],
         "subject" : "Simple mail",
         "body" : "Hello"
         }
     * @param mail
     */
    @PostMapping("/simple")
    public void sendSimpleEmail(@RequestBody Mail mail){
        emailService.sendSimpleEmail(mail);
    }

    /** POST
     {
         "to" : ["mail@gmail.com"],
         "subject" : "HTML mail",
         "body" : "<h1>Hello, World</h1><p>This is an email with simple HTML markup.</p>"
         }
     * @param mail
     */
    @PostMapping("/html")
    public void sendHTMLEmail(@RequestBody Mail mail) throws MessagingException {
        emailService.sendHTMLEmail(mail);
    }

    /** POST
         {
         "to" : ["mail@gmail.com"],
         "subject" : "Thymleaf mail"
         }
     * @param mail
     */
    @PostMapping("/template")
    public void sendEmailWithThymeLeaf(@RequestBody Mail mail) throws MessagingException {
        emailService.sendEmailWithThymeLeaf(mail);
    }

    /** POST
         {
         "to" : ["mail@gmail.com"]
         }
     * @param mail
     */
    @PostMapping("/attachment")
    public void sendEmailWithAttachment(@RequestBody Mail mail) throws MessagingException {
        emailService.sendEmailWithAttachment(mail);
    }

}
