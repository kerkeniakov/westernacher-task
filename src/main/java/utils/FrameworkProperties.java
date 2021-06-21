package utils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FrameworkProperties {
    private String result = "";
    private InputStream InputStream;

    public String getProperty(String key)  {
        try {
            Properties properties = new Properties();
            String propertiesFileName = Constants.PROPERTIES_FILE_NAME;
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException(Constants.FILE_NOT_FOUND_MESSAGE);
            }
            String propertyValue= properties.getProperty(key);
            this.result = propertyValue;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
