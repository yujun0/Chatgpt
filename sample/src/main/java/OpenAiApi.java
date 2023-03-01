
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class OpenAiApi {

    public static void main(String[] args) throws IOException, InterruptedException {
        var scanner = new Scanner(System.in);
        System.out.println("請輸入要問的事情：");
        String prompt = scanner.nextLine();
        System.out.println("請輸入回答創意程度(0(保守/實際)~1(創新/無序)：");
        double temperature = scanner.nextDouble();
        // 這邊輸入自己的token
        String apiKey = "YOUR_TOKEN";
        String apiUrl = "https://api.openai.com/v1/completions";

        var httpClient = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\n" +
                                "  \"model\": \"text-davinci-003\",\n" +
                                "  \"prompt\": \"" + prompt + "\",\n" +
                                "  \"max_tokens\": 500,\n" +
                                "  \"temperature\": " + temperature + "\n" +
                                "}")).build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonStr = response.body();
        System.out.println(jsonStr);

        var gson = new Gson();
        var jsonObject = gson.fromJson(jsonStr, JsonObject.class);

        JsonArray choices = jsonObject.getAsJsonArray("choices");
        String text = choices.get(0).getAsJsonObject().get("text").getAsString().replaceAll("\n", "").trim();
        JsonObject usage = jsonObject.getAsJsonObject("usage");
        int totalTokens = usage.get("total_tokens").getAsInt();

        System.out.println("Ans: " + text);
        System.out.println("Total Tokens: " + totalTokens);
    }

}
