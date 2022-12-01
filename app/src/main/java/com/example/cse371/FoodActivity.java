package com.example.cse371;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class FoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        findViewById(R.id.chick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RestaurantActivity.class);
                Bundle b = new Bundle();
                b.putString("name", "Chick-n-bap");
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        findViewById(R.id.cafe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RestaurantActivity.class);
                Bundle b = new Bundle();
                b.putString("name", "Iacocca Cafe");
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }
}