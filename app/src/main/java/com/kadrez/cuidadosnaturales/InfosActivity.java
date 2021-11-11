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

public class InfosActivity extends AppCompatActivity {
    private Button addBtn;
    private EditText title_ET, desc_ET, content_ET, category_ET;
    ProgressBar progressBar;
    private String title, description, content, category;
    private Date date;
    UtilService utilService;
    static final String apiUrl = "https://cuidadosnaturales.herokuapp.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_infos);
        title_ET = findViewById(R.id.title_ET);
        desc_ET = findViewById(R.id.desc_ET);
        content_ET = findViewById(R.id.content_ET);
        category_ET = findViewById(R.id.category_ET);
        progressBar = findViewById(R.id.progress_bar);
        addBtn = findViewById(R.id.addBtn);
        utilService = new UtilService();

        // Botón de añadir que llama a la función createInfo
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utilService.hideKeyboard(view, InfosActivity.this);
                title = title_ET.getText().toString();
                description = desc_ET.getText().toString();
                content = content_ET.getText().toString();
                category = category_ET.getText().toString();
                if (validate(view)) {
                    createInfo(view);
                }
            }
        });
    }

    /**
     * Añadir nueva información en enciclopedia al presionar botón de añadir
     *
     * @param view
     */
    private void createInfo(View view) {
        progressBar.setVisibility(View.VISIBLE);
        final HashMap<String, String> params = new HashMap<>();
        params.put("title", title);
        params.put("description", description);
        params.put("content", content);
        params.put("category", category);
        String endpoint = apiUrl+"/enciclopedias/"; // ruta de enciclopedias
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                endpoint, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        Toast.makeText(InfosActivity.this, "Info added successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(InfosActivity.this, ManageEncyclopediaActivity.class));
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
                        Toast.makeText(InfosActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
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
     * Validar información en los campos a la hora de intentar añadir nuevos datos a enciclopedia
     *
     * @param view
     * @return
     */
    public boolean validate(View view) {
        boolean isValid;
        if (!TextUtils.isEmpty(title)) {
            if (!TextUtils.isEmpty(description)) {
                if (!TextUtils.isEmpty(content)) {
                    if (!TextUtils.isEmpty(category)) {
                        isValid = true;
                    } else {
                        utilService.showSnackBar(view, "please enter category....");
                        isValid = false;
                    }
                } else {
                    utilService.showSnackBar(view, "please enter content....");
                    isValid = false;
                }
            } else {
                utilService.showSnackBar(view, "please enter description....");
                isValid = false;
            }
        } else {
            utilService.showSnackBar(view, "please enter title....");
            isValid = false;
        }
        return isValid;
    }
}