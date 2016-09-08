package bean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/6/28.
 */
public class video_item {
    String title;
    String path;
    long duration;
    long size;
    Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public video_item(Bitmap bitmap, long size, long duration, String title, String path) {

        this.bitmap = bitmap;
        this.size = size;
        this.duration = duration;
        this.title = title;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public video_item(String title, String path, long duration, long size) {

        this.title = title;
        this.path = path;
        this.duration = duration;
        this.size = size;
    }
}
