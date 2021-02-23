public class Url {
    private String protocol;
    private String host;
    private Integer port;
    private String path;

    public Url(String fullUrl){
        setUrl(fullUrl);
    }

    public void setUrl(String fullUrl){
        if(fullUrl == null || fullUrl.length() == 0)
            throw new IllegalArgumentException("fullUrl cannot be null or empty");
        try { parseFullUrl(fullUrl); }
        catch(Exception e) { throw new IllegalArgumentException("fullUrl bad Url"); }
    }

    public String  getProtocol() { return protocol; }
    public String  getHost    () { return host; }
    public Integer getPort    () { return port; }
    public String  getPath    () { return path; }
    //Функуция возвращает protocol://host или host
    public String getFullHost() {
        StringBuilder res = new StringBuilder();
        if(!protocol.equals(""))
            res.append(protocol + "://");
        res.append(host);
        return res.toString();
    }

    //Функция парсит строку на protocol, host, port и path
    private void parseFullUrl(String fullUrl) {
        int endProtocol = fullUrl.indexOf("://");
        if(endProtocol != -1)
            protocol = fullUrl.substring(0, endProtocol);
        else {
            protocol = "";
            endProtocol = -3;
        }

        int endHost = fullUrl.indexOf("/", endProtocol + 3);
        if(endHost == -1){
            host = fullUrl.substring(endProtocol + 3);
            path = "/";
        } else {
            host = fullUrl.substring(endProtocol + 3, endHost);
            path = fullUrl.substring(endHost);
        }

        int startPort = host.indexOf(":");
        port = 80;
        if(startPort != -1)
            port = Integer.valueOf(host.substring(startPort + 1));
    }

    //Сравнение по host + path
    public boolean equals(Object obj){
        if(obj == this) return true;
        if(obj == null || obj.getClass() != this.getClass())
            return false;
        Url other = (Url) obj;
        return toString().equals(other.toString());
    }
    public int hashCode() {
        return toString().hashCode();
    }
    public String toString(){
        return host + path;
    }
}