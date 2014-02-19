package org.motechproject.mtraining.osgi;

import org.motechproject.mtraining.service.CourseService;
import org.motechproject.testing.osgi.BaseOsgiIT;

import java.util.ArrayList;
import java.util.List;

public class CourseBundleIT extends BaseOsgiIT {

    public void testThatCourseServiceIsAvailable() throws Exception {
        CourseService courseService = (CourseService) getApplicationContext().getBean("courseService");
        assertNotNull(courseService);
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[]{"META-INF/motech/testTrainingApplicationContext.xml"};
    }

    @Override
    protected List<String> getImports() {
        List<String> imports = new ArrayList<>();
        imports.add("org.motechproject.mtraining.service");
        return imports;
    }
}
