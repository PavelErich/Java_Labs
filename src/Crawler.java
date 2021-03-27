import java.util.HashSet;

public class Crawler {
    //Строковые константы
    private static final String OPEN_TAG = "<a ";
    private static final String CLOSE_TAG = "</a>";
    private static final String PAR_HREF = "href=\"";

    //Максимальная глубина поиска
    private int max_depth;

    //Массив потоков
    private CrawlerTask[] tasks;
    public Crawler() {
        tasks = null;
    }

    //Класс задача
    private class CrawlerTask extends Thread {
        //Пул сайтов
        private final URLPool pool;
        //Рассматриваемый URLDepthPair
        private URLDepthPair curr_pair;
        //Статус потока - isFree = true - свободен
        private boolean isFree;
        public CrawlerTask(URLPool pool) {
            this.pool = pool;
            this.curr_pair = null;
            this.isFree = true;
        }

        public synchronized void run() {
            while (!isInterrupted()) {
                //isFree = true;
                //Берем сайт из пула
                try { curr_pair = pool.poll(); }
                catch (Exception e) { isFree = true; continue; }
                //Если пул был не пустой, то добавляем сайт в массив посещеных сайтов
                pool.push_visited(curr_pair);
                //Если глубина < max_depth - 1, то парсим этот сайт
                if(curr_pair.getDepth() < max_depth - 1) {
                    isFree = false;
                    StringBuilder page = Request.getPage(curr_pair.getUrl());
                    if (page != null) parseUrls(page);
                }
                isFree = true;
            }
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
        private synchronized void addValidUrl(String link) {
            link = link.trim();
            Url url;
            if(link.startsWith("/"))
                url = new Url(curr_pair.getUrl().getFullHost() + link);
            else if(link.startsWith("http"))
                url = new Url(link);
            else return;
            URLDepthPair udp = new URLDepthPair(url, curr_pair.getDepth() + 1);
            pool.push(udp);
        }
    }

    //Принимает ссылку на первый сайт в формате protocol://host:port/path
    //и максимальную глубину для парсинга
    public HashSet<URLDepthPair> getSites(String site, int _max_depth, int count_tasks) {
        if(_max_depth < 1)
            throw new IllegalArgumentException("Depth cannot be less than 1");
        max_depth = _max_depth;
        tasks = new CrawlerTask[count_tasks];

        URLPool pool = new URLPool();
        pool.push(new URLDepthPair(new Url(site), 0));
        //Запускаем поток
        for(int i = 0; i < count_tasks; i++) {
            tasks[i] = new CrawlerTask(pool);
            tasks[i].start();
        }
        //Ожидаем когда все потоки освободятся и пул станет пустым
        boolean isAllFree = true;
        do {
            try { Thread.sleep(1000); }
            catch (Exception e) { continue; }
            isAllFree = true;
            for(int i = 0; i < count_tasks; i++)
                isAllFree &= tasks[i].isFree;
        } while(!isAllFree || !pool.isEmpty());
        //Прерываем все потоки
        for(int i = 0; i < count_tasks; i++)
            tasks[i].interrupt();

        return pool.getVisited();
    }
}