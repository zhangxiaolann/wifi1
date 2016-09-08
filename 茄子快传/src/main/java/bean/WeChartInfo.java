package bean;


public class WeChartInfo
{


    public WeChartInfo(String firstImg, String id, String source, String url, String title, String mark) {
        this.firstImg = firstImg;
        this.id = id;
        this.source = source;
        this.url = url;
        this.title = title;
        this.mark = mark;
    }

    /**
     * firstImg : http://zxpic.gtimg.com/infonew/0/wechat_pics_-6577728.jpg/640
     * id : wechat_20160707033856
     * source : 冷兔
     * title : 当朋友想跟你自拍，然而你并没有兴趣的时候！
     * url : http://v.juhe.cn/weixin/redirect?wid=wechat_20160707033856
     * mark : 热门
     */

    private String firstImg;
    private String id;
    private String source;
    private String title;
    private String url;
    private String mark;

    public String getFirstImg() {
        return firstImg;
    }

    public void setFirstImg(String firstImg) {
        this.firstImg = firstImg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}

