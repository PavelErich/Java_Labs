import java.util.ArrayDeque;
import java.util.HashSet;

public class Crawler {
    //Строковые константы
    private static final String OPEN_TAG = "<a ";
    private static final String CLOSE_TAG = "</a>";
    private static final String PAR_HREF = "href=\"";

    //Максимальная глубина
    private int max_depth;
    //Глубина в данный момент
    private int curr_depth;
    //Url сайта на котором происходит parse
    private Url curr_url;

    //Посещенные сайты
    private final HashSet<URLDepthPair> visited;
    //Не посещенные сайты
    private final ArrayDeque<URLDepthPair> no_visited;

    public Crawler() {
        visited = new HashSet<>();
        no_visited = new ArrayDeque<>();
    }

    //Функция принимает  страницу и добавляет
    //все найденные ссылки в no_visited
    private void parseUrls(StringBuilder page) {
        int open = page.indexOf(OPEN_TAG);
        int close = page.indexOf(CLOSE_TAG);
        int href, close_href;
        while(open != -1) {
            href = page.indexOf(PAR_HREF, open + OPEN_TAG.length());
            if(href != -1 && href < close && href > open) {
                close_href = page.indexOf("\"", href + PAR_HREF.length());
                addValidUrl(page.substring(href + PAR_HREF.length(), close_href));
            }
            open = page.indexOf(OPEN_TAG, close + CLOSE_TAG.length());
            close = page.indexOf(CLOSE_TAG, open + OPEN_TAG.length());
        }
    }

    //Дополнительная функция, проверяющая валидной Url
    //и добавление в no_visited
    private void addValidUrl(String link) {
        link = link.trim();
        Url url;
        if(link.startsWith("/"))
            url = new Url(curr_url.getFullHost() + link);
        else if(link.startsWith("http"))
            url = new Url(link);
        else return;
        URLDepthPair udp = new URLDepthPair(url, curr_depth);
        no_visited.add(udp);
    }

    //Функция рекурсивно берет сайт из
    //no_visited парся его.
    private void getSites() {
        //Если достигли max_depth - 1, то заканчиваем парсинг
        if(curr_depth == max_depth - 1){
            while(!no_visited.isEmpty())
                visited.add(no_visited.pollFirst());
            return;
        }
        //Или если no_visited пустой
        if(no_visited.size() == 0)
            return;
        StringBuilder page;
        int size = no_visited.size();
        curr_depth++;
        for(int i = 0; i < size; i++) {
            URLDepthPair el = no_visited.pollFirst();
            if(visited.contains(el) || el == null) continue;
            visited.add(el);
            System.out.println("Current depth: " + curr_depth + " Index site: " + i + " Visited size: " + visited.size());
            curr_url = el.getUrl();
            page = Request.getPage(curr_url);
            if(page != null) parseUrls(page);
        }

        getSites();
    }

    //Принимает ссылку на первый сайт в формате protocol://host:port/path
    //и максимальную глубину для парсинга
    public HashSet<URLDepthPair> getSites(String site, int _max_depth) {
        if(_max_depth < 1)
            throw new IllegalArgumentException("Depth cannot be less than 1");
        visited.clear();
        no_visited.clear();
        curr_depth = 0;
        curr_url = new Url(site);
        max_depth = _max_depth;
        addValidUrl(site);
        getSites();
        return visited;
    }
}