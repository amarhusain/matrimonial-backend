package com.beat.matrimonial.service;

import com.beat.matrimonial.entity.Course;
import com.beat.matrimonial.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {


    private  final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> findAllCourses() {
        List<Course> courses = (List<Course>) courseRepository.findAll();
        return courses;
    }

}
