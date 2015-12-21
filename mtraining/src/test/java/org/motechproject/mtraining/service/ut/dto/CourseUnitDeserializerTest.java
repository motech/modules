package org.motechproject.mtraining.service.ut.dto;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.motechproject.mtraining.domain.CourseUnitState;
import org.motechproject.mtraining.dto.ChapterUnitDto;
import org.motechproject.mtraining.dto.CourseUnitDeserializer;
import org.motechproject.mtraining.dto.CourseUnitDto;
import org.motechproject.mtraining.util.Constants;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CourseUnitDeserializerTest {

    @Test
    public void shouldDeserializeJson() throws IOException {
        JsonParser jsonParser = getParser("/course-units-dto.json");
        List<CourseUnitDto> courses = new CourseUnitDeserializer().deserialize(jsonParser, null);

        assertNotNull(courses);
        assertEquals(2, courses.size());

        verifyCourse(courses.get(0));
    }

    private void verifyCourse(CourseUnitDto course) {
        verifyUnit(course, 162l, "course_1", CourseUnitState.Active.toString(), Constants.COURSE);

        assertNotNull(course.getUnits());
        assertEquals(3, course.getUnits().size());

        ChapterUnitDto chapter1 = (ChapterUnitDto) course.getUnits().get(0);
        ChapterUnitDto chapter2 = (ChapterUnitDto) course.getUnits().get(1);
        ChapterUnitDto chapter3 = (ChapterUnitDto) course.getUnits().get(2);

        verifyUnit(chapter1, 159l, "chapter_1", CourseUnitState.Active.toString(), Constants.CHAPTER);
        verifyUnit(chapter2, 160l, "chapter_2", CourseUnitState.Active.toString(), Constants.CHAPTER);
        verifyUnit(chapter3, 161l, "chapter_3", CourseUnitState.Active.toString(), Constants.CHAPTER);

        assertNull(chapter1.getQuiz());
        verifyUnit(chapter2.getQuiz(), 152l, "quiz_2", CourseUnitState.Active.toString(), Constants.QUIZ);
        verifyUnit(chapter3.getQuiz(), 153l, "quiz_3", CourseUnitState.Active.toString(), Constants.QUIZ);
        assertNull(chapter2.getQuiz().getUnits());
        assertNull(chapter3.getQuiz().getUnits());

        assertEquals(2, chapter1.getUnits().size());
        CourseUnitDto lesson1 = chapter1.getUnits().get(0);
        CourseUnitDto lesson2 = chapter1.getUnits().get(1);

        verifyUnit(lesson1, 154l, "lesson_1", CourseUnitState.Active.toString(), Constants.LESSON);
        verifyUnit(lesson2, 155l, "lesson_2", CourseUnitState.Active.toString(), Constants.LESSON);
        assertNull(lesson1.getUnits());
        assertNull(lesson2.getUnits());

        assertEquals(1, chapter2.getUnits().size());
        CourseUnitDto lesson3 = chapter2.getUnits().get(0);

        verifyUnit(lesson3, 156l, "lesson_3", CourseUnitState.Active.toString(), Constants.LESSON);
        assertNull(lesson3.getUnits());

        assertEquals(0, chapter3.getUnits().size());
    }

    private void verifyUnit(CourseUnitDto unit, long id, String name, String state, String type) {
        assertEquals(id, unit.getId());
        assertEquals(name, unit.getName());
        assertEquals(state, unit.getState());
        assertEquals(type, unit.getType());
    }

    private JsonParser getParser(String resourcePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory(mapper);

        StringWriter writer = new StringWriter();
        IOUtils.copy(this.getClass().getResourceAsStream(resourcePath), writer);

        return jsonFactory.createJsonParser(writer.toString());
    }
}
