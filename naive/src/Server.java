import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

class Server {
    private final static int BUFFER_SIZE = 256;
    private AsynchronousServerSocketChannel server;
    private final static String HEADERS =
            "HTTP/1.1 200 OK\n" +
                    "Host: native\n" +
                    "Content-Type: text/html\n" +
                    "Content-Length: %s\n" +
                    "Conection: close\n\n";
    private final HttpHandler handler;

    Server(HttpHandler handler) {
        this.handler = handler;
    }

    public void bootstrap() {
        try {
            server = AsynchronousServerSocketChannel.open();
            server.bind(new InetSocketAddress("127.0.0.1", 8088)); //установим сервер на адрес

            Future<AsynchronousSocketChannel> future = server.accept(); // возвращает соедиение (если клиент постучится)
            System.out.println("New client connection");

            AsynchronousSocketChannel clientChannel = future.get();

            while (clientChannel != null && clientChannel.isOpen()) {
                ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
                StringBuilder builder = new StringBuilder();
                boolean keepReading = true;

                while (keepReading) {
                    int readResult = clientChannel.read(buffer).get();

                    int position = buffer.position();
                    keepReading = readResult == BUFFER_SIZE;
                    buffer.flip(); // вернёт курсор в буфере на начальную позицию
                    CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer);//декодируем буфер
                    builder.append(charBuffer);
                    buffer.clear();
                }
                HttpRequest request = new HttpRequest(builder.toString());
                HttpResponse response = new HttpResponse();
                String body = this.handler.handle(request, response);

                String page = String.format(HEADERS, body.length()) + body;
                ByteBuffer resp = ByteBuffer.wrap(page.getBytes());
                clientChannel.write(resp);
                clientChannel.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
