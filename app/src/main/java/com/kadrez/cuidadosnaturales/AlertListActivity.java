package com.kadrez.cuidadosnaturales;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.kadrez.cuidadosnaturales.Adapters.RecyclerViewAlertsAdapter;
import com.kadrez.cuidadosnaturales.UtilsService.UtilService;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kadrez.cuidadosnaturales.Models.Alert;

import java.util.List;
import java.util.ArrayList;

public class AlertListActivity extends AppCompatActivity {
    private Button backBtn, getBtn;
    ProgressBar progressBar;
    private List<Alert> alerts;
    private RecyclerView recyclerView;
    UtilService utilService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_list);
        alerts = new ArrayList<>();
        recyclerView = findViewById(R.id.listRecyclerView);
        getBtn = findViewById(R.id.getBtn);

        // Llamar a función listAlerts para cargar listado de alertas
        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listAlerts(view);
            }
        });
        progressBar = findViewById(R.id.progress_bar);
        utilService = new UtilService();
    }

    /**
     * Obtener lista de alertas mediante petición a API
     *
     * @param view
     */
    private void listAlerts(View view) {
        progressBar.setVisibility(View.VISIBLE);
        String apiKey = "http://192.168.1.82:3000/alerts";  // Dirección ip correspondiente a servidor de API, con respectivo puerto y ruta de alertas
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                apiKey, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // guardar response
                try {
                    for (int i = 0; i < response.length(); i++) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        try {
                            Alert alert = new Alert();
                            alert.setName(response.getJSONObject(i).getString("plant"));
                            alert.setAlertType(response.getJSONObject(i).getString("type"));
                            String dtStart = response.getJSONObject(i).getString("date");
                            System.out.println("dtStart: " + dtStart);
                            Date date = format.parse(dtStart);
                            System.out.println("date: " + date);
                            alert.setDate(date);
                            alert.setImage_url(response.getJSONObject(i).getString("img_url"));
                            alerts.add(alert);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Response", response.toString());
                setuprecyclerview(alerts);
                progressBar.setVisibility(View.GONE);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }) {
        };

        // Establecer la política de reintentos
        int socketTime = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonArrayRequest.setRetryPolicy(policy);

        // Agregar petición
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Adaptador para plasmar la información obtenida en forma de recyclerView
     *
     * @param alerts
     */
    private void setuprecyclerview(List<Alert> alerts) {
        RecyclerViewAlertsAdapter myadapter = new RecyclerViewAlertsAdapter(this, alerts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myadapter);
    }
}