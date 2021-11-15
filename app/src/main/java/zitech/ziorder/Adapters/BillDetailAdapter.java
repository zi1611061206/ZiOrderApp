package zitech.ziorder.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.sql.SQLException;
import java.util.List;

import zitech.ziorder.Models.MenuModel;
import zitech.ziorder.Objects.BillDetail;
import zitech.ziorder.Objects.Menu;
import zitech.ziorder.R;

public class BillDetailAdapter extends ArrayAdapter {

    private Context context;
    private int itemLayout;
    private List<BillDetail> billDetailList;

    public BillDetailAdapter(@NonNull Context context, int itemLayout, List<BillDetail> billDetailList) {
        super(context, itemLayout, billDetailList);
        this.context=context;
        this.itemLayout=itemLayout;
        this.billDetailList=billDetailList;
    }

    @Override
    public int getCount() {
        return billDetailList.size();
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
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(itemLayout, null);

            viewHolder.tvDishName=view.findViewById(R.id.textview_dish_bill_detail);
            viewHolder.tvPrice=view.findViewById(R.id.textview_price_bill_detail);
            viewHolder.tvAmount=view.findViewById(R.id.textview_amount_bill_detail);
            viewHolder.tvPrepay=view.findViewById(R.id.textview_prepay_bill_detail);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        BillDetail billDetail = billDetailList.get(position);

        int dishId = billDetail.getDishId();
        MenuModel menuModel = new MenuModel();
        Menu menu = null;
        try {
            menu = menuModel.GetDishDetail(dishId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        viewHolder.tvDishName.setText(menu.getDishName());
        viewHolder.tvPrice.setText(menu.getPrice()+"");
        viewHolder.tvAmount.setText("x "+billDetail.getAmount());
        viewHolder.tvPrepay.setText(billDetail.getPrePay()+"");
        return view;
    }
}
