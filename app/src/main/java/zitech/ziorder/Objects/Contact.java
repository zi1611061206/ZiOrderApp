package zitech.ziorder.Objects;

public class Contact {
    String title;
    String info;
    int icon;

    public Contact(String title, String info, int icon) {
        this.title = title;
        this.info = info;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
