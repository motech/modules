package org.motechproject.mtraining.service.impl;

import org.motechproject.mtraining.dto.CourseUnitDto;
import org.motechproject.mtraining.service.CourseStructureService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link org.motechproject.mtraining.service.CourseStructureService}.
 * This service allows creating relations between course units(Course, Chapter, Quiz and Lesson).
 */
@Service
public class CourseStructureServiceImpl implements CourseStructureService {

    @Override
    public void updateCourseStructure(List<CourseUnitDto> courses) {
        //TODO saving
    }
}
