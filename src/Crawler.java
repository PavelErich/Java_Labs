import java.net.*;
import java.io.*;
import java.util.LinkedList;
import java.lang.*;

public class Crawler {
    private final String OPEN_TAG = "<a";
    private final String CLOSE_TAG = "</a>";
    private final String PAR_HREF = "href=\"";
    private final String URL_PREFIX = "http://";

    public LinkedList<URLDepthPair> visited;
    public int max_depth = 4;

    public Crawler(){
        visited = new LinkedList<>();
    }

    public boolean isValidHref(String href){
        for(int i = 0; i < visited.size(); i++)
            if(visited.get(i).getUrl().equals(href))
                return false;
        return true;
    }

    public void getSites(String site, int depth) throws IOException {
        if(depth >= max_depth) return;
        URL url = new URL(site);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuffer page = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            //TODO: сразу анализировать ссылки
            page.append(line);
        }

        Integer open = page.indexOf(OPEN_TAG), close = 0;
        String link = "";
        while(open != -1){
            open = page.indexOf(PAR_HREF, open);
            close = page.indexOf("\"", open + PAR_HREF.length());
            if(open == -1 || close == -1 || page.indexOf(CLOSE_TAG, close) == -1)
                break;
            link = page.substring(open + PAR_HREF.length(), close);
            if(link.startsWith(URL_PREFIX))
                if(isValidHref(link)) {
                    visited.add(new URLDepthPair(link, depth));
                    getSites(link, depth + 1);
                }
            open = page.indexOf(OPEN_TAG, close);
        }

        reader.close();
    }

    public static void print_list(LinkedList<URLDepthPair> list){
        if(list == null || list.size() == 0)
            throw new IllegalArgumentException("List cannot be null or empty");
        for(int i = 0; i < list.size() - 1; i++)
            System.out.println(list.get(i) + ", ");
        System.out.print(list.get(list.size() - 1));
    }

    public static void main(String[] args) throws IOException {
        Crawler crawler = new Crawler();
        int depth = 0;
        String site = "http://bio.acousti.ca";
        LinkedList<URLDepthPair> list = new LinkedList<>();
        crawler.getSites(site, depth + 1);
        list.add(new URLDepthPair(site, depth));

        print_list(crawler.visited);
    }
}