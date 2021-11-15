package zitech.ziorder.Objects;

public class AreaSpinner {
    private int id;
    private String areaName;

    public AreaSpinner(int id, String areaName) {
        this.id = id;
        this.areaName = areaName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
