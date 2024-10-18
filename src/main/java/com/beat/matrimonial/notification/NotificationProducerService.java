package com.beat.matrimonial.notification;

import com.beat.matrimonial.dto.EmailNotification;
import com.beat.matrimonial.dto.SMSNotification;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducerService {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  public void sendEmailNotification(EmailNotification notification) {
    rabbitTemplate.convertAndSend("emailQueue", notification);
  }

  public void sendSMSNotification(SMSNotification notification) {
    rabbitTemplate.convertAndSend("smsQueue", notification);
  }
}
