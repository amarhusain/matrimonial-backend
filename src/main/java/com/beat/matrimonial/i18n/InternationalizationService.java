package com.beat.matrimonial.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class InternationalizationService {

  @Autowired
  private MessageSource messageSource;

  public String getLocalizedReligion(String religion) {
    return messageSource.getMessage("religion." + religion, null, LocaleContextHolder.getLocale());
  }
}