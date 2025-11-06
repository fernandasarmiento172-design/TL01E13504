package com.example.tl01e13504;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class VerImagenActivity extends AppCompatActivity {

    private ImageView imageViewContacto;
    private Button btnCerrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_imagen);

        imageViewContacto = findViewById(R.id.imageViewContacto);
        btnCerrar = findViewById(R.id.btnCerrar);

        // Obtener imagen Base64 del Intent
        String imagenBase64 = getIntent().getStringExtra("imagenBase64");

        if (imagenBase64 != null && !imagenBase64.isEmpty()) {
            byte[] decodedBytes = Base64.decode(imagenBase64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            imageViewContacto.setImageBitmap(bitmap);
        }

        btnCerrar.setOnClickListener(v -> finish());
    }
}

