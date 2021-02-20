public class URLDepthPair {
    private final String URL_PREFIX = "http://";
    private String Url;
    private int Depth;

    public URLDepthPair(String url, int depth){
        setUrl(url);
        setDepth(depth);
    }

    public String getUrl() { return Url; }
    public int getDepth() { return Depth; }

    public void setUrl(String url) {
        if(!url.startsWith(URL_PREFIX))
            throw new IllegalArgumentException("URL cannot starts with URL_PREFIX");
        Url = url;
    }
    public void setDepth(int depth) {
        if(depth < 0)
            throw new IllegalArgumentException("Depth cannot be less than 0");
        Depth = depth;
    }

    public boolean equals(Object obj){
        if(obj == this) return true;
        if(obj == null || obj.getClass() != this.getClass())
            return false;
        URLDepthPair other = (URLDepthPair) obj;
        return Url.equals(other.Url);// && Depth == other.Depth;
    }
    public int hashCode(){
        return 31 * Url.hashCode() + Depth;
    }
    public String toString(){
        return "[URL: " + Url + ", Depth: " + Depth + "]";
    }
}