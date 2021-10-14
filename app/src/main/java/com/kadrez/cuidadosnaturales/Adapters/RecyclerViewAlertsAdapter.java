package com.kadrez.cuidadosnaturales.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.Glide;
import com.kadrez.cuidadosnaturales.AlertListActivity;
import com.kadrez.cuidadosnaturales.Models.Alert;
import com.kadrez.cuidadosnaturales.R;

import java.util.List;


public class RecyclerViewAlertsAdapter extends RecyclerView.Adapter<RecyclerViewAlertsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Alert> mData;
    RequestOptions option;


    public RecyclerViewAlertsAdapter(Context mContext, List<Alert> mData) {
        this.mContext = mContext;
        this.mData = mData;

        // Crear petición mediante Glide
        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.alert_list_items, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, AlertListActivity.class);
                i.putExtra("plant_name", mData.get(viewHolder.getAdapterPosition()).getName());
                i.putExtra("alert_type", mData.get(viewHolder.getAdapterPosition()).getAlertType());
                i.putExtra("date", mData.get(viewHolder.getAdapterPosition()).getDate());
                i.putExtra("plant_img", mData.get(viewHolder.getAdapterPosition()).getImage_url());

                mContext.startActivity(i);

            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_name.setText(mData.get(position).getName());
        holder.tv_type.setText(mData.get(position).getAlertType());
        holder.tv_date.setText(mData.get(position).getDate().toString());

        // Cargar una imagen de internet y ponerla en Imageview usando Glide

        Glide.with(mContext).load(mData.get(position).getImage_url()).apply(option).into(holder.img_thumbnail);


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        TextView tv_type;
        TextView tv_date;
        ImageView img_thumbnail;
        LinearLayout view_container;


        public MyViewHolder(View itemView) {
            super(itemView);

            view_container = itemView.findViewById(R.id.container);
            tv_name = itemView.findViewById(R.id.tvName);
            tv_type = itemView.findViewById(R.id.tvType2);
            tv_date = itemView.findViewById(R.id.tvDate);
            img_thumbnail = itemView.findViewById(R.id.thumbnail);

        }
    }

}