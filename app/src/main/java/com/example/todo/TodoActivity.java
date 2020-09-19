package com.example.todo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class TodoActivity extends AppCompatActivity {
    MaterialTextView textView;
    RecyclerView recyclerView;
    ArrayList<myDoes> list;
    DoesAdapter doesAdapter;
    ImageView newtodo,moreitem;
    ScrollView scrollView;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        textView=findViewById(R.id.usernamedisp);
        newtodo=findViewById(R.id.newtodobtn);
        scrollView=findViewById(R.id.scroll);
        moreitem=findViewById(R.id.more);
        username=getIntent().getExtras().getString("Username");
        textView.setText(username);
        recyclerView=findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();

        moreitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu=new PopupMenu(TodoActivity.this,v);
                popupMenu.inflate(R.menu.todomenu);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.editacc:
                                Intent intent=new Intent(TodoActivity.this,editAccDetails.class);
                                System.out.println(username);
                                intent.putExtra("username",username);
                                startActivity(intent);
                                return true;
                            case R.id.logout:
                                startActivity(new Intent(TodoActivity.this,LoginActivity.class));
                                finish();
                                return true;
                        }
                        return false;
                    }
                });
            }
        });

        newtodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TodoActivity.this,newTodo.class);
                intent.putExtra("todos",list.size());
                intent.putExtra("name",username);
                startActivity(intent);
            }
        });
        scrollView.setSmoothScrollingEnabled(true);
        scrollView.fling(20);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users").child(username).child("TODOS");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    myDoes p=dataSnapshot.getValue(myDoes.class);
                    list.add(p);
                }
                System.out.println(list.size());
                doesAdapter =new DoesAdapter(TodoActivity.this,list,username);
                doesAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(doesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TodoActivity.this, "NO data found", Toast.LENGTH_SHORT).show();
            }
        });
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN|ItemTouchHelper.UP,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
                        .addBackgroundColor(getResources().getColor(R.color.delete))
                        .addActionIcon(R.drawable.ic_baseline_delete_24)
                        .addSwipeLeftLabel("Delete")
                        .addSwipeRightLabel("Delete")
                        .setActionIconTint(Color.parseColor("#000000"))
                        .setSwipeLeftLabelColor(Color.parseColor("#000000"))
                        .setSwipeRightLabelColor(Color.parseColor("#000000"))
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return 0.5f;
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Collections.swap(list,viewHolder.getAdapterPosition(),target.getAdapterPosition());
                doesAdapter.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(new ContextThemeWrapper(TodoActivity.this,R.style.Theme_AppCompat_Dialog_Alert));
                builder.setMessage("Confrim Delete")
                        .setTitle("Delete Todo")
                        .setCancelable(true)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final int x=viewHolder.getAdapterPosition();
                                System.out.println(viewHolder.getAdapterPosition());
                                final myDoes myDoes=list.get(viewHolder.getAdapterPosition());
                                list.remove(viewHolder.getAdapterPosition());
                                final boolean[] flag = {false};
                                RelativeLayout relativeLayout=findViewById(R.id.todolayout);
                                doesAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("users").child(username);
                                databaseReference1.child("TODOS").child(myDoes.getTITLE()).removeValue();
                               Snackbar snackbar= Snackbar.make(relativeLayout,"A todo was removed ",Snackbar.LENGTH_LONG);
                               snackbar.setAction("UNDO", new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       flag[0] =true;
                                       list.add(x,myDoes);
                                       System.out.println(list.toString());
                                       DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("users").child(username);
                                       databaseReference1.child("TODOS").child(myDoes.getTITLE()).setValue(myDoes);
                                       doesAdapter.notifyItemInserted(x);
                                   }
                               }).setActionTextColor(Color.RED).show();
                                System.out.println("Flag val : "+flag[0]);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                doesAdapter.notifyDataSetChanged();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}