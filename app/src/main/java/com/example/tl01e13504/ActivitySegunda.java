package com.example.tl01e13504;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.tl01e13504.Configuraciones.Contacto;
import com.example.tl01e13504.Configuraciones.DbContactos;
import java.util.ArrayList;
import java.util.List;

public class ActivitySegunda extends AppCompatActivity {

    ListView listViewContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        listViewContactos = findViewById(R.id.listViewContactos);

        // Botón atrás
        Button btnAtras = findViewById(R.id.btnatras);
        if (btnAtras != null) {
            btnAtras.setOnClickListener(v -> finish());
        }

        // Llamada inicial (solo para mostrar al abrir)
        cargarLista();
    }

    // 👇 OJO: este método debe ir fuera de onCreate()
    @Override
    protected void onResume() {
        super.onResume();
        cargarLista(); // Se ejecuta cada vez que la Activity vuelve a mostrarse
    }

    private void cargarLista() {
        DbContactos dbContactos = new DbContactos(ActivitySegunda.this);
        ArrayList<Contacto> listaObjetos = dbContactos.mostrarContactos();

        List<String> listaStrings = new ArrayList<>();

        // Si la lista está vacía, agregaremos un mensaje de error manual.
        if (listaObjetos.isEmpty()) {
            listaStrings.add("--- ERROR: BASE DE DATOS VACÍA O FALLÓ LECTURA ---");
        } else {
            // Usa tu bucle original si la lista tiene algo
            for (Contacto contacto : listaObjetos) {
                String item = contacto.getNombre() +
                        " | Cód: " + contacto.getCodigoPais() +
                        " | Tel: " + contacto.getTelefono();
                listaStrings.add(item);
            }
        }

        Toast.makeText(this, "Items leídos de la BD: " + listaObjetos.size(), Toast.LENGTH_LONG).show();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listaStrings
        );
        listViewContactos.setAdapter(adapter);
    }
}