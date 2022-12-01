package com.example.cse371;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse371.adapter.GymAdapter;
import com.example.cse371.adapter.ReviewAdapter;
import com.example.cse371.adapter.SchoolworkAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class SchoolworkActivity extends AppCompatActivity {
    ArrayList<String> schoolworklist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shool_work);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("schoolwork").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    RecyclerView listview = findViewById(R.id.recyclerView);
                    listview.setLayoutManager(new LinearLayoutManager(SchoolworkActivity.this));
                    listview.addItemDecoration(new DividerItemDecoration(SchoolworkActivity.this,LinearLayoutManager.VERTICAL));
                    schoolworklist = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            schoolworklist.add(String.valueOf(document.get("deparment")));
                        }
                    }
                    SchoolworkAdapter adapter = new SchoolworkAdapter(SchoolworkActivity.this,schoolworklist);
                    listview.setAdapter(adapter);
                }
            }
        });

    }
}