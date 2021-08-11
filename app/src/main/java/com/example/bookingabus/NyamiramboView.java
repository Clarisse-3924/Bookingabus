package com.example.bookingabus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class NyamiramboView extends AppCompatActivity {
    DatabaseReference reff;
    EditText Name,Email,Initial,Destination,Date,Time;
    Nyamirambo book;
    RecyclerView recyclerView;
    String name ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nyamirambo_view);
        Name = (EditText)findViewById(R.id.name);
        Email= (EditText)findViewById(R.id.email);
        Initial= (EditText)findViewById(R.id.hotel);
        Date = (EditText)findViewById(R.id.number);
        Destination = (EditText)findViewById(R.id.room);
        Time = (EditText) findViewById(R.id.Number2);

        recyclerView = findViewById(R.id.myrecycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reff = FirebaseDatabase.getInstance().getReference().child("BookNyamirambo");
        book=new Nyamirambo();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Nyamirambo> options =
                new FirebaseRecyclerOptions.Builder<Nyamirambo>()
                        .setQuery(reff,Nyamirambo.class)
                        .build();
        FirebaseRecyclerAdapter<Nyamirambo,ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Nyamirambo, ViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Nyamirambo model) {
                        holder.setData(getApplicationContext(),model.getName(),model.getEmail(),
                                model.getDate(),model.getTime());

                        holder.setOnClickListener(new ViewHolder.Clicklistener() {
                            @Override
                            public void onItemlongClick(View view, int position) {

                                name = getItem(position).getName();

                                showDeleteDataDialog(name);
                            }
                        });


                    }


                    @NonNull
                    @Override
                    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.nyamirambolist,parent,false);

                        return new ViewHolder(view);
                    }
                };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    private void showDeleteDataDialog(final String name){
        AlertDialog.Builder builder = new AlertDialog.Builder(NyamiramboView.this);
        builder.setTitle("Delete");
        builder.setMessage("Are you Sure to Delete this Data");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Query query = reff.orderByChild("name").equalTo(name);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(NyamiramboView.this, "Data deleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}