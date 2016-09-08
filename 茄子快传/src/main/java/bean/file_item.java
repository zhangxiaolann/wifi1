package bean;

/**
 * Created by Administrator on 2016/6/1.
 */
public class file_item {

    int drawable_int;
    String file_name;
    String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public file_item(int drawable_int, String file_name) {
        this.drawable_int = drawable_int;
        this.file_name = file_name;
    }

    public file_item() {

    }


    @Override
    public String toString() {
        return "file_item{" +
                "drawable_int=" + drawable_int +
                ", file_name='" + file_name + '\'' +
                '}';
    }

    public int getDrawable_int() {

        return drawable_int;
    }

    public void setDrawable_int(int drawable_int) {
        this.drawable_int = drawable_int;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;

    }
}
