package org.motechproject.mtraining.service;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.mtraining.domain.Course;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CourseServiceImplTest {

    private CourseService courseService;

    @Before
    public void setUp() throws Exception {
        courseService = new CourseServiceImpl();
    }

    @Test
    public void shouldHaveAnEmptyCourseListOnCreation() throws Exception {
        List<Course> allCourses = courseService.getAllCourses();
        assertThat(allCourses.size(), is(0));
    }

    @Test
    public void shouldAddCourse() throws Exception {
        courseService.addCourse(new Course("My Course 1"));
        List<Course> allCourses = courseService.getAllCourses();
        assertThat(allCourses.size(), is(1));
        assertThat(allCourses.get(0).getName(), Is.is("My Course 1"));
    }
}
