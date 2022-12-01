package com.example.cse371;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse371.adapter.QaAdapter;
import com.example.cse371.adapter.ReviewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class QaActivity extends AppCompatActivity {
    ArrayList<Qa> qalist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa);

        Bundle b = getIntent().getExtras();
        String name = "";
        if(b != null) {
            name = b.getString("name");
        }
        String finalName = name;
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(QaActivity.this);
                final EditText edittext = new EditText(QaActivity.this);
                alert.setMessage("");
                alert.setTitle("Question");

                alert.setView(edittext);
                alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String content = edittext.getText().toString();

                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                        Random r = new Random();
                        int i2 = r.nextInt(100000);
                        DocumentReference documentReference = firebaseFirestore.collection("qa").document(String.valueOf(i2));
                        Map<String, Object> hm = new HashMap<>();
                        hm.put("name",finalName);
                        hm.put("qa",content);
                        hm.put("qa_author",LoginActivity.username);
                        hm.put("replies","");
                        documentReference.set(hm).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //Toast.makeText(AddReviewActivity.this, "Success", Toast.LENGTH_LONG).show();
                                new AlertDialog.Builder(QaActivity.this)
                                        .setTitle("Tip")
                                        .setMessage("Submit question success!")
                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                                firebaseFirestore.collection("qa").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            RecyclerView listview = findViewById(R.id.recyclerView);
                                                            listview.setLayoutManager(new LinearLayoutManager(QaActivity.this));
                                                            listview.addItemDecoration(new DividerItemDecoration(QaActivity.this,LinearLayoutManager.VERTICAL));
                                                            qalist = new ArrayList<>();
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                if (document.exists() && document.get("name").equals(finalName)) {
                                                                    Qa qa = new Qa();
                                                                    qa.setDocid(String.valueOf(document.getReference().getId()));
                                                                    qa.setName(String.valueOf(document.get("name")));
                                                                    qa.setQa(String.valueOf(document.get("qa")));
                                                                    qa.setQa_author(String.valueOf(document.get("qa_author")));
                                                                    String replies = String.valueOf(document.get("replies"));
                                                                    String[] s = replies.split("###");
                                                                    ArrayList<QaReply> qaReplies = new ArrayList<>();
                                                                    for (int i = 0; i < s.length; i++) {
                                                                        String content = s[i];
                                                                        String[] c = content.split("@@@");
                                                                        if (c.length==2){
                                                                            QaReply qr = new QaReply();
                                                                            qr.setName(c[0]);
                                                                            qr.setContent(c[1]);
                                                                            qaReplies.add(qr);
                                                                        }
                                                                    }
                                                                    qa.setReplies(qaReplies);
                                                                    qalist.add(qa);
                                                                }
                                                            }
                                                            QaAdapter adapter = new QaAdapter(QaActivity.this,qalist);
                                                            listview.setAdapter(adapter);
                                                        }
                                                    }
                                                });
                                            }
                                        })
                                        .show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                alert.show();
            }
        });

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("qa").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    RecyclerView listview = findViewById(R.id.recyclerView);
                    listview.setLayoutManager(new LinearLayoutManager(QaActivity.this));
                    listview.addItemDecoration(new DividerItemDecoration(QaActivity.this,LinearLayoutManager.VERTICAL));
                    qalist = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists() && document.get("name").equals(finalName)) {
                            Qa qa = new Qa();
                            qa.setDocid(String.valueOf(document.getReference().getId()));
                            qa.setName(String.valueOf(document.get("name")));
                            qa.setQa(String.valueOf(document.get("qa")));
                            qa.setQa_author(String.valueOf(document.get("qa_author")));
                            String replies = String.valueOf(document.get("replies"));
                            String[] s = replies.split("###");
                            ArrayList<QaReply> qaReplies = new ArrayList<>();
                            for (int i = 0; i < s.length; i++) {
                                String content = s[i];
                                String[] c = content.split("@@@");
                                if (c.length==2){
                                    QaReply qr = new QaReply();
                                    qr.setName(c[0]);
                                    qr.setContent(c[1]);
                                    qaReplies.add(qr);
                                }
                            }
                            qa.setReplies(qaReplies);
                            qalist.add(qa);
                        }
                    }
                    QaAdapter adapter = new QaAdapter(QaActivity.this,qalist);
                    listview.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 9999) {
//                Review review = (Review) data.getSerializableExtra("review");
//                qalist.add(review);
//                ReviewAdapter adapter = new ReviewAdapter(QaActivity.this,reviewlist);
//                RecyclerView listview = findViewById(R.id.recyclerView);
//                listview.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
            }
        }
    }
}