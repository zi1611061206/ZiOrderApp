package zitech.ziorder.Objects;

import androidx.annotation.NonNull;

public class Category {
    private int id;
    private String categoryName;

    public Category(int id, String name) {
        this.id = id;
        this.categoryName = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return categoryName;
    }

    public void setName(String name) {
        this.categoryName = name;
    }

    @NonNull
    @Override
    public String toString() {
        return categoryName;
    }
}
