package com.kadrez.cuidadosnaturales;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.kadrez.cuidadosnaturales.Adapters.RecyclerViewInfoAdapter;
import com.kadrez.cuidadosnaturales.Models.Info;
import com.kadrez.cuidadosnaturales.UtilsService.UtilService;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class EncyclopediaListActivity extends AppCompatActivity {
    private Button getBtn;
    private EditText searchTxt;
    ProgressBar progressBar;
    private List<Info> infos;
    private RecyclerView recyclerView;
    UtilService utilService;
    static final String apiUrl = "https://cuidadosnaturales.herokuapp.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encyclopedia_list);
        infos = new ArrayList<>();
        recyclerView = findViewById(R.id.listRecyclerView);
        getBtn = findViewById(R.id.getBtn);
        searchTxt = findViewById(R.id.searchTxt);

        // Llamar a función listInfo para listar información de enciclopedias
        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listInfo(view);
            }
        });
        progressBar = findViewById(R.id.progress_bar);
        utilService = new UtilService();
    }

    /**
     * Obtener información de enciclopedias mediante petición a API
     *
     * @param view
     */
    private void listInfo(View view) {
        infos.clear();
        String endpoint = "";
        progressBar.setVisibility(View.VISIBLE);
        if(searchTxt.getText().toString().equals("")){
            endpoint = apiUrl+"/enciclopedias/"; // ruta de enciclopedias
        }else{
            endpoint = apiUrl+"/enciclopedias/title/"+searchTxt.getText().toString(); // ruta de enciclopedias
        }
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                endpoint, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // guardar response
                try {

                    for (int i = 0; i < response.length(); i++) {
                        Info info = new Info();
                        info.setTitle(response.getJSONObject(i).getString("title"));
                        info.setDescription(response.getJSONObject(i).getString("description"));
                        info.setContent(response.getJSONObject(i).getString("content"));
                        info.setCategory(response.getJSONObject(i).getString("category"));
                        infos.add(info);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Response", response.toString());
                setuprecyclerview(infos);
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
     * @param infos
     */
    private void setuprecyclerview(List<Info> infos) {
        RecyclerViewInfoAdapter myadapter = new RecyclerViewInfoAdapter(this, infos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myadapter);
    }
}