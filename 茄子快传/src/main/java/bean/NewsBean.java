package bean;

/**
 * Created by Administrator on 2016/7/11.
 */
public class NewsBean {

    /**
     * ctime : 2016-07-11 10:04
     * title : 负面缠身中的特斯拉，本周有一个绝密计划要公布
     * description : 腾讯科技
     * picUrl : http://inews.gtimg.com/newsapp_ls/0/404488981_300240/0
     * url : http://tech.qq.com/a/20160711/019288.htm
     */

    private String ctime;
    private String title;
    private String description;
    private String picUrl;
    private String url;

    public NewsBean(String ctime, String title, String description, String url, String picUrl) {
        this.ctime = ctime;
        this.title = title;
        this.description = description;
        this.url = url;
        this.picUrl = picUrl;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
