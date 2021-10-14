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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kadrez.cuidadosnaturales.AlertListActivity;
import com.kadrez.cuidadosnaturales.Models.Plant;
import com.kadrez.cuidadosnaturales.R;

import java.util.List;


public class RecyclerViewPlantsAdapter extends RecyclerView.Adapter<RecyclerViewPlantsAdapter.MyViewHolder> {

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