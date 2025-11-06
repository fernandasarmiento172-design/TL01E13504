package com.example.tl01e13504;

import android.os.Bundle;
import android.view.View; // 👈 Importar View
import android.widget.ArrayAdapter;
import android.widget.Button; // 👈 Importar Button
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Importar las clases necesarias
import com.example.tl01e13504.Configuraciones.SQLLiteConexion;
import com.example.tl01e13504.Configuraciones.Transacciones;
import com.example.tl01e13504.Configuraciones.Contactos;
import java.util.List;

public class ActivitySegunda extends AppCompatActivity {

    ListView listViewContactos;
    SQLLiteConexion dbHelper;
    List<Contactos> listaResultados;

    // 1. Declaración del botón
    Button btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        // 1. Inicialización de vistas y conexión
        listViewContactos = findViewById(R.id.listViewContactos);

        // 2. Inicialización del botón "Regresar"
        btnRegresar = findViewById(R.id.btnatras);

        dbHelper = new SQLLiteConexion(this, Transacciones.DBNAME, null, Transacciones.DBVERSION);

        // 3. Programar el Listener del botón "Regresar"
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 🚀 Cierra la actividad actual y regresa a la actividad anterior (MainActivity)
                finish();
            }
        });

        // 4. Llamar al método de lectura
        mostrarContactos();

        // Opcional: Listener para depuración
        listViewContactos.setOnItemClickListener((parent, view, position, id) -> {
            Contactos contactoSeleccionado = listaResultados.get(position);
            Toast.makeText(this, "Seleccionado: " + contactoSeleccionado.getNombre(), Toast.LENGTH_SHORT).show();
        });
    }

    private void mostrarContactos() {
        listaResultados = dbHelper.obtenerContactos();

        if (listaResultados.isEmpty()) {
            Toast.makeText(this, "No hay contactos guardados en la BD.", Toast.LENGTH_LONG).show();
            return;
        }

        ArrayAdapter<Contactos> adaptador = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listaResultados);

        listViewContactos.setAdapter(adaptador);
        Toast.makeText(this, "Se cargaron " + listaResultados.size()+ " contactos.", Toast.LENGTH_SHORT).show();
    }
}