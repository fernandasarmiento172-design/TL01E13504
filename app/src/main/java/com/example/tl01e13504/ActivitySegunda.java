package com.example.tl01e13504;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast; // Añadir Toast para mensajes de depuración

import androidx.appcompat.app.AppCompatActivity;

// Importar las clases necesarias
import com.example.tl01e13504.Configuraciones.SQLLiteConexion;
import com.example.tl01e13504.Configuraciones.Transacciones;
import com.example.tl01e13504.Configuraciones.Contactos; // ⚠️ AQUI ASUMO QUE Contactos ESTÁ EN Configuraciones
import java.util.List; // Usar List en lugar de ArrayList<String>

public class ActivitySegunda extends AppCompatActivity {

    ListView listViewContactos; // Cambiado de 'listView' a 'listViewContactos'
    SQLLiteConexion dbHelper;
    List<Contactos> listaResultados; // Usar la lista de objetos Contactos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        // 1. Inicialización de vistas y conexión
        listViewContactos = findViewById(R.id.listViewContactos);
        dbHelper = new SQLLiteConexion(this, Transacciones.DBNAME, null, Transacciones.DBVERSION);

        // 2. Llamar al método de lectura
        mostrarContactos();

        // Opcional: Listener para depuración
        listViewContactos.setOnItemClickListener((parent, view, position, id) -> {
            Contactos contactoSeleccionado = listaResultados.get(position);
            Toast.makeText(this, "Seleccionado: " + contactoSeleccionado.getNombre(), Toast.LENGTH_SHORT).show();
        });
    }

    private void mostrarContactos() {
        // 🚀 AHORA USAMOS EL MÉTODO obtenerContactos() DE SQLLiteConexion
        listaResultados = dbHelper.obtenerContactos();

        if (listaResultados.isEmpty()) {
            Toast.makeText(this, "No hay contactos guardados en la BD.", Toast.LENGTH_LONG).show();
            return;
        }

        // El ArrayAdapter llama automáticamente al método toString() de la clase Contactos
        ArrayAdapter<Contactos> adaptador = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listaResultados);

        listViewContactos.setAdapter(adaptador);
        Toast.makeText(this, "Se cargaron " + listaResultados.size()+ " contactos.", Toast.LENGTH_SHORT).show();
    }
}