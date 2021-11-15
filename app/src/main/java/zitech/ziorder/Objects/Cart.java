package zitech.ziorder.Objects;

public class Cart {
    private int dishesId;
    private String dishName;
    private float price;
    private int amount;
    private float preTotal;

    public Cart(int dishesId, String dishName, float price, int amount, float preTotal) {
        this.dishesId = dishesId;
        this.dishName = dishName;
        this.price = price;
        this.amount = amount;
        this.preTotal = preTotal;
    }

    public int getDishesId() {
        return dishesId;
    }

    public void setDishesId(int dishesId) {
        this.dishesId = dishesId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public float getPreTotal() {
        return preTotal;
    }

    public void setPreTotal(float preTotal) {
        this.preTotal = preTotal;
    }
}
