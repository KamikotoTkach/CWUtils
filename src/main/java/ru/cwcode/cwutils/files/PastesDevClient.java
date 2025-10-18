package ru.cwcode.cwutils.files;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PastesDevClient {
  
  private static final String DEFAULT_URL = "https://pastes.dev";
  private static final String DEFAULT_API_URL = "https://api.pastes.dev";
  
  private static final HttpClient SHARED_CLIENT = HttpClient.newBuilder()
                                                            .version(HttpClient.Version.HTTP_2)
                                                            .build();
  
  /**
   * Создает новый paste на pastes.dev
   *
   * @param content  содержимое paste
   * @param language язык синтаксиса (примеры в PasteLanguage)
   * @return URL     созданного paste
   * @throws IOException          если произошла ошибка при отправке запроса
   * @throws InterruptedException если запрос был прерван
   */
  public static String createPaste(String content, String language)
    throws IOException, InterruptedException {
    return createPaste(content, language, DEFAULT_API_URL, DEFAULT_URL);
  }
  
  /**
   * Создает новый paste на своём инстансе PastesDev
   *
   * @param content  содержимое paste
   * @param language язык синтаксиса (примеры в PasteLanguage)
   * @param apiUrl   url api собственного инстанса PastesDev
   * @param url      url собственного инстанса PastesDev
   * @return URL созданного paste
   * @throws IOException          если произошла ошибка при отправке запроса
   * @throws InterruptedException если запрос был прерван
   */
  public static String createPaste(String content, String language, String apiUrl, String url)
    throws IOException, InterruptedException {
    
    HttpRequest request = HttpRequest.newBuilder()
                                     .uri(URI.create(apiUrl + "/post"))
                                     .header("Content-Type", "text/" + language)
                                     .header("User-Agent", "Shared Paste Client (github.com/KamikotoTkach)")
                                     .POST(HttpRequest.BodyPublishers.ofString(content))
                                     .build();
    
    HttpResponse<String> response = SHARED_CLIENT.send(
      request,
      HttpResponse.BodyHandlers.ofString()
    );
    
    if (response.statusCode() == 201) {
      String location = response.headers()
                                .firstValue("Location")
                                .orElse(null);
      
      if (location != null) {
        return url + "/" + location;
      }
    }
    
    throw new IOException("Failed to create paste. Status: " +
                          response.statusCode() + ", Body: " + response.body());
  }
  
  public static class PasteLanguage {
    public static String plain = "plain";
    public static String log = "log";
    public static String yaml = "yaml";
    public static String json = "json";
    public static String xml = "xml";
    public static String ini = "ini";
    public static String java = "java";
    public static String javascript = "javascript";
    public static String typescript = "typescript";
    public static String python = "python";
    public static String kotlin = "kotlin";
    public static String scala = "scala";
    public static String cpp = "cpp";
    public static String csharp = "csharp";
    public static String shell = "shell";
    public static String ruby = "ruby";
    public static String rust = "rust";
    public static String sql = "sql";
    public static String go = "go";
    public static String lua = "lua";
    public static String swift = "swift";
    public static String c = "c";
    public static String html = "html";
    public static String css = "css";
    public static String scss = "scss";
    public static String php = "php";
    public static String graphql = "graphql";
    public static String diff = "diff";
    public static String dockerfile = "dockerfile";
    public static String markdown = "markdown";
    public static String proto = "proto";
  }
}
