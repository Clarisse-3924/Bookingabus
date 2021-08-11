package com.example.bookingabus;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {


    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClicklistener.onItemlongClick(view,getAdapterPosition());
                return false;
            }
        });
    }

    private ViewHolder.Clicklistener mClicklistener;

    public void setData(Context applicationContext, String name, String email, String Date, String Time) {
        TextView textView = itemView.findViewById(R.id.hello);

        textView.setText("Name: " + name + "\n"  + "Phone Number: "  + email  + "\n"+"Date:" +Date +"\n"  + "Time:" +Time);
    }

    public interface Clicklistener{
        void onItemlongClick(View view , int position);
    }

    public void setOnClickListener(ViewHolder.Clicklistener clickListener){
        mClicklistener = (Clicklistener) clickListener;
    }

}