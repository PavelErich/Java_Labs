import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class URLPool {
    //Очередь непосещенных сайтов
    private final Queue<URLDepthPair> no_visited;
    //Список посещенных сайтов
    private final HashSet<URLDepthPair> visited;
    public URLPool() {
        no_visited = new LinkedList<>();
        visited = new HashSet<>();
    }

    //Функция достает из no_visited очередной сайт, или возвращает Exception
    //если no_visited пустой
    public synchronized URLDepthPair poll() throws Exception {
        if(no_visited.isEmpty())
            throw new Exception();
        return no_visited.poll();
    }
    //Функция добавления сайта в очередь no_visited, если он еще не был посещен
    //или его нет в очереди
    public synchronized void push(final URLDepthPair pair) {
        if(visited.contains(pair) || no_visited.contains(pair))
            return;
        no_visited.add(pair);
    }
    //Функция добавления сайта в список посещенных сайтов visited
    public synchronized void push_visited(final URLDepthPair pair) {
        visited.add(pair);
    }

    public synchronized boolean isEmpty() { return no_visited.isEmpty(); }
    public HashSet<URLDepthPair> getVisited() {
        return visited;
    }
}