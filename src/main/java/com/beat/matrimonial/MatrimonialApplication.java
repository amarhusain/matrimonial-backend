package com.beat.matrimonial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class MatrimonialApplication {

  public static void main(String[] args) {
    SpringApplication.run(MatrimonialApplication.class, args);
    nu.pattern.OpenCV.loadLocally();
  }

}
