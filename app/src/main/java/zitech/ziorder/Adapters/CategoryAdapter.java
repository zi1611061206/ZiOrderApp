package zitech.ziorder.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;

import java.util.List;

import zitech.ziorder.Objects.Category;
import zitech.ziorder.R;

public class CategoryAdapter extends ArrayAdapter {

    private Context context;
    private int itemLayout;
    private List<Category> categoryList;

    public CategoryAdapter(@NonNull Context context, int itemLayout, List<Category> categoryList) {
        super(context, itemLayout, categoryList);
        this.context=context;
        this.itemLayout=itemLayout;
        this.categoryList=categoryList;
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
        Button btnCategory;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(itemLayout, null);
            viewHolder.btnCategory = view.findViewById(R.id.button_item_category);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Category category = categoryList.get(position);
        viewHolder.btnCategory.setText(category.getName());
        return view;
    }
}
