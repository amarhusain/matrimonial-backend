package com.beat.matrimonial.notification;

import com.beat.matrimonial.dto.EmailNotification;
import com.beat.matrimonial.dto.SMSNotification;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumerService {


  private final JavaMailSender emailSender;

  private final SMSService smsService;

  @Autowired
  public NotificationConsumerService(JavaMailSender emailSender, SMSService smsService) {
    this.emailSender = emailSender;
    this.smsService = smsService;
  }

  @RabbitListener(queues = "emailQueue")
  public void processEmailNotification(EmailNotification notification) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(notification.getTo());
    message.setSubject(notification.getSubject());
    message.setText(notification.getBody());
    emailSender.send(message);
  }

  @RabbitListener(queues = "smsQueue")
  public void processSMSNotification(SMSNotification notification) {
    smsService.sendSMS(notification.getPhoneNumber(), notification.getMessage());
  }
}
