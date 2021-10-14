package com.kadrez.cuidadosnaturales;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class ManagePlantsActivity extends AppCompatActivity {
    private Button addBtn;
    private Button listBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_plants);
        addBtn = findViewById(R.id.addBtn);
        listBtn = findViewById(R.id.listBtn);

        // Redirigir a menú de creación de plantas
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagePlantsActivity.this, PlantsActivity.class);
                startActivity(intent);
            }
        });

        // Redirigir a listado de plantas
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(ManagePlantsActivity.this, PlantListActivity.class);
                startActivity(intent2);
            }
        });
    }
}