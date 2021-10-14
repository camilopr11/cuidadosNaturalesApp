package com.kadrez.cuidadosnaturales;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class ManageEncyclopediaActivity extends AppCompatActivity {
    private Button addBtn;
    private Button listBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_encyclopedia);
        addBtn = findViewById(R.id.addBtn);
        listBtn = findViewById(R.id.listBtn);

        // Redirigir a menú de creación de enciclopedias
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageEncyclopediaActivity.this, InfosActivity.class);
                startActivity(intent);
            }
        });

        // Redirigir a listado de información de enciclopedias
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(ManageEncyclopediaActivity.this, EncyclopediaListActivity.class);
                startActivity(intent2);
            }
        });
    }
}