package zitech.ziorder.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.SQLException;
import java.util.List;

import zitech.ziorder.Activities.MenuActivity;
import zitech.ziorder.Activities.TableActivity;
import zitech.ziorder.Models.BillModel;
import zitech.ziorder.Objects.Bill;
import zitech.ziorder.Objects.Table;
import zitech.ziorder.R;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {
    private TableActivity context;
    private int itemLayout;
    private List<Table> tableList;

    public TableAdapter(TableActivity context, int itemLayout, List<Table> tableList) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.tableList = tableList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_table, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Table table = tableList.get(position);
        int status = table.getStatus();
        String readyTable = table.getTableName() + " \n---KV" + table.getAreaId() + "--- \n" + context.getString(R.string.ready);
        String busyTable = table.getTableName() + " \n---KV" + table.getAreaId() + "--- \n" + context.getString(R.string.being_used);
        if (status == 0) {
            holder.btnTable.setText(readyTable);
            holder.btnTable.setTextColor(context.getResources().getColor(R.color.green_dark));
        } else {
            holder.btnTable.setText(busyTable);
            holder.btnTable.setTextColor(context.getResources().getColor(R.color.red));
        }

        AddEvent(table, holder);
    }

    private void AddEvent(final Table table, ViewHolder holder) {
        holder.btnTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order();
            }

            private void Order() {
                int tableId = table.getId();
                int status = table.getStatus();
                Intent intent = new Intent(context, MenuActivity.class);
                intent.putExtra("tableId_select", tableId);
                intent.putExtra("tableStatus_select", status);
                context.startActivity(intent);
            }
        });
        holder.btnTable.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ViewBill();
                return true;
            }

            private void ViewBill() {
                int tableId = table.getId();
                String tableName = table.getTableName();
                int status = table.getStatus();
                if (status == 1) {
                    try{
                        BillModel billModel = new BillModel();
                        Bill bill = billModel.GetBill(tableId);
                        int billId = bill.getId();
                        context.ShowBillDialog(tableName, billId, tableId);
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tableList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Button btnTable;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnTable = itemView.findViewById(R.id.button_item_table);
        }
    }
}
