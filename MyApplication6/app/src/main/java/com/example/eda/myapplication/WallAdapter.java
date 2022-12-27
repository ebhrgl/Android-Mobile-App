package com.example.eda.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class WallAdapter  extends RecyclerView.Adapter<WallAdapter.MyViewHolder>{
    private List<Wall> MessageList;
    private int currentSelectedPosition = RecyclerView.NO_POSITION;
    private Context context;

    public  class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView wallname;
        public TextView latitude;
        public TextView longtitude;
        public int id;

        public MyViewHolder(View view) {
            super(view);
            context = itemView.getContext();
            wallname = (TextView) view.findViewById(R.id.wallname);
        }
    }

    public WallAdapter(List<Wall>MessagesList) {

        this.MessageList = MessagesList;
    }

    public int getItemCount() {
        return MessageList.size();
    }
    public WallAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item2, parent, false);

        return new WallAdapter.MyViewHolder(itemView);
    }

    public void onBindViewHolder(final WallAdapter.MyViewHolder holder, final int position) {

        //binding the data from our ArrayList of object to the item.xml using the viewholder

        Wall m = MessageList.get(position);
        holder.wallname.setText(m.getWallname());
        holder.id = m.getId();
        holder.wallname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        Intent intent =  new Intent(context, MessageActivity.class);
        intent.putExtra("wallname",holder.wallname.getText().toString());
        intent.putExtra("wallid",holder.id);
        context.startActivity(intent);
            }
        });

    }

}
