package com.beat.matrimonial.controller;


import com.beat.matrimonial.entity.Course;
import com.beat.matrimonial.service.CourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@Tag(name = "API for managing courses", description="")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    @GetMapping
    public ResponseEntity<List<Course>> retrieveCourses() {
        List<Course> courses = courseService.findAllCourses();
            return ResponseEntity.ok().body(courses);
    }

}
