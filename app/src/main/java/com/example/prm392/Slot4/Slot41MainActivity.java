package com.example.prm392.Slot4;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392.R;

import java.util.ArrayList;
import java.util.List;

public class Slot41MainActivity extends AppCompatActivity {
    ListView listView;
    private Slot41Adapter adapter;
    private List<Student1> list = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_slot41_main);
        listView = findViewById(R.id.slot41Lv);
        list.add(new Student1("Nguyen Van A", "18", R.drawable.android));
        list.add(new Student1("NguyVan B", "1", R.drawable.apple));
        list.add(new Student1("Ngun Van C", "158", R.drawable.blogger));
        list.add(new Student1("yen Van D", "188", R.drawable.hp));
        list.add(new Student1("Ngun Van E", "14", R.drawable.chrome));
        list.add(new Student1("Nguyen Vn F", "177", R.drawable.hancock));
        list.add(new Student1("Nguyen V G", "20", R.drawable.facebook));
        list.add(new Student1("Nguyen Vaaaaaafd G", "270", R.drawable.dell));
        list.add(new Student1("Nguyen Vfdfdfdf G", "220", R.drawable.border));
        adapter = new Slot41Adapter(this,list);
        listView.setAdapter(adapter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}