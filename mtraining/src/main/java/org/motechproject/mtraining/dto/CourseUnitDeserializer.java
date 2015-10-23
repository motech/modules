package org.motechproject.mtraining.dto;


import org.apache.commons.beanutils.BeanUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.motechproject.mtraining.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code CourseUnitDeserializer} for the {@code List<CourseUnitDto>}.
 */
public class CourseUnitDeserializer extends JsonDeserializer<List<CourseUnitDto>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseUnitDeserializer.class);

    @Override
    public List<CourseUnitDto> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode jsonNode = jsonParser.readValueAsTree();
        List<CourseUnitDto> units = new ArrayList<>();

        ObjectCodec codec = jsonParser.getCodec();
        ObjectMapper mapper;

        if (codec instanceof ObjectMapper) {
            mapper = (ObjectMapper) codec;
        } else {
            mapper = new ObjectMapper();
        }

        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType stringType = typeFactory.constructType(String.class);
        JavaType longType = typeFactory.constructType(Long.class);
        JavaType courseType = typeFactory.constructType(CourseUnitDto.class);
        JavaType coursesType = typeFactory.constructCollectionType(List.class, CourseUnitDto.class);
        JavaType chaptersType = typeFactory.constructCollectionType(List.class, ChapterUnitDto.class);

        for (JsonNode unit : jsonNode) {
            String type = mapper.readValue(unit.get("type"), stringType);
            CourseUnitDto unitDto;

            if (Constants.CHAPTER.equals(type)) {
                unitDto = new ChapterUnitDto();
            } else {
                unitDto = new CourseUnitDto();
            }

            setProperty("id", longType, unitDto, unit, mapper);
            setProperty("name", stringType, unitDto, unit, mapper);
            setProperty("state", stringType, unitDto, unit, mapper);
            setProperty("type", stringType, unitDto, unit, mapper);


            if (Constants.COURSE.equals(type)) {
                setProperty("units", chaptersType, unitDto, unit, mapper);
            } else if (Constants.CHAPTER.equals(type)) {
                setProperty("quiz", courseType, unitDto, unit, mapper);
                setProperty("units", coursesType, unitDto, unit, mapper);
            } else {
                setProperty("units", coursesType, unitDto, unit, mapper);
            }
            units.add(unitDto);
        }
        return units;
    }

    private void setProperty(String propertyName, JavaType javaType, CourseUnitDto unitDto, JsonNode node, ObjectMapper mapper) {
        if (node.has(propertyName)) {
            try {
                Object value = mapper.readValue(node.get(propertyName), javaType);
                BeanUtils.setProperty(unitDto, propertyName, value);
            } catch (IllegalAccessException | InvocationTargetException | IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
