package com.kadrez.cuidadosnaturales;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private Button gardenBtn;
    private Button encyclopediaBtn;
    private Button alertsBtn;
    private Button exitBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gardenBtn = findViewById(R.id.gardenBtn);
        encyclopediaBtn = findViewById(R.id.encyclopediaBtn);
        alertsBtn = findViewById(R.id.alertsBtn);
        exitBtn = findViewById(R.id.exitBtn);

        // Redirigir a gestión de jardín virtual
        gardenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ManagePlantsActivity.class);
                startActivity(intent);
            }
        });

        // Redirigir a gestión de enciclopedias
        encyclopediaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity.this, ManageEncyclopediaActivity.class);
                startActivity(intent2);
            }
        });

        // Redirigir a gestión de alertas
        alertsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(MainActivity.this, ManageAlertsActivity.class);
                startActivity(intent3);
            }
        });

        // Salir de app
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);

            }
        });
    }
}