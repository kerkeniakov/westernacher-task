import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.Constants;
import utils.FrameworkProperties;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static io.qameta.allure.Allure.step;

public class HTTPRequestTest {
    public String access_token;
    static FrameworkProperties frameworkProperties;
    @BeforeEach
    public void setup()  {
        frameworkProperties = new FrameworkProperties();

    }

    @Test
    @DisplayName("API tests")
    public void testLogin() throws IOException {
        step("1. Test that OATH endpoint returns access token", () -> {
            URL url = new URL("https://test.easyleave.de:8081/oauth/token");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("authority", "test.easyleave.de:8081");
            con.setRequestProperty("accept", "application/json, text/plain, */*");
            con.setRequestProperty("authorization", frameworkProperties.getProperty(Constants.AUTHORIZATION_BEARER_TOKEN));
            con.setRequestProperty("sec-ch-ua-mobile", "?0");
            con.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            con.setRequestProperty("origin", "https://test.easyleave.de");
            con.setRequestProperty("sec-fetch-site", "same-site");
            con.setRequestProperty("sec-fetch-mode", "cors");
            con.setRequestProperty("sec-fetch-dest", "empty");
            con.setRequestProperty("referer", "https://test.easyleave.de/");
            con.setRequestProperty("accept-language", "en-US,en;q=0.9");
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            String jsonInputString =frameworkProperties.getProperty(Constants.HTTP_JSON_TEST_DATA) ;
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            int responseCode = con.getResponseCode();
            InputStreamReader inputStreamReader = null;
            if (responseCode >= 200 && responseCode < 400) {
                inputStreamReader = new InputStreamReader(con.getInputStream());
            } else {
                inputStreamReader = new InputStreamReader(con.getErrorStream());
            }
            BufferedReader in = new BufferedReader(inputStreamReader);
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONObject jsonObject = new JSONObject(response.toString());
            System.out.println("Step 1 http response: "+responseCode +"," + response.toString());
            access_token = jsonObject.getString("access_token");
            Assertions.assertEquals(responseCode, 200);
            Assertions.assertTrue(response.toString().contains("access_token"));
        });
        step("2. Do a get request to /userDetails endpoint and validate that response contains \"lastName\":\"Kreuf\"", () -> {
            URL url = new URL("https://test.easyleave.de:8081/userDetails");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestProperty("authority", "test.easyleave.de:8081");
            con.setRequestProperty("accept", "application/json, text/plain, */*");
            con.setRequestProperty("authorization", "Bearer "+ access_token);
            con.setRequestProperty("accept-language", "en");
            con.setRequestProperty("sec-ch-ua-mobile", "?0");
            con.setRequestProperty("origin", "https://test.easyleave.de");
            con.setRequestProperty("sec-fetch-site", "same-site");
            con.setRequestProperty("sec-fetch-mode", "cors");
            con.setRequestProperty("sec-fetch-dest", "empty");
            con.setRequestProperty("referer", "https://test.easyleave.de/");
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            System.out.println("Response code: " + responseCode);

            InputStreamReader inputStreamReader = null;
            if (responseCode >= 200 && responseCode < 400) {
                inputStreamReader = new InputStreamReader(con.getInputStream());
            } else {
                inputStreamReader = new InputStreamReader(con.getErrorStream());
            }
            BufferedReader in = new BufferedReader(inputStreamReader);
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("Step 2 http response: "+responseCode +"," + response.toString());
            Assertions.assertEquals(responseCode, 200);
            Assertions.assertTrue(response.toString().contains("\"lastName\":\"Kreuf\""));
        });
    }
}
