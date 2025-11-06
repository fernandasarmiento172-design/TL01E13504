package com.example.tl01e13504;

import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable; // Importación necesaria para onActivityResult
import androidx.appcompat.app.AppCompatActivity;

import com.example.tl01e13504.Configuraciones.ActivityActualizar;
import com.example.tl01e13504.Configuraciones.AdaptadorContactos;
import com.example.tl01e13504.Configuraciones.Contactos;
import com.example.tl01e13504.Configuraciones.SQLLiteConexion;
import com.example.tl01e13504.Configuraciones.Transacciones;

import java.util.ArrayList;

public class ActivitySegunda extends AppCompatActivity {

    // 1. Constante para identificar la solicitud de actualización
    private static final int REQUEST_CODE_ACTUALIZAR = 100; // Puedes usar cualquier número

    private ListView listViewContactos;
    private ArrayList<Contactos> listaResultados = new ArrayList<>();
    private SQLLiteConexion dbHelper;

    // 2. Declaro el botón Actualizar Contacto
    private Button btnRegresar, btnverimagen, btnEliminarContacto, btnActualizarContacto;
    private Contactos contactoSeleccionado = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        // Inicialización de vistas
        listViewContactos = findViewById(R.id.listViewContactos);
        btnRegresar = findViewById(R.id.btnatras);
        btnverimagen = findViewById(R.id.btnverimagen);
        btnEliminarContacto = findViewById(R.id.btneliminarcontacto);
        // 3. Inicialización del botón Actualizar
        btnActualizarContacto = findViewById(R.id.btnactualizarcontacto); // Asumo este ID de tu XML

        // Inicialización de la base de datos
        dbHelper = new SQLLiteConexion(this, Transacciones.DBNAME, null, Transacciones.DBVERSION);

        // Cargar contactos al iniciar
        mostrarContactos();

        // Lógica de SELECCIÓN del contacto en el ListView
        listViewContactos.setOnItemClickListener((parent, view, position, id) -> {
            contactoSeleccionado = listaResultados.get(position);
            Toast.makeText(this, "Seleccionado: " + contactoSeleccionado.getNombre(), Toast.LENGTH_SHORT).show();
        });

        // Lógica del botón VER IMAGEN
        btnverimagen.setOnClickListener(v -> {
            if (contactoSeleccionado == null) {
                Toast.makeText(this, "Selecciona un contacto primero.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(ActivitySegunda.this, VerImagenActivity.class);
            intent.putExtra("imagenBase64", contactoSeleccionado.getImagen());
            startActivity(intent);
        });

        // Lógica del botón ELIMINAR CONTACTO
        btnEliminarContacto.setOnClickListener(v -> {
            if (contactoSeleccionado == null) {
                Toast.makeText(this, "Selecciona un contacto primero para eliminar.", Toast.LENGTH_SHORT).show();
                return;
            }
            mostrarDialogoConfirmacion();
        });

        // 4. LÓGICA DEL BOTÓN ACTUALIZAR CONTACTO
        btnActualizarContacto.setOnClickListener(v -> {
            if (contactoSeleccionado == null) {
                Toast.makeText(this, "Selecciona un contacto para actualizar.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Iniciar la Activity de Edición
            Intent intent = new Intent(ActivitySegunda.this, ActivityActualizar.class);
            // IMPORTANTE: La clase Contactos debe implementar Serializable o Parcelable
            intent.putExtra("contacto_a_editar", contactoSeleccionado);
            startActivityForResult(intent, REQUEST_CODE_ACTUALIZAR);
        });

        // Botón "Regresar"
        btnRegresar.setOnClickListener(view -> finish());

    }

    // 5. Método para manejar el resultado de la ActivityActualizar
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Verificamos si regresamos de la Activity de Actualizar y si la operación fue exitosa
        if (requestCode == REQUEST_CODE_ACTUALIZAR && resultCode == RESULT_OK) {
            // Recargamos la lista para mostrar los cambios
            mostrarContactos();
            contactoSeleccionado = null; // Limpiamos la selección
        }
    }


    private void mostrarDialogoConfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar Eliminación");
        builder.setMessage("¿Estás seguro de que deseas eliminar a " + contactoSeleccionado.getNombre() + "?");

        builder.setPositiveButton("Sí, Eliminar", (dialog, id) -> {
            int resultado = dbHelper.eliminarContacto(contactoSeleccionado.getId());

            if (resultado > 0) {
                Toast.makeText(ActivitySegunda.this,
                        "Contacto '" + contactoSeleccionado.getNombre() + "' eliminado.",
                        Toast.LENGTH_LONG).show();

                contactoSeleccionado = null;
                mostrarContactos();
            } else {
                Toast.makeText(ActivitySegunda.this, "Error al eliminar el contacto.", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, id) -> dialog.dismiss());
        builder.create().show();
    }


    private void mostrarContactos() {
        listaResultados = dbHelper.obtenerContactos();

        if (listaResultados.isEmpty()) {
            Toast.makeText(this, "No hay contactos guardados en la BD.", Toast.LENGTH_LONG).show();
            return;
        }

        AdaptadorContactos adaptador = new AdaptadorContactos(this, listaResultados);
        listViewContactos.setAdapter(adaptador);
    }
}