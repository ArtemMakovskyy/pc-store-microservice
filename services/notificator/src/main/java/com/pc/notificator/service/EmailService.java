package com.pc.notificator.service;

import com.pc.notificator.entity.Mail;
import jakarta.mail.MessagingException;

public interface EmailService {

    void sendSimpleEmail(Mail mail);

    void sendHTMLEmail(Mail mail) throws MessagingException;

    void sendEmailWithThymeLeaf(Mail mail) throws MessagingException;

    void sendEmailWithAttachment(Mail mail) throws MessagingException;
}
