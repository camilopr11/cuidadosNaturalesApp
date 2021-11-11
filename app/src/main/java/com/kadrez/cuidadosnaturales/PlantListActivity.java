package com.kadrez.cuidadosnaturales;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.kadrez.cuidadosnaturales.Adapters.RecyclerViewAlertsAdapter;
import com.kadrez.cuidadosnaturales.Adapters.RecyclerViewPlantsAdapter;
import com.kadrez.cuidadosnaturales.Models.Plant;
import com.kadrez.cuidadosnaturales.UtilsService.UtilService;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class PlantListActivity extends AppCompatActivity {
    private Button getBtn;
    ProgressBar progressBar;
    private List<Plant> plants;
    private RecyclerView recyclerView;
    UtilService utilService;
    static final String apiUrl = "https://cuidadosnaturales.herokuapp.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_list);
        plants = new ArrayList<>();
        recyclerView = findViewById(R.id.listRecyclerView);
        getBtn = findViewById(R.id.getBtn);

        // Llamar a función listPlants para listar plantas existentes
        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listPlants(view);
            }
        });
        progressBar = findViewById(R.id.progress_bar);
        utilService = new UtilService();
    }

    /**
     * Obtener lista de plantas mediante petición a API
     *
     * @param view
     */
    private void listPlants(View view) {
        plants.clear();
        progressBar.setVisibility(View.VISIBLE);
        String endpoint = apiUrl+"/plants";  // ruta de plantas
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                endpoint, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // guardar response
                try {
                    for (int i = 0; i < response.length(); i++) {
                        Plant plant = new Plant();
                        plant.setId(response.getJSONObject(i).getString("_id"));
                        plant.setName(response.getJSONObject(i).getString("name"));
                        plant.setType(response.getJSONObject(i).getString("type"));
                        plant.setScientificName(response.getJSONObject(i).getString("scientific_name"));
                        plant.setOrder(response.getJSONObject(i).getString("order"));
                        plant.setImg_url(response.getJSONObject(i).getString("img_url"));
                        plants.add(plant);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Response", response.toString());
                setuprecyclerview(plants);
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
     * @param plants
     */
    private void setuprecyclerview(List<Plant> plants) {
        RecyclerViewPlantsAdapter myadapter = new RecyclerViewPlantsAdapter(this, plants);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myadapter);

    }

}