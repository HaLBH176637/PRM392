package com.example.prm392.Slot1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392.R;
import com.example.prm392.Slot2.Slot2MainActivity;

public class Slot1MainActivity extends AppCompatActivity {
    private EditText num1, num2;
    private Button btnCaculate;
    private TextView total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_slot1_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        num1 = findViewById(R.id.num1);
        num2 = findViewById(R.id.num2);
        btnCaculate = findViewById(R.id.caculate);
        total = findViewById(R.id.total);
        btnCaculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int n1 = Integer.parseInt(num1.getText().toString());
//                int n2 = Integer.parseInt(num2.getText().toString());
//                int sum = n1 + n2;
//                total.setText("Result: " + sum);
                //chuyen activity tu slot 1 qua slot 2
                Intent intent = new Intent(Slot1MainActivity.this, Slot2MainActivity.class);

                //dua lu lieu vao intent de truyen qua slot 2
                intent.putExtra("num1", Integer.parseInt(num1.getText().toString()));
                intent.putExtra("num2", Integer.parseInt(num2.getText().toString()));

                //goi activity 2
                startActivity(intent);
            }
        });
    }
}