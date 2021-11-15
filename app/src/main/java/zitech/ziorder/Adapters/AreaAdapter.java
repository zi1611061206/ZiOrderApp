package zitech.ziorder.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import zitech.ziorder.Objects.Area;
import zitech.ziorder.R;

public class AreaAdapter extends ArrayAdapter {

    private Context context;
    private int itemLayout;
    private ArrayList<Area> areaList;

    public AreaAdapter(@NonNull Context context, int itemLayout, ArrayList<Area> areaList) {
        super(context, itemLayout, areaList);
        this.context=context;
        this.itemLayout=itemLayout;
        this.areaList=areaList;
    }

    @Override
    public int getCount() {
        return areaList.size();
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
        Button btnArea;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(itemLayout, null);
            viewHolder.btnArea = view.findViewById(R.id.button_item_area);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Area area = areaList.get(position);
        viewHolder.btnArea.setText(context.getString(R.string.area)+" "+area.getAreaName());
        return view;
    }
}
