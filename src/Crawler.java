import java.net.*;
import java.io.*;
import java.util.LinkedList;
import java.lang.*;

public class Crawler {
    private static final String OPEN_TAG = "<a ";
    private static final String CLOSE_TAG = "</a>";
    private static final String PAR_HREF = "href=\"";
    private static final String URL_PREFIX = "http://";

    private LinkedList<URLDepthPair> visited;

    public Crawler(){ visited = new LinkedList<>(); }

    //Функция принимает веб-страницу, глубину и максимальную глубину
    //Функция анализирует веб-страницу, отмечая найденные ссылки
    //visited и вызывая для них функцию getSites
    private void goToValidSites(StringBuffer page, int depth, int max_depth) {
        int open = page.indexOf(OPEN_TAG), close = 0;
        String link = "";
        while (open != -1) {
            open = page.indexOf(PAR_HREF, open);
            close = page.indexOf("\"", open + PAR_HREF.length());
            if (open == -1 || close == -1 || page.indexOf(CLOSE_TAG, close) == -1)
                break;
            link = page.substring(open + PAR_HREF.length(), close);
            if (link.startsWith(URL_PREFIX))
                if (!visited.contains(new URLDepthPair(link, 1))) {
                    visited.add(new URLDepthPair(link, depth));
                    getSites(link, depth + 1, max_depth);
                    //Вывод сделан для того, чтобы можно было наблюдать за процессом
                    System.out.println("[URL: " + link + ", Depth: " + depth + "]");
                }
            open = page.indexOf(OPEN_TAG, close);
        }
    }

    //Функция принимает url сайта, глубину и максимальную глубину
    //Вспомогательная рекурсивная функция, которая получает веб-страницу
    //по url и вызывает функцию goToValidSites для её анализа
    private void getSites(String site, int depth, int max_depth) {
        try {
            if (depth >= max_depth) return;
            URL url = new URL(site);
            URLConnection conn = url.openConnection();
            conn.setReadTimeout(10000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuffer page = new StringBuffer();

            while ((line = reader.readLine()) != null)
                page.append(line);
            reader.close();

            goToValidSites(page, depth, max_depth);
        }
        catch (Exception e) { }
    }

    //Функция принимает ссылку на исходный сайт и максимальную глубину поиска
    //Возвращает список посещенных сайтов
    public LinkedList<URLDepthPair> getSites(String site, int max_depth) {
        if(max_depth < 1)
            throw new IllegalArgumentException("Depth cannot be less than 1");
        if(!site.startsWith(URL_PREFIX))
            throw new IllegalArgumentException("Url cannot starts with not http://");
        visited.clear();
        visited.add(new URLDepthPair(site, 0));
        System.out.println("[URL: " + site + ", Depth: " + 0 + "]");
        getSites(site, 1, max_depth);
        return visited;
    }
}