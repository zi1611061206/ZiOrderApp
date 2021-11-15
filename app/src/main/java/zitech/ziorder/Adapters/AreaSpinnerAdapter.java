package zitech.ziorder.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import zitech.ziorder.Objects.AreaSpinner;
import zitech.ziorder.R;

public class AreaSpinnerAdapter extends BaseAdapter {

    private Context context;
    private int itemLayout;
    private ArrayList<AreaSpinner> areaList;

    public AreaSpinnerAdapter(Context context, int itemLayout, ArrayList<AreaSpinner> areaList) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.areaList = areaList;
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
        TextView tvAreaName;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder= new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(itemLayout, null);
        viewHolder.tvAreaName = view.findViewById(R.id.textview_area_name_item_spinner);
        viewHolder.tvAreaName.setText(context.getString(R.string.area)+" "+areaList.get(position).getAreaName());
        return view;
    }
}
