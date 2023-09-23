import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherAPI {
    public static void main(String[] args) {
        try {
            // Replace "YOUR_API_KEY" with your actual OpenWeatherMap API key
            String apiKey = "33e8ae3fda651daa489a6241e3acd2d4";

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your ZIP code: ");
            String zipCode = scanner.nextLine();
            scanner.close();

            String apiUrl = "https://api.openweathermap.org/data/2.5/weather?zip=" + zipCode + "&appid=" + apiKey;


            // Create an HTTP connection
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response as plain text
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Extract the temperature using regular expressions
                String responseText = response.toString();
                Pattern pattern = Pattern.compile("\"temp\":(\\d+\\.\\d+)");
                Matcher matcher = pattern.matcher(responseText);

                if (matcher.find()) {
                    double kelvinTemperature = Double.parseDouble(matcher.group(1));
                    double fahrenheitTemperature = (kelvinTemperature - 273.15) * 9 / 5 + 32;
                    System.out.printf("Temperature in %s: %.2f degrees Fahrenheit%n", zipCode, fahrenheitTemperature);
                } else {
                    System.out.println("Temperature not found in the response.");
                }
            } else {
                System.out.println("Error: HTTP Response Code " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
