package sample.FileConverter;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ObjectToJsonConverter {

    public static void fromObjectToJson(String nameFile, Object object) {
        ObjectMapper mapper = new ObjectMapper();

        File file = new File(nameFile);
        try {
            // Serialize Java object info JSON file.
            mapper.writeValue(file, object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Object fromJsonToObject(String nameFile, Class classTest) {

        File file = new File(nameFile);
        ObjectMapper mapper = new ObjectMapper();

        Object object = null;
        try {
            // Deserialize JSON file into Java object.
            object = classTest.newInstance();
            object = mapper.readValue(file, classTest);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return object;
    }
}
