package com.example.eda.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    private List<Message> MessageList;

    public  class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nickname;
        public TextView message;

        public MyViewHolder(View view) {
            super(view);

            nickname = (TextView) view.findViewById(R.id.nickname);
            message = (TextView) view.findViewById(R.id.message);
        }
    }

    public MessageAdapter(List<Message>MessagesList) {

        this.MessageList = MessagesList;
    }

     public int getItemCount() {
        return MessageList.size();
    }
    public MessageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new MessageAdapter.MyViewHolder(itemView);
    }

    public void onBindViewHolder(final MessageAdapter.MyViewHolder holder, final int position) {

        //binding the data from our ArrayList of object to the item.xml using the viewholder

        Message m = MessageList.get(position);
        holder.nickname.setText(m.getNickname());

        holder.message.setText(m.getMessage() );

    }

}
