package com.example.prm392.Slot5;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392.R;

import java.util.ArrayList;
import java.util.List;

public class Slot5MainActivity extends AppCompatActivity {
    ListView listView;
    EditText txtId,txtName,txtPrice;
    Button btnInsert, btnSelectAll;
    Slot5Adapter adapter;
    List<Slot5Product> list=new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot5_main);
        listView=findViewById(R.id.slot5_listview);
        txtId=findViewById(R.id.slot5_id);
        txtName=findViewById(R.id.slot5_name);
        txtPrice=findViewById(R.id.slot5_price);
        btnInsert=findViewById(R.id.slot5_insert);
        btnSelectAll=findViewById(R.id.slot5_getdata);
        Slot5ProductDAO dao=new Slot5ProductDAO(this);
        btnInsert.setOnClickListener(v->{
            Slot5Product p=new Slot5Product();
            p.setId(txtId.getText().toString());
            p.setName(txtName.getText().toString());
            p.setPrice(txtPrice.getText().toString());
            int kq=dao.insertProduct(p);
            if (kq==-1){
                Toast.makeText(getApplicationContext(),"Insert that bai",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(),"Insert thanh cong",Toast.LENGTH_SHORT).show();
            }
        });
        btnSelectAll.setOnClickListener(v->{
            list=dao.getAll();
            adapter=new Slot5Adapter(list,this);
            listView.setAdapter(adapter);
        });
    }
}