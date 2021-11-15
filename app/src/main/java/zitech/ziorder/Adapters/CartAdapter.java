package zitech.ziorder.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import zitech.ziorder.Activities.MenuActivity;
import zitech.ziorder.Objects.Cart;
import zitech.ziorder.R;

public class CartAdapter extends ArrayAdapter {

    private Context context;
    private int itemLayout;
    private List<Cart> cartList;

    public CartAdapter(@NonNull Context context, int itemLayout, List<Cart> cartList) {
        super(context, itemLayout, cartList);
        this.context=context;
        this.itemLayout=itemLayout;
        this.cartList=cartList;
    }

    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView tvDishName, tvPrice, tvAmount, tvPrepay;
        ImageButton imbDown, imbUp;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(itemLayout, null);

            viewHolder.tvDishName=view.findViewById(R.id.textview_dish_item_cart);
            viewHolder.tvPrice=view.findViewById(R.id.textview_price_item_cart);
            viewHolder.tvAmount=view.findViewById(R.id.textview_amount_item_cart);
            viewHolder.tvPrepay=view.findViewById(R.id.textview_prepay_item_cart);
            viewHolder.imbDown=view.findViewById(R.id.imagebutton_amount_down_item_cart);
            viewHolder.imbUp=view.findViewById(R.id.imagebutton_amount_up_item_cart);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Cart cart = cartList.get(position);
        viewHolder.tvDishName.setText(cart.getDishName());
        viewHolder.tvPrice.setText(cart.getPrice()+"");
        viewHolder.tvAmount.setText(cart.getAmount()+"");
        viewHolder.tvPrepay.setText(cart.getPreTotal()+"");

        viewHolder.imbDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Down();
            }

            private void Down() {
                int amount = Integer.parseInt(viewHolder.tvAmount.getText().toString());
                if (amount > 0){
                    amount --;
                    viewHolder.tvAmount.setText(amount+"");
                }
                MenuActivity.sqLiteModel.UpdateData(amount, amount*cart.getPrice(), cart.getDishesId());
            }
        });
        viewHolder.imbUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Up();
            }

            private void Up() {
                int amount = Integer.parseInt(viewHolder.tvAmount.getText().toString());
                if (amount < 99){
                    amount++;
                    viewHolder.tvAmount.setText(amount+"");
                }
                MenuActivity.sqLiteModel.UpdateData(amount, amount*cart.getPrice(), cart.getDishesId());
            }
        });
        viewHolder.tvAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float price = Float.parseFloat(viewHolder.tvPrice.getText().toString());
                int amount = Integer.parseInt(viewHolder.tvAmount.getText().toString());
                float totalPreview = (float)amount*price;
                viewHolder.tvPrepay.setText(totalPreview+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }
}
