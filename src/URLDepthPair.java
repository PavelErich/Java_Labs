public class URLDepthPair {
    private String Url;
    private int Depth;

    public URLDepthPair(String url, int depth){
        Url = url;
        Depth = depth;
    }

    public String getUrl() { return Url; }
    public int getDepth() { return Depth; }

    public boolean equals(Object obj){
        if(obj == this) return true;
        if(obj == null || obj.getClass() != this.getClass())
            return false;
        URLDepthPair other = (URLDepthPair) obj;
        return Url.equals(other.Url) && Depth == other.Depth;
    }
    public int hashCode(){
        return 31 * Url.hashCode() + Depth;
    }
    public String toString(){
        return "[URL: " + Url + ", Depth: " + Depth + "]";
    }
}