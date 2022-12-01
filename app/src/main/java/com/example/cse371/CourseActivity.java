package com.example.cse371;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse371.adapter.CourseAdapter;
import com.example.cse371.adapter.SchoolworkAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class CourseActivity extends AppCompatActivity {
    ArrayList<String> courselist = new ArrayList<>();
    String name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        name = getIntent().getStringExtra("name");
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("course").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    RecyclerView listview = findViewById(R.id.recyclerView);
                    listview.setLayoutManager(new LinearLayoutManager(CourseActivity.this));
                    listview.addItemDecoration(new DividerItemDecoration(CourseActivity.this,LinearLayoutManager.VERTICAL));
                    courselist = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists() && document.get("deparment").equals(name)) {
                            courselist.add(String.valueOf(document.get("name")));
                        }
                    }
                    CourseAdapter adapter = new CourseAdapter(CourseActivity.this,courselist);
                    listview.setAdapter(adapter);
                }
            }
        });

    }
}