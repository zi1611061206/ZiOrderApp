package zitech.ziorder.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import zitech.ziorder.Objects.CategorySpinner;
import zitech.ziorder.R;

public class CategorySpinnerAdapter extends BaseAdapter {

    private Context context;
    private int itemLayout;
    private ArrayList<CategorySpinner> categoryList;

    public CategorySpinnerAdapter(Context context, int itemLayout, ArrayList<CategorySpinner> categoryList) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.categoryList = categoryList;
    }

    @Override
    public int getCount() {
        return categoryList.size();
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
        TextView tvCategoryName;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder= new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(itemLayout, null);
        viewHolder.tvCategoryName = view.findViewById(R.id.textview_category_name_item_spinner);
        viewHolder.tvCategoryName.setText(categoryList.get(position).getCategoryName());
        return view;
    }
}
