package com.example.todo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class DoesAdapter extends RecyclerView.Adapter<DoesAdapter.MyViewholder> {
    Context context;
    ArrayList<myDoes> myDoes;
    String username;

    DoesAdapter(Context c, ArrayList<myDoes> p, String user) {
        context = c;
        myDoes = p;
        username = user;
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewholder(LayoutInflater.from(context).inflate(R.layout.does, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewholder holder, final int position) {
        final boolean[] ischecked = {false};
        holder.TITLE.setText(myDoes.get(position).getTITLE());
        holder.DESCRIPTION.setText(myDoes.get(position).getDESCRIPTION());
        holder.DATE.setText(myDoes.get(position).getDATE());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditTodo.class);
                intent.putExtra("Title", myDoes.get(position).TITLE);
                intent.putExtra("Desc", myDoes.get(position).DESCRIPTION);
                intent.putExtra("Username", username);
                intent.putExtra("Date",myDoes.get(position).DATE);
                context.startActivity(intent);
            }
        });

        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ischecked[0])
                    holder.check.setSpeed(3);
                else
                    holder.check.setSpeed(-3);
                holder.check.playAnimation();
                ischecked[0] = !ischecked[0];
            }
        });
    }

    @Override
    public int getItemCount() {
        return myDoes.size();
    }

    static class MyViewholder extends RecyclerView.ViewHolder {
        MaterialTextView TITLE, DESCRIPTION,DATE;
        LottieAnimationView check;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            TITLE = itemView.findViewById(R.id.titledoes);
            DESCRIPTION = itemView.findViewById(R.id.descdoes);
            DATE=itemView.findViewById(R.id.datedoes);
            check = itemView.findViewById(R.id.checkTodo);
        }
    }
}
