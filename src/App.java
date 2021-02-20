import java.util.HashSet;
import java.util.LinkedList;

public class App {
    public static void main(String[] args){
        //Сайт для примера: http://bio.acousti.ca
        //if(args.length != 2)
        //    throw new IllegalArgumentException();
        //String site = args[0];
        //int max_depth = Integer.parseInt(args[1]);
        String site = "http://bio.acousti.ca";
        int max_depth = 10;
        Crawler crawler = new Crawler();
        HashSet<URLDepthPair> res = crawler.getSites(site, max_depth);
        print_list(res);
    }

    //Дополнительная функция для вывода списка
    public static void print_list(HashSet<URLDepthPair> list){
        if(list == null) throw new IllegalArgumentException("List cannot be null");
        for(URLDepthPair el : list)
            System.out.println(el);
    }
}
