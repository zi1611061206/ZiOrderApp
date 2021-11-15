package zitech.ziorder.Activities;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.List;

import zitech.ziorder.Adapters.ContactAdapter;
import zitech.ziorder.Objects.Contact;
import zitech.ziorder.R;

public class ContactActivity extends AppCompatActivity {

    ListView lvContact;
    List<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        AddViewById();
        LoadContact();
        AddEvent();
    }

    private void AddEvent() {
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CreateQRcode(position);
            }

            private void CreateQRcode(int position) {
                final Dialog dialog = new Dialog(ContactActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_share_qrcode);

                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                int width = displayMetrics.widthPixels;
                int height = displayMetrics.heightPixels;
                dialog.getWindow().setLayout(width * 7 / 8, height * 3 / 5);

                TextView tvTitle = dialog.findViewById(R.id.textview_title_qrcode_contact_share);
                ImageView imgContent = dialog.findViewById(R.id.imageview_content_qrcode_contact_share);

                tvTitle.setText(contactList.get(position).getTitle());
                imgContent.setImageBitmap(ConvertStringToQrcode(contactList.get(position).getInfo()));
                dialog.show();
            }

            private Bitmap ConvertStringToQrcode(String content) {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                BitMatrix bitMatrix = null;
                try {
                    bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE,200,200);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                BarcodeEncoder bar = new BarcodeEncoder();
                Bitmap bitMap = bar.createBitmap(bitMatrix);
                return bitMap;
            }
        });
    }

    private void LoadContact() {
        contactList = new ArrayList<>();
        contactList.add(new Contact("Email", "anonymous220011@gmail.com", R.drawable.email));
        contactList.add(new Contact("Phone", "0943144178", R.drawable.phone));
        contactList.add(new Contact("Address", "549/35D XVNT - HCM", R.drawable.address));
        contactList.add(new Contact("Facebook", "https://www.facebook.com/zi.anonymous.zi", R.drawable.facebook));
        contactList.add(new Contact("Version", "Version 1.0", R.drawable.version));
        contactList.add(new Contact("Store", "Zi Coffee Shop", R.drawable.store));
        contactList.add(new Contact("Dev team", "ZiTeam\nNguyễn Ngọc Hiếu 1611061206\nĐỗ Gia Huy 1611061523", R.drawable.teamdev));
        contactList.add(new Contact("Debut", "02/12/2019", R.drawable.calendar));
        contactList.add(new Contact("Copyright", "Copyright © 2019 Ziteam Developer. All rights reserved", R.drawable.copyright));
        ArrayAdapter contactAdapter = new ContactAdapter(this, R.layout.item_contact, contactList);
        lvContact.setAdapter(contactAdapter);
    }

    private void AddViewById() {
        lvContact = findViewById(R.id.listview_contact);
    }
}
