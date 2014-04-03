package org.motechproject.transliteration.service;

import junit.framework.Assert;
import org.junit.Test;
import org.motechproject.transliteration.service.TransliterationServiceImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by kosh on 3/13/14.
 */
public class TransliterationServiceTests {
    TransliterationServiceImpl engine = new TransliterationServiceImpl();

    @Test
    public void NotNullNameTest()
    {
        String result = engine.transliterate("asha sen");
        Assert.assertNotNull(result);
        Assert.assertEquals("अशा सेन", result);
    }

    @Test
    public void CaseInsensitiveNameTest() {
        String result1 = engine.transliterate("asha sen");
        String result2 = engine.transliterate("ASha SEN");
        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertEquals(result1, result2);
        Assert.assertEquals("अशा सेन", result1);
    }

    @Test
    public void SpaceNameTest() {
        String result = engine.transliterate("asha sen");
        Assert.assertEquals(2, result.split(" ").length);
    }

    /*
    @Test
    public void BulkDataTest() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("/home/kosh/dev/sample.txt"));
            String line = br.readLine();
            StringBuilder results = new StringBuilder();

            while (line != null) {
                String result = service.transliterate(line);
                results.append(result);
                results.append(System.lineSeparator());
                line = br.readLine();
            }

            Assert.assertNotNull(results);
            System.out.println(results.toString());
        } catch (FileNotFoundException fe) {
            Assert.fail();
        } catch (IOException ie) {

        }

        /*
        try {
            FileInputStream inputStream = new FileInputStream("/home/kosh/dev/sample.txt");
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            DataInputStream dis = new DataInputStream(bis);

            while (dis.available() != 0) {
                System.out.println(dis.readLine());
            }
        } catch (FileNotFoundException fe) {
            Assert.fail();
        } catch (IOException ie) {
            Assert.fail();
        }


    }*/
}
