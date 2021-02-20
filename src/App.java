import java.util.LinkedList;

public class App {
    public static void main(String[] args){
        //Сайт для примера: http://bio.acousti.ca
        if(args.length != 2)
            throw new IllegalArgumentException();
        String site = args[0];
        int max_depth = Integer.parseInt(args[1]);
        Crawler crawler = new Crawler();
        LinkedList<URLDepthPair> res = crawler.getSites(site, max_depth);
        //print_list(res);
    }

    //Дополнительная функция для вывода списка
    public static void print_list(LinkedList<URLDepthPair> list){
        if(list == null) throw new IllegalArgumentException("List cannot be null");
        for(int i = 0; i < list.size(); i++)
            System.out.println(list.get(i));
    }
}
