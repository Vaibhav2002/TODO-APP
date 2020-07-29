package com.example.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class DoesAdapter extends RecyclerView.Adapter<DoesAdapter.MyViewholder> {
    Context context;
    ArrayList<myDoes> myDoes;

    DoesAdapter(Context c, ArrayList<myDoes> p) {
        context = c;
        myDoes = p;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewholder(LayoutInflater.from(context).inflate(R.layout.does, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        holder.TITLE.setText(myDoes.get(position).getTITLE());
        holder.DESCRIPTION.setText(myDoes.get(position).getDESCRIPTION());
    }

    @Override
    public int getItemCount() {
        return myDoes.size();
    }

    class MyViewholder extends RecyclerView.ViewHolder {
        MaterialTextView TITLE, DESCRIPTION;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            TITLE = itemView.findViewById(R.id.titledoes);
            DESCRIPTION = itemView.findViewById(R.id.descdoes);
        }
    }
}
