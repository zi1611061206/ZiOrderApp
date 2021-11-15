package zitech.ziorder.Objects;

public class BillDetail {
    private int id;
    private int dishId;
    private int amount;
    private float prePay;

    public BillDetail(int id, int dishId, int amount, float prePay) {
        this.id = id;
        this.dishId = dishId;
        this.amount = amount;
        this.prePay = prePay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public float getPrePay() {
        return prePay;
    }

    public void setPrePay(float prePay) {
        this.prePay = prePay;
    }
}
