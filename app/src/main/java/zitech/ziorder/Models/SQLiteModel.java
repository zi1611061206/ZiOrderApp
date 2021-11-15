package zitech.ziorder.Models;

import android.database.Cursor;

import zitech.ziorder.Activities.MenuActivity;
import zitech.ziorder.Objects.SQLiteObject;

public class SQLiteModel {

    public SQLiteObject sqLiteObject;
    public MenuActivity context;

    public SQLiteModel(MenuActivity context) {
        this.context = context;
        sqLiteObject = new SQLiteObject(context);
    }

    public void DeleteCart(){
        sqLiteObject.QueryData("drop table cart");
    }

    public void InitCart(){
        sqLiteObject.QueryData("create table if not exists cart(id integer primary key autoincrement, dishesid integer, dishesname text, price float, amount integer, pretotal float)");
    }

    public void ClearData(){
        sqLiteObject.QueryData("delete from cart");
    }

    public void InsertData(int dishesId, String dishesName, float price, int amount, float preTotal){
        sqLiteObject.QueryData("insert into cart values(null, "+dishesId+", '"+dishesName+"', "+price+", "+amount+", "+preTotal+")");
    }

    public void UpdateData(int amount, float preTotal, int dishesId){
        sqLiteObject.QueryData("update cart set amount = "+amount+", pretotal = "+preTotal+" where dishesid = "+dishesId);
    }

    public int Count(int dishesId){
        Cursor cursor=sqLiteObject.GetData("select * from cart where dishesid = "+dishesId);
        int count = 0;
        while (cursor.moveToNext())
            count++;
        return count;
    }

    public boolean CheckExist(int dishesId){
        Cursor cursor = sqLiteObject.GetData("select * from cart where dishesid = "+dishesId);
        if (cursor.moveToNext()){
            return true;
        }
        return false;
    }

    public Cursor GetCartData(){
        return sqLiteObject.GetData("select * from cart where amount > 0");
    }
}
