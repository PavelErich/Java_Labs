import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Request {
    //Функция возвращающая веб-страницу
    static public StringBuilder getPage(Url url) {
        StringBuilder page = null;
        if(url.getProtocol().equals("http")) {
            Socket socket = null;
            BufferedReader reader = null;
            PrintWriter p_writer = null;
            try {
                //Создаем сокет
                socket = new Socket(url.getHost(), url.getPort());
                socket.setSoTimeout(1500);
                //Отправляем гет запрос
                p_writer = new PrintWriter(socket.getOutputStream());
                Request.sendGet(p_writer, url);
                //Получаем результат
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String tmp;
                tmp = reader.readLine();
                //Если код ошибки 200 и Content-Type = text/html, то возвращается
                //веб-страница, иначе null
                if (tmp.startsWith("HTTP/1.1 200")) {
                    while (!tmp.startsWith("Content-Type: "))
                        tmp = reader.readLine();
                    if (!tmp.substring(14).startsWith("text/html"))
                        return null;
                    page = new StringBuilder();
                    while ((tmp = reader.readLine()) != null)
                        page.append(tmp);
                }
                //Если код ошибки 301 и в ответе указан url редиректа
                //то возвращаем сайт редиректа
                else if(tmp.startsWith("HTTP/1.1 301")) {
                    while(tmp.length() != 0 && !tmp.startsWith("Location: "))
                        tmp = reader.readLine();
                    if(tmp.length() != 0) {
                        Url temp = new Url(tmp.substring(10));
                        if(temp.getProtocol().equals("http"))
                            page = getPage(temp);
                    }
                }
            }
            catch (Exception e) { page = null; }

            try {
                if(socket != null) socket.close();
                if(reader != null) reader.close();
                if(p_writer != null) p_writer.close();
            } catch (Exception e) { page = null; }
        }
        return page;
    }

    //Функция отправляет GET запрос сайту Url
    static private void sendGet(PrintWriter p_writer, Url url) {
        p_writer.println("GET " + url.getPath() + " HTTP/1.1");
        p_writer.println("Host: " + url.getHost() + ":" + url.getPort());
        p_writer.println("Connection: Close");
        p_writer.println();
        p_writer.flush();
    }
}