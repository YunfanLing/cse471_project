package com.example.cse371;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse371.adapter.GymAdapter;
import com.example.cse371.adapter.ReviewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class GymActivity extends AppCompatActivity {
    ArrayList<Gym> gymlist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("gym").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    RecyclerView listview = findViewById(R.id.recyclerView);
                    listview.setLayoutManager(new LinearLayoutManager(GymActivity.this));
                    listview.addItemDecoration(new DividerItemDecoration(GymActivity.this,LinearLayoutManager.VERTICAL));
                    gymlist = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Gym gym = new Gym();
                        gym.setDocid(String.valueOf(document.getReference().getId()));
                        gym.setName(String.valueOf(document.get("name")));
                        gym.setTime(String.valueOf(document.get("time")));
                        gym.setPlace(String.valueOf(document.get("place")));
                        gym.setJoined(String.valueOf(document.get("joined")));
                        gymlist.add(gym);
                    }
                    GymAdapter adapter = new GymAdapter(GymActivity.this,gymlist);
                    listview.setAdapter(adapter);
                }
            }
        });
    }
}