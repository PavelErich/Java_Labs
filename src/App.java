import java.util.HashSet;

public class App {
    public static void main(String[] args) {
        if(args.length < 3)
            throw new IllegalArgumentException("Count args cannot be less 3");
        //Сайт для тестирования: "http://bio.acousti.ca";
        //Примерные результаты тестирования:
        //max_depth = 2, время = 1 сек, сайтов = 74, потоков 20
        //max_depth = 3, время ~ 10 сек, сайтов ~ 2800, потоков 20
        //max_depth = 4, время ~ 29.4 мин, сайтов ~ 130000, потоков 20
        String site = args[0];
        int max_depth = Integer.parseInt(args[1]);
        int count_tasks = Integer.parseInt(args[2]);
        Crawler crawler = new Crawler();
        long m = System.currentTimeMillis();
        HashSet<URLDepthPair> res = crawler.getSites(site, max_depth, count_tasks);
        System.out.println("За " + (double) (System.currentTimeMillis() - m) + " мс, найдено " + res.size() + " сайтов!");

        for(URLDepthPair pair : res)
            Logger.writeSites(pair.toString());
    }
}