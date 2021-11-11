package com.kadrez.cuidadosnaturales.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kadrez.cuidadosnaturales.AlertListActivity;
import com.kadrez.cuidadosnaturales.ManagePlantsActivity;
import com.kadrez.cuidadosnaturales.Models.Plant;
import com.kadrez.cuidadosnaturales.PlantListActivity;
import com.kadrez.cuidadosnaturales.PlantsActivity;
import com.kadrez.cuidadosnaturales.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecyclerViewPlantsAdapter extends RecyclerView.Adapter<RecyclerViewPlantsAdapter.MyViewHolder> {

    static final String apiUrl = "https://cuidadosnaturales.herokuapp.com";
    private Context mContext;
    private List<Plant> mData;
    RequestOptions option;


    public RecyclerViewPlantsAdapter(Context mContext, List<Plant> mData) {
        this.mContext = mContext;
        this.mData = mData;

        // Crear petición mediante Glide
        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.plant_list_items, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, AlertListActivity.class);
                i.putExtra("name", mData.get(viewHolder.getAdapterPosition()).getName());
                i.putExtra("type", mData.get(viewHolder.getAdapterPosition()).getType());
                i.putExtra("scientific_name", mData.get(viewHolder.getAdapterPosition()).getScientificName());
                i.putExtra("order", mData.get(viewHolder.getAdapterPosition()).getOrder());
                i.putExtra("img_url", mData.get(viewHolder.getAdapterPosition()).getImg_url());

                mContext.startActivity(i);

            }
        });

        viewHolder.view_container.setOnLongClickListener(new View.OnLongClickListener() {

            @Override

            public boolean onLongClick(View view) {
                String id = mData.get(viewHolder.getAdapterPosition()).getId();
                /**
                 * Obtener lista de plantas mediante petición a API
                 *
                 * @param view
                 */
                String endpoint = apiUrl+"/plants/"+id;  // ruta de plantas
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE,
                        endpoint, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                Toast.makeText(mContext, "Plant deleted successfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(mContext, PlantListActivity.class);
                                mContext.startActivity(i);
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
                                Toast.makeText(mContext, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException | UnsupportedEncodingException je) {
                                je.printStackTrace();
                            }
                        }
                    }
                }) {
                };

                    // Establecer la política de reintentos
                    int socketTime = 3000;
                    RetryPolicy policy = new DefaultRetryPolicy(socketTime,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    jsonObjectRequest.setRetryPolicy(policy);

                    // Agregar petición
                    RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                    requestQueue.add(jsonObjectRequest);


                return true;

            }

        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_name.setText(holder.tv_name.getText() + " " + (mData.get(position).getName()));
        holder.tv_type.setText(holder.tv_type.getText() + " " + (mData.get(position).getType()));
        holder.tv_scName.setText(holder.tv_scName.getText() + " " + mData.get(position).getScientificName());
        holder.tv_order.setText(holder.tv_order.getText() + " " + mData.get(position).getOrder());

        // Cargar una imagen de internet y ponerla en Imageview usando Glide

        Glide.with(mContext).load(mData.get(position).getImg_url()).apply(option).into(holder.img_thumbnail);


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        TextView tv_type;
        TextView tv_scName;
        TextView tv_order;
        ImageView img_thumbnail;
        LinearLayout view_container;


        public MyViewHolder(View itemView) {
            super(itemView);

            view_container = itemView.findViewById(R.id.container);
            tv_name = itemView.findViewById(R.id.tvName);
            tv_type = itemView.findViewById(R.id.tvType);
            tv_scName = itemView.findViewById(R.id.tvScName);
            tv_order = itemView.findViewById(R.id.tvOrder);
            img_thumbnail = itemView.findViewById(R.id.thumbnail);

        }
    }

}