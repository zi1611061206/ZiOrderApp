package zitech.ziorder.Objects;

public class CategorySpinner {
    private int id;
    private String cotegoryName;

    public CategorySpinner(int id, String cotegoryName) {
        this.id = id;
        this.cotegoryName = cotegoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return cotegoryName;
    }

    public void setCategoryName(String areaName) {
        this.cotegoryName = areaName;
    }
}
