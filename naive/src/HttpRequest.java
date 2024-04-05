import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    public final static String DELIMITER = "\r\n\r\n"; //разделение заголовка от тела
    public final static String NEW_LINE = "\r\n";
    public final static String HEADER_DELIMITER = ":";
    private String message;
    private final HttpMethod method;
    private final String url;
    private final Map<String, String> headers;
    private final String body;
    public HttpRequest(String message) {
        this.message = message;
        String[] parts = message.split(DELIMITER);
        String head = parts[0];

        String[] headers = head.split(NEW_LINE);
        String[] firstLine = headers[0].split(" ");// первая строчка
        method = HttpMethod.valueOf(firstLine[0]);
        url = firstLine[1];

        this.headers = Collections.unmodifiableMap(
                new HashMap<>() {{
                    for (int i=0; i<headers.length; i++){
                        String[] headerPart = headers[i].split(HEADER_DELIMITER, 2);// максимум 2 элемента в результате сплита
                        put(headerPart[0].trim(), headerPart[1].trim());
                    }
                }}
        );
        String bodyLength = this.headers.get("Content-Length");
        int length = bodyLength != null ? Integer.parseInt(bodyLength) : 0;
        this.body = parts.length > 1 ? parts[1].trim().substring(0, length) : "";
    }
}
