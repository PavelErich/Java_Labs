import java.net.*;
import java.io.*;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.lang.*;

/*
Получить веб-страницу
Проанализировать её
Добавить найденные ссылки в no_visited (очередь) если только нет в visited
А при добавлении из no_visited проверяется нет ли её в visited. Два раза по O(1)
 */

public class Crawler {
    private static final String OPEN_TAG = "<a ";
    private static final String CLOSE_TAG = "</a>";
    private static final String PAR_HREF = "href=\"";
    private static final String URL_PREFIX = "http://";

    private int max_depth;
    private int curr_depth;

    private ArrayDeque<URLDepthPair> no_visited;
    private HashSet<URLDepthPair> visited;

    public Crawler(){
        visited = new HashSet<>();
        no_visited = new ArrayDeque<>();
    }

   private void parseUrls(StringBuffer page){
        int open = page.indexOf(OPEN_TAG);
        int href = 0, close = 0;
        while(open != -1){
            href = page.indexOf(PAR_HREF, open + OPEN_TAG.length());
            if(href != -1) {
                int chref = page.indexOf("\"", href + PAR_HREF.length());
                addValidUrl(page.substring(href + PAR_HREF.length(), chref));
            }
            close = page.indexOf(CLOSE_TAG, open + OPEN_TAG.length());
            open = page.indexOf(OPEN_TAG, close + CLOSE_TAG.length());
        }
    }

    private void addValidUrl(String url){
        url = url.trim();
        if(!url.startsWith(URL_PREFIX)) return;
        url = url.substring(URL_PREFIX.length());
        if(url.endsWith("/"))
            url = url.substring(0, url.length() - 1);
        URLDepthPair udp = new URLDepthPair(url, curr_depth);
        if(visited.contains(udp) || no_visited.contains(udp))
            return;
        if(url.endsWith(".jpg") || url.endsWith(".mp4"))
            visited.add(udp);
        else no_visited.add(udp);
    }

    private int split(String url){
        int index = url.indexOf("/");
        if(index == -1) return url.length();
        return index;
    }

    private StringBuffer getPage(String url){
        StringBuffer page = null;
        try {
            int index = split(url);
            String host = url.substring(0, index == url.length() ? index : index - 1);
            String site = url.substring(index == url.length() ? index : index + 1);
            Socket s = new Socket(host, 80);
            s.setSoTimeout(10000);
            PrintWriter wtr = new PrintWriter(s.getOutputStream());

            wtr.println("GET /"+site+" HTTP/1.1");
            wtr.println("Host: " + host + ":80");
            wtr.println("Connection: Close");
            wtr.println();
            wtr.flush();

            InputStreamReader isr = new InputStreamReader(s.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            page = new StringBuffer();
            String new_line = "";

            while ((new_line = reader.readLine()) != null)
                page.append(new_line);

            reader.close();
            wtr.close();
            s.close();
        } catch (Exception e){}
        return page;
    }

    private void getSites() {
        if(curr_depth == max_depth - 1){
            for(URLDepthPair el : no_visited)
                visited.add(el);
            no_visited.clear();
            return;
        }
        if(no_visited.size() == 0)
            return;
        StringBuffer page = null;
        int size = no_visited.size();
        curr_depth++;
        int test = 0;
        for(int i = 0; i < size; i++) {
            URLDepthPair tmp = no_visited.pollFirst();
            if(visited.contains(tmp)) continue;
            page = getPage(tmp.getUrl());
            visited.add(tmp);
            if(page != null && page.indexOf("HTTP/1.1 200") != -1)
                parseUrls(page);
        }

        getSites();
    }

    public HashSet<URLDepthPair> getSites(String site, int _max_depth) {
        if(_max_depth < 1)
            throw new IllegalArgumentException("Depth cannot be less than 1");
        if(!site.startsWith(URL_PREFIX))
            throw new IllegalArgumentException("Url cannot starts with not http://");
        visited.clear();
        no_visited.clear();
        curr_depth = 0;
        max_depth = _max_depth;
        addValidUrl(site);
        getSites();
        return visited;
    }
}