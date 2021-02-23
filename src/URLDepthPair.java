public class URLDepthPair {
    private Url _url;
    private int _depth;

    public URLDepthPair(Url url, int depth){
        setUrl(url);
        setDepth(depth);
    }

    public Url getUrl  () { return _url; }
    public int getDepth() { return _depth; }

    public void setUrl (Url url) { _url = url; }
    public void setDepth(int depth) {
        if(depth < 0)
            throw new IllegalArgumentException("Depth cannot be less than 0");
        _depth = depth;
    }

    public boolean equals(Object obj){
        if(obj == this) return true;
        if(obj == null || obj.getClass() != this.getClass())
            return false;
        URLDepthPair other = (URLDepthPair) obj;
        return _url.equals(other._url);// && Depth == other.Depth;
    }
    public int hashCode(){
        return _url.hashCode();// + Depth;
    }
    public String toString(){
        return "[URL: " + getUrl() + ", Depth: " + getDepth() + "]";
    }
}