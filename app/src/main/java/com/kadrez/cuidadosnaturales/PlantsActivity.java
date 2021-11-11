package com.kadrez.cuidadosnaturales;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kadrez.cuidadosnaturales.UtilsService.UtilService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PlantsActivity extends AppCompatActivity {
    private Button addBtn;
    private EditText name_ET, type_ET, scName_ET, order_ET, img_ET;
    ProgressBar progressBar;
    private String name, type, scientific_name, order, img;
    UtilService utilService;
    static final String apiUrl = "https://cuidadosnaturales.herokuapp.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plants);
        name_ET = findViewById(R.id.name_ET);
        type_ET = findViewById(R.id.type_ET);
        scName_ET = findViewById(R.id.scName_ET);
        order_ET = findViewById(R.id.order_ET);
        img_ET = findViewById(R.id.img_ET);
        progressBar = findViewById(R.id.progress_bar);
        addBtn = findViewById(R.id.addBtn);
        utilService = new UtilService();

        // Botón de añadir que llama a la función createPlant
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utilService.hideKeyboard(view, PlantsActivity.this);
                name = name_ET.getText().toString();
                type = type_ET.getText().toString();
                scientific_name = scName_ET.getText().toString();
                order = order_ET.getText().toString();
                img = img_ET.getText().toString();
                if (validate(view)) {
                    createPlant(view);
                }
            }
        });
    }

    /**
     * Crear nueva planta al presionar botón de añadir
     *
     * @param view
     */
    private void createPlant(View view) {
        progressBar.setVisibility(View.VISIBLE);
        final HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("type", type);
        params.put("scientific_name", scientific_name);
        params.put("order", order);
        params.put("img_url", img);
        String endpoint = apiUrl+"/plants"; //  ruta de plantas
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                endpoint, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        Toast.makeText(PlantsActivity.this, "Plant added successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PlantsActivity.this, ManagePlantsActivity.class));
                        progressBar.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));

                        JSONObject obj = new JSONObject(res);
                        Toast.makeText(PlantsActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException | UnsupportedEncodingException je) {
                        je.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return params;
            }
        };

        // Establecer la política de reintentos
        int socketTime = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Agregar petición
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Validar información en los campos a la hora de intentar crear una planta
     *
     * @param view
     * @return
     */
    public boolean validate(View view) {
        boolean isValid;
        if (!TextUtils.isEmpty(name)) {
            if (!TextUtils.isEmpty(type)) {
                if (!TextUtils.isEmpty(scientific_name)) {
                    if (!TextUtils.isEmpty(order)) {
                        if (!TextUtils.isEmpty(img)) {
                            isValid = true;
                        } else {
                            utilService.showSnackBar(view, "please enter image url....");
                            isValid = false;
                        }
                    } else {
                        utilService.showSnackBar(view, "please enter order....");
                        isValid = false;
                    }
                } else {
                    utilService.showSnackBar(view, "please enter scientific name....");
                    isValid = false;
                }
            } else {
                utilService.showSnackBar(view, "please enter type....");
                isValid = false;
            }
        } else {
            utilService.showSnackBar(view, "please enter name....");
            isValid = false;
        }
        return isValid;
    }
}