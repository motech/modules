package org.motechproject.sms.templates;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TemplateTest {

    @Test
    public void shouldGeneratePostMethod() throws IOException {
        String jsonTemplate = "{\n" +
                "        \"name\":\"Voto\",\n" +
                "        \"configurables\": [\n" +
                "            \"api_key\"\n" +
                "        ],\n" +
                "        \"outgoing\":{\n" +
                "            \"maxSmsSize\":\"160\",\n" +
                "            \"millisecondsBetweenMessages\":\"1\",\n" +
                "            \"exponentialBackOffRetries\":\"true\",\n" +
                "            \"maxRecipient\":\"1\",\n" +
                "            \"hasAuthentication\":\"false\",\n" +
                "            \"request\":{\n" +
                "                \"type\":\"POST\",\n" +
                "                \"urlPath\":\"https://url\",\n" +
                "                \"jsonContentType\":\"true\",\n" +
                "                \"bodyParameters\":{\n" +
                "                    \"status_callback_url\":\"[callback]\",\n" +
                "                    \"subscribers\":\"[subscribers]\"\n" +
                "                }\n" +
                "            },\n" +
                "            \"response\":{\n" +
                "            }\n" +
                "        },\n" +
                "        \"status\":{\n" +
                "        },\n" +
                "        \"incoming\":{\n" +
                "        }\n" +
                "    }";
        Gson gson = new Gson();
        Template template = gson.fromJson(jsonTemplate, new TypeToken<Template>() { } .getType());

        String expectedJson = "{\"status_callback_url\":\"http://:someUrl\",\"subscribers\":[{\"phone\":\"48700123123\",\"language\":\"en\"}]}";

        Map<String, String> props = new HashMap<>();
        props.put("message", "message");
        props.put("recipients", "1");
        props.put("callback", "http://:someUrl");
        props.put("subscribers", "[{\"phone\":\"48700123123\",\"language\":\"en\"}]");

        HttpMethod httpMethod = template.generateRequestFor(props);

        RequestEntity requestEntity = ((PostMethod) httpMethod).getRequestEntity();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        requestEntity.writeRequest(bos);
        String json = new String(bos.toByteArray(), "UTF-8");

        Assert.assertEquals(expectedJson, json);
    }
}
