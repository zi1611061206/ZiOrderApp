package zitech.ziorder.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;
import java.util.ArrayList;

import zitech.ziorder.Adapters.AreaSpinnerAdapter;
import zitech.ziorder.Models.AreaModel;
import zitech.ziorder.Objects.AreaSpinner;
import zitech.ziorder.R;
import zitech.ziorder.Threads.AddTableAsyncTask;

public class AddTableActivity extends AppCompatActivity {
    EditText edtTableName;
    Spinner spArea;
    TextView tvError;
    Button btnCancel, btnAdd;

    ArrayList<AreaSpinner> areaArrayList=new ArrayList<>();
    AreaSpinnerAdapter areaAdapter;

    int areaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_table);

        areaId=0;
        AddViewById();
        try {
            LoadAreaSpinner();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AddEvent();
    }

    private void AddEvent() {
        spArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                areaId = areaArrayList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }

            private void Back() {
                this.Back();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tableName = edtTableName.getText().toString().trim();
                if (tableName.equals("")) {
                    tvError.setText(getString(R.string.full_fill));
                } else if (areaId == 0) {
                    tvError.setText(getString(R.string.require_area));
                } else {
                    AddTable(areaId, tableName);
                }
            }

            private void AddTable(int areaId, String tableName) {
                new AddTableAsyncTask(AddTableActivity.this, areaId, tableName).execute();
            }
        });
    }

    private void LoadAreaSpinner() throws SQLException {
        areaArrayList.clear();
        areaArrayList = new AreaModel().GetAreaSpinnerList();
        areaAdapter = new AreaSpinnerAdapter(AddTableActivity.this,R.layout.item_area_spinner,areaArrayList);
        this.spArea.setAdapter(areaAdapter);
    }

    private void AddViewById() {
        edtTableName = findViewById(R.id.edittext_table_name_dialog);
        spArea = findViewById(R.id.spinner_Area);
        tvError = findViewById(R.id.textview_error_add_table);
        btnCancel = findViewById(R.id.button_cancel_add_table_dialog);
        btnAdd = findViewById(R.id.button_confrim_add_table_dialog);
    }
}
