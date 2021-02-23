import java.util.HashSet;

public class App {
    public static void main(String[] args) {
        if(args.length < 2)
            throw new IllegalArgumentException("Count args cannot be less 2");
        //Сайт для тестирования: "http://bio.acousti.ca";
        String site = args[0];
        int max_depth = Integer.parseInt(args[1]);
        Crawler crawler = new Crawler();
        long m = System.currentTimeMillis();
        HashSet<URLDepthPair> res = crawler.getSites(site, max_depth);
        System.out.println((double) (System.currentTimeMillis() - m));
        print_list(res);
    }
    public static void print_list(HashSet<URLDepthPair> list) {
        if(list == null) throw new IllegalArgumentException("List cannot be null");
        for(URLDepthPair el : list)
            System.out.println(el);
        System.out.println(list.size());
    }
}