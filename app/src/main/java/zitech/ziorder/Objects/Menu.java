package zitech.ziorder.Objects;

import ir.mirrajabi.searchdialog.core.Searchable;

public class Menu implements Searchable {
    private int id;
    private int categoryId;
    private String dishName;
    private byte[] dishImage;
    private float price;

    public Menu(int id, int categoryId, String dishName, byte[] dishImage, float price) {
        this.id=id;
        this.categoryId=categoryId;
        this.dishName=dishName;
        this.dishImage=dishImage;
        this.price=price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public byte[] getImage() {
        return dishImage;
    }

    public void setImage(byte[] dishImage) {
        this.dishImage = dishImage;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String getTitle() {
        return dishName;
    }
}
