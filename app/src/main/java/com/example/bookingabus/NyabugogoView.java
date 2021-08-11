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

public class NyabugogoView extends AppCompatActivity {
    DatabaseReference reff;
    EditText Name,Email,Initial,Destination,Date,Time;
    Nyabugogo book;
    RecyclerView recyclerView;
    String name ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nyabugogo_view);
        Name = (EditText)findViewById(R.id.name);
        Email= (EditText)findViewById(R.id.email);
        Initial= (EditText)findViewById(R.id.hotel);
        Date = (EditText)findViewById(R.id.number);
        Destination = (EditText)findViewById(R.id.room);
        Time = (EditText) findViewById(R.id.Number2);

        recyclerView = findViewById(R.id.myrecycleview3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reff = FirebaseDatabase.getInstance().getReference().child("BookNyabugogo");
        book=new Nyabugogo();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Nyabugogo> options =
                new FirebaseRecyclerOptions.Builder<Nyabugogo>()
                        .setQuery(reff,Nyabugogo.class)
                        .build();
        FirebaseRecyclerAdapter<Nyabugogo,ViewHolder2> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Nyabugogo, ViewHolder2>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolder2 holder, int position, @NonNull Nyabugogo model) {
                        holder.setData(getApplicationContext(),model.getName(),model.getEmail(),
                                model.getDate(),model.getTime());

                        holder.setOnClickListener(new ViewHolder2.Clicklistener() {
                            @Override
                            public void onItemlongClick(View view, int position) {

                                name = getItem(position).getName();

                                showDeleteDataDialog(name);
                            }
                        });


                    }


                    @NonNull
                    @Override
                    public ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.nyabugogolist,parent,false);

                        return new ViewHolder2(view);
                    }
                };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    private void showDeleteDataDialog(final String name){
        AlertDialog.Builder builder = new AlertDialog.Builder(NyabugogoView.this);
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
                        Toast.makeText(NyabugogoView.this, "Data deleted", Toast.LENGTH_SHORT).show();
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