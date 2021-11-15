package zitech.ziorder.Objects;

import androidx.annotation.NonNull;

public class Area {
    private int id;
    private String areaName;

    public Area(int id, String areaName) {
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

    @NonNull
    @Override
    public String toString() {
        return areaName;
    }
}
