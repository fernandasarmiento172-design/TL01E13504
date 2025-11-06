package com.example.tl01e13504;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tl01e13504.Configuraciones.Contactos;
import com.example.tl01e13504.Configuraciones.SQLLiteConexion;
import com.example.tl01e13504.Configuraciones.Transacciones;

import java.util.ArrayList;

public class ActivitySegunda extends AppCompatActivity {

    private ListView listViewContactos;
    private ArrayList<Contactos> listaResultados = new ArrayList<>();
    private SQLLiteConexion dbHelper;
    private Button btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        // Inicialización de vistas
        listViewContactos = findViewById(R.id.listViewContactos);
        btnRegresar = findViewById(R.id.btnatras);

        // Inicialización de la base de datos
        dbHelper = new SQLLiteConexion(this, Transacciones.DBNAME, null, Transacciones.DBVERSION);

        // Botón "Regresar"
        btnRegresar.setOnClickListener(view -> finish());

        // Cargar contactos en el ListView
        mostrarContactos();
    }

    private void mostrarContactos() {
        listaResultados.clear();
        listaResultados.addAll(dbHelper.obtenerContactos());

        if (listaResultados.isEmpty()) {
            Toast.makeText(this, "No hay contactos guardados en la BD.", Toast.LENGTH_LONG).show();
            return;
        }

        ArrayAdapter<Contactos> adaptador = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listaResultados);
        listViewContactos.setAdapter(adaptador);

        Toast.makeText(this, "Se cargaron " + listaResultados.size() + " contactos.", Toast.LENGTH_SHORT).show();
    }
}