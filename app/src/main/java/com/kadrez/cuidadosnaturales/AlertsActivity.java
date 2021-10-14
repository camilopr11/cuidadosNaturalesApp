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

public class AlertsActivity extends AppCompatActivity {
    private Button createBtn;
    private EditText name_ET, date_ET, img_ET;
    private Spinner type_SP;
    ProgressBar progressBar;
    private String name, type, dateStr, img;
    private Date date;
    UtilService utilService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alerts);
        name_ET = findViewById(R.id.name_ET);
        type_SP = findViewById(R.id.type_SP);
        date_ET = findViewById(R.id.date_ET);
        img_ET = findViewById(R.id.img_ET);
        progressBar = findViewById(R.id.progress_bar);
        createBtn = findViewById(R.id.createBtn);
        utilService = new UtilService();


        date_ET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilService.hideKeyboard(v, AlertsActivity.this);
                showDateTimeDialog(date_ET);
            }
        });

        // Botón de crear que llama a la función createAlert
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utilService.hideKeyboard(view, AlertsActivity.this);
                name = name_ET.getText().toString();
                type = type_SP.getSelectedItem().toString();
                dateStr = date_ET.getText().toString();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                try {
                    date = format.parse(dateStr);
                    System.out.println(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                img = img_ET.getText().toString();
                if (validate(view)) {
                    createAlert(view);
                }
            }
        });
    }

    /**
     * Mostrar selector de fecha y hora y asignar formato
     *
     * @param date_time_in
     */
    private void showDateTimeDialog(final EditText date_time_in) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

                        date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(AlertsActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };

        new DatePickerDialog(AlertsActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    /**
     * Crear nueva alerta al presionar botón de crear
     *
     * @param view
     */
    private void createAlert(View view) {
        progressBar.setVisibility(View.VISIBLE);
        final HashMap<String, String> params = new HashMap<>();
        params.put("plant", name);
        params.put("type", type);
        params.put("date", date.toString());
        params.put("img_url", img);
        String apiKey = "http://192.168.1.82:3000/alerts/"; // Dirección ip correspondiente a servidor de API, con respectivo puerto y ruta de alertas
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                apiKey, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        Toast.makeText(AlertsActivity.this, "Alert added successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AlertsActivity.this, ManageAlertsActivity.class));
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
                        Toast.makeText(AlertsActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
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
     * Validar información en los campos a la hora de intentar crear una alerta
     *
     * @param view
     * @return
     */
    public boolean validate(View view) {
        boolean isValid;

        if (!TextUtils.isEmpty(name)) {
            if (!TextUtils.equals(type, "Select alert type")) {
                if (!TextUtils.isEmpty(dateStr)) {
                    if (!TextUtils.isEmpty(img)) {
                        isValid = true;
                    } else {
                        utilService.showSnackBar(view, "please enter image url....");
                        isValid = false;
                    }
                } else {
                    utilService.showSnackBar(view, "please enter date....");
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