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
import com.kadrez.cuidadosnaturales.Models.Info;
import com.kadrez.cuidadosnaturales.R;

import java.util.List;


public class RecyclerViewInfoAdapter extends RecyclerView.Adapter<RecyclerViewInfoAdapter.MyViewHolder> {

    private Context mContext;
    private List<Info> mData;
    RequestOptions option;


    public RecyclerViewInfoAdapter(Context mContext, List<Info> mData) {
        this.mContext = mContext;
        this.mData = mData;

        // Crear petición mediante Glide
        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.info_list_items, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, AlertListActivity.class);
                i.putExtra("title", mData.get(viewHolder.getAdapterPosition()).getTitle());
                i.putExtra("description", mData.get(viewHolder.getAdapterPosition()).getDescription());
                i.putExtra("content", mData.get(viewHolder.getAdapterPosition()).getContent());
                i.putExtra("category", mData.get(viewHolder.getAdapterPosition()).getCategory());

                mContext.startActivity(i);

            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_title.setText(mData.get(position).getTitle());
        holder.tv_description.setText(mData.get(position).getDescription());
        holder.tv_content.setText(mData.get(position).getContent());
        holder.tv_category.setText(mData.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        TextView tv_description;
        TextView tv_content;
        TextView tv_category;
        LinearLayout view_container;


        public MyViewHolder(View itemView) {
            super(itemView);

            view_container = itemView.findViewById(R.id.container);
            tv_title = itemView.findViewById(R.id.tvTitle);
            tv_description = itemView.findViewById(R.id.tvDescription);
            tv_content = itemView.findViewById(R.id.tvContent);
            tv_category = itemView.findViewById(R.id.tvCategory);


        }
    }

}