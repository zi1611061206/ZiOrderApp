package zitech.ziorder.Adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import zitech.ziorder.Activities.DetailActivity;
import zitech.ziorder.Activities.MenuActivity;
import zitech.ziorder.Objects.Menu;
import zitech.ziorder.R;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private MenuActivity context;
    private int itemLayout;
    private List<Menu> menuList;

    public MenuAdapter(MenuActivity context, int itemLayout, List<Menu> menuList) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_menu, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imgDish.setImageBitmap(ConvertStringToImage(menuList.get(position)));
        holder.tvDishName.setText(menuList.get(position).getDishName());
        holder.tvPrice.setText(menuList.get(position).getPrice() + "");
        Menu menu = menuList.get(position);
        AddEvent(menu, holder);
    }

    private void AddEvent(final Menu menu, final ViewHolder holder) {
        holder.imbAmountDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownAmount();
            }

            private void DownAmount() {
                int amount = Integer.parseInt(holder.edtAmount.getText().toString());
                if (amount > 0) {
                    amount--;
                    holder.edtAmount.setText(amount + "");
                }

            }
        });
        holder.imbAmountUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpAmount();
            }

            private void UpAmount() {
                int amount = Integer.parseInt(holder.edtAmount.getText().toString());
                if (amount < 99) {
                    amount++;
                    holder.edtAmount.setText(amount + "");
                }
            }
        });
        holder.edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String amountStr = holder.edtAmount.getText().toString();
                if (amountStr.trim().length() > 0) {
                    float price = Float.parseFloat(holder.tvPrice.getText().toString());
                    int amount = Integer.parseInt(amountStr);
                    float totalPreview = (float) amount * price;
                    holder.tvTotalPreview.setText(totalPreview + "");

                    if (MenuActivity.sqLiteModel.CheckExist(menu.getId())) {
                        MenuActivity.sqLiteModel.UpdateData(amount, totalPreview, menu.getId());
                    } else {
                        MenuActivity.sqLiteModel.InsertData(menu.getId(), menu.getDishName(), price, amount, totalPreview);
                    }
                } else {
                    holder.edtAmount.setText("0");
                    holder.edtAmount.setSelection(1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.imgDish.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ViewDetail();
                return true;
            }

            private void ViewDetail() {
                int dishId = menu.getId();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("dishId_select", dishId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDish;
        TextView tvDishName, tvPrice;
        ImageButton imbAmountDown, imbAmountUp;
        TextView tvTotalPreview;
        EditText edtAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDish = itemView.findViewById(R.id.image_item_menu);
            tvDishName = itemView.findViewById(R.id.textview_dishes_name_item_menu);
            tvPrice = itemView.findViewById(R.id.textview_dishes_price_item_menu);
            imbAmountDown = itemView.findViewById(R.id.imagebutton_amount_down);
            imbAmountUp = itemView.findViewById(R.id.imagebutton_amount_up);
            tvTotalPreview = itemView.findViewById(R.id.textview_total_preview_item_menu);
            edtAmount = itemView.findViewById(R.id.edittext_dishes_amount_item_menu);
        }
    }

    private Bitmap ConvertStringToImage(Menu menu) {
        byte[] imageBytes = menu.getImage();
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }
}
