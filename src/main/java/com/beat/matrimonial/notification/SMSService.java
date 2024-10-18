package com.beat.matrimonial.notification;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SMSService {

  @Value("${twilio.account.sid}")
  private String ACCOUNT_SID;

  @Value("${twilio.auth.token}")
  private String AUTH_TOKEN;

  @Value("${twilio.phone.number}")
  private String FROM_NUMBER;

  public void sendSMS(String toPhoneNumber, String messageBody) {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    Message message = Message.creator(
            new PhoneNumber(toPhoneNumber),
            new PhoneNumber(FROM_NUMBER),
            messageBody)
        .create();

    System.out.println("SMS sent with SID: " + message.getSid());
  }
}
