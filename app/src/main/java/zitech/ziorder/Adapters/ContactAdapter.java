package zitech.ziorder.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import zitech.ziorder.Objects.Contact;
import zitech.ziorder.R;

public class ContactAdapter extends ArrayAdapter {

    private Context context;
    private int itemLayout;
    private List<Contact> contactList;

    public ContactAdapter(@NonNull Context context, int itemLayout, List<Contact> contactList) {
        super(context, itemLayout, contactList);
        this.context = context;
        this.itemLayout = itemLayout;
        this.contactList = contactList;
    }

    @Override
    public int getCount() {
        return contactList.size();
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
        ImageView imgIcon;
        TextView tvTitle, tvInfo;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(itemLayout, null);

            viewHolder.imgIcon = view.findViewById(R.id.imageview_icon_contact_item);
            viewHolder.tvTitle = view.findViewById(R.id.textview_title_contact_item);
            viewHolder.tvInfo = view.findViewById(R.id.textview_info_contact_item);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Contact contact = contactList.get(position);
        viewHolder.tvTitle.setText(contact.getTitle());
        viewHolder.tvInfo.setText(contact.getInfo());
        viewHolder.imgIcon.setImageResource(contact.getIcon());
        return view;
    }
}
