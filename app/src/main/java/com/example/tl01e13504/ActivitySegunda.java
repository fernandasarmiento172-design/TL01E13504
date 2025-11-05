 package com.example.tl01e13504;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

 public class ActivitySegunda extends AppCompatActivity {
     ListView listViewContactos ;
     ArrayAdapter<String> adaptador;


     Button btnatras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_segunda);
        listViewContactos = findViewById(R.id.listViewContactos);
        Button botonAtras = findViewById(R.id.btnatras);
        botonAtras.setOnClickListener(view -> {
            finish();
        });
    }
 }