package com.example.tl01e13504;

import android.os.Bundle;
import android.view.View; // 👈 Importar la clase View
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tl01e13504.Configuraciones.SQLLiteConexion;
import com.example.tl01e13504.Configuraciones.Transacciones;
import java.util.ArrayList;

public class ActivitySegunda extends AppCompatActivity {

    ListView listViewContactos;
    ArrayList<com.example.tl01e13504.Contactos> listaResultados;
    SQLLiteConexion dbHelper;

    // Declaración del botón
    Button btnRegresar; // 👈 Ya estaba declarada, ¡perfecto!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        // 1. Inicialización de Vistas
        listViewContactos = findViewById(R.id.listViewContactos);
        btnRegresar = findViewById(R.id.btnatras); // 👈 Inicializar el botón

        dbHelper = new SQLLiteConexion(this, Transacciones.DBNAME, null, Transacciones.DBVERSION);

        // 2. Lógica del botón "Regresar" 🔙
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cierra la actividad actual (ActivitySegunda) y regresa a la anterior (MainActivity)
                finish();
            }
        });

        // 3. Carga de contactos
        mostrarContactos();
    }

    private void mostrarContactos() {
        listaResultados = dbHelper.obtenerContactos();

        if (listaResultados.isEmpty()) {
            Toast.makeText(this, "No hay contactos guardados en la BD.", Toast.LENGTH_LONG).show();
            return;
        }

        ArrayAdapter<com.example.tl01e13504.Contactos> adaptador = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listaResultados);

        listViewContactos.setAdapter(adaptador);
        Toast.makeText(this, "Se cargaron " + listaResultados.size() + " contactos.", Toast.LENGTH_SHORT).show();
    }
}
