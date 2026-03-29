package ru.cwcode.cwutils.files;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;

public final class PastesDevClient {
  
  private static final String DEFAULT_URL = "https://pastes.dev";
  private static final String DEFAULT_API_URL = "https://api.pastes.dev";
  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
  private static final String USER_AGENT = "Shared Paste Client (github.com/KamikotoTkach)";
  
  private static final HttpClient SHARED_CLIENT = HttpClient.newBuilder()
                                                            .version(HttpClient.Version.HTTP_2)
                                                            .connectTimeout(DEFAULT_TIMEOUT)
                                                            .build();
  
  private PastesDevClient() {
    throw new UnsupportedOperationException("Utility class");
  }
  
  /**
   * Создает новый paste на pastes.dev
   *
   * @param content  содержимое paste
   * @param language язык синтаксиса (примеры в PasteLanguage)
   * @return URL созданного paste
   * @throws IOException          если произошла ошибка сети или получен статус ошибки
   * @throws InterruptedException если запрос был прерван
   */
  public static String createPaste(String content, String language)
    throws IOException, InterruptedException {
    return createPaste(content, language, DEFAULT_API_URL, DEFAULT_URL);
  }
  
  /**
   * Создает новый paste на своём инстансе PastesDev
   */
  public static String createPaste(String content, String language, String apiUrl, String baseUrl)
    throws IOException, InterruptedException {
    PasteResult result = createPasteDetailed(content, language, apiUrl, baseUrl);
    
    if (result.isSuccess() && result.url() != null) {
      return result.url();
    }
    throw new IOException("Failed to create paste. Status: " +
                          result.statusCode() + ", Error: " + result.errorBody());
  }
  
  /**
   * Получает содержимое paste с pastes.dev
   *
   * @param keyOrUrl ключ (например, abc123) или полный URL paste
   * @return содержимое paste, либо null, если paste не найден (404)
   * @throws IOException          при ошибке сети или другом статусе ошибки (5xx)
   * @throws InterruptedException если запрос был прерван
   */
  public static String getPaste(String keyOrUrl)
    throws IOException, InterruptedException {
    return getPaste(keyOrUrl, DEFAULT_API_URL);
  }
  
  /**
   * Получает содержимое paste со своего инстанса PastesDev
   */
  public static String getPaste(String keyOrUrl, String apiUrl)
    throws IOException, InterruptedException {
    PasteResult result = getPasteDetailed(keyOrUrl, apiUrl);
    
    if (result.isSuccess()) {
      return result.content();
    }
    
    if (result.statusCode() == 404) {
      return null;
    }
    
    throw new IOException("Failed to get paste. Status: " +
                          result.statusCode() + ", Error: " + result.errorBody());
  }
  
  public static PasteResult createPasteDetailed(String content, String language)
    throws IOException, InterruptedException {
    return createPasteDetailed(content, language, DEFAULT_API_URL, DEFAULT_URL);
  }
  
  public static PasteResult createPasteDetailed(String content, String language, String apiUrl, String baseUrl)
    throws IOException, InterruptedException {
    
    validateContent(content);
    validateLanguage(language);
    
    String normalizedApiUrl = normalizeBaseUrl(requireNonBlank(apiUrl, "apiUrl"));
    String normalizedUrl = normalizeBaseUrl(requireNonBlank(baseUrl, "baseUrl"));
    
    HttpRequest request = HttpRequest.newBuilder()
                                     .uri(URI.create(normalizedApiUrl + "/post"))
                                     .header("Content-Type", "text/" + language)
                                     .header("User-Agent", USER_AGENT)
                                     .POST(HttpRequest.BodyPublishers.ofString(content))
                                     .timeout(DEFAULT_TIMEOUT)
                                     .build();
    
    HttpResponse<String> response = SHARED_CLIENT.send(
      request,
      HttpResponse.BodyHandlers.ofString()
    );
    
    if (response.statusCode() == 201) {
      String location = response.headers().firstValue("Location").orElse(null);
      if (location != null && !location.isBlank()) {
        String finalUrl = location.startsWith("http") ? location : normalizedUrl + "/" + trimLeadingSlashes(location);
        String key = extractPasteKey(location);
        return new PasteResult(response.statusCode(), key, finalUrl, null, null);
      }
    }
    
    return new PasteResult(response.statusCode(), null, null, null, response.body());
  }
  
  public static PasteResult getPasteDetailed(String keyOrUrl)
    throws IOException, InterruptedException {
    return getPasteDetailed(keyOrUrl, DEFAULT_API_URL);
  }
  
  public static PasteResult getPasteDetailed(String keyOrUrl, String apiUrl)
    throws IOException, InterruptedException {
    
    String key = extractPasteKey(keyOrUrl);
    String normalizedApiUrl = normalizeBaseUrl(requireNonBlank(apiUrl, "apiUrl"));
    
    HttpRequest request = HttpRequest.newBuilder()
                                     .uri(URI.create(normalizedApiUrl + "/" + key))
                                     .header("User-Agent", USER_AGENT)
                                     .GET()
                                     .timeout(DEFAULT_TIMEOUT)
                                     .build();
    
    HttpResponse<String> response = SHARED_CLIENT.send(
      request,
      HttpResponse.BodyHandlers.ofString()
    );
    
    if (response.statusCode() == 200) {
      return new PasteResult(response.statusCode(), key, null, response.body(), null);
    }
    
    return new PasteResult(response.statusCode(), key, null, null, response.body());
  }
  
  private static void validateContent(String content) {
    Objects.requireNonNull(content, "content must not be null");
    if (content.isEmpty()) {
      throw new IllegalArgumentException("content must not be empty");
    }
  }
  
  private static void validateLanguage(String language) {
    requireNonBlank(language, "language");
  }
  
  private static String extractPasteKey(String keyOrUrl) {
    String value = requireNonBlank(keyOrUrl, "keyOrUrl").trim();
    value = trimTrailingSlashes(value);
    
    int lastSlashIndex = value.lastIndexOf('/');
    if (lastSlashIndex != -1) {
      value = value.substring(lastSlashIndex + 1);
    }
    
    if (value.isBlank()) {
      throw new IllegalArgumentException("Invalid paste key or URL provided");
    }
    return value;
  }
  
  private static String requireNonBlank(String value, String name) {
    Objects.requireNonNull(value, name + " must not be null");
    if (value.isBlank()) {
      throw new IllegalArgumentException(name + " must not be blank");
    }
    return value;
  }
  
  private static String normalizeBaseUrl(String baseUrl) {
    return trimTrailingSlashes(baseUrl.trim());
  }
  
  private static String trimTrailingSlashes(String value) {
    String result = value;
    while (result.endsWith("/")) {
      result = result.substring(0, result.length() - 1);
    }
    return result;
  }
  
  private static String trimLeadingSlashes(String value) {
    String result = value.trim();
    while (result.startsWith("/")) {
      result = result.substring(1);
    }
    return result;
  }
  
  public record PasteResult(int statusCode, String key, String url, String content, String errorBody) {
    public boolean isSuccess() {
      return statusCode >= 200 && statusCode < 300;
    }
  }
  
  @UtilityClass
  public static final class PasteLanguage {
    public final String PLAIN = "plain";
    public final String LOG = "log";
    public final String YAML = "yaml";
    public final String JSON = "json";
    public final String XML = "xml";
    public final String INI = "ini";
    public final String JAVA = "java";
    public final String JAVASCRIPT = "javascript";
    public final String TYPESCRIPT = "typescript";
    public final String PYTHON = "python";
    public final String KOTLIN = "kotlin";
    public final String SCALA = "scala";
    public final String CPP = "cpp";
    public final String CSHARP = "csharp";
    public final String SHELL = "shell";
    public final String RUBY = "ruby";
    public final String RUST = "rust";
    public final String SQL = "sql";
    public final String GO = "go";
    public final String LUA = "lua";
    public final String SWIFT = "swift";
    public final String C = "c";
    public final String HTML = "html";
    public final String CSS = "css";
    public final String SCSS = "scss";
    public final String PHP = "php";
    public final String GRAPHQL = "graphql";
    public final String DIFF = "diff";
    public final String DOCKERFILE = "dockerfile";
    public final String MARKDOWN = "markdown";
    public final String PROTO = "proto";
  }
}
