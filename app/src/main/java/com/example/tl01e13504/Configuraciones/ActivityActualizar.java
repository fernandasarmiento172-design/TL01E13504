package com.example.tl01e13504.Configuraciones;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tl01e13504.Configuraciones.Contactos;
import com.example.tl01e13504.Configuraciones.SQLLiteConexion;
import com.example.tl01e13504.Configuraciones.Transacciones;
import com.example.tl01e13504.R;

public class ActivityActualizar extends AppCompatActivity {

    private EditText txtNombre, txtTelefono, txtNota, txtCodigoPais;
    private Button btnGuardar, btnRegresar;
    private SQLLiteConexion dbHelper;
    private Contactos contactoActual; // Usamos este objeto para tener acceso al ID y la imagen actual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);

        dbHelper = new SQLLiteConexion(this, Transacciones.DBNAME, null, Transacciones.DBVERSION);

        // Inicializar vistas
        txtNombre = findViewById(R.id.txtNombreActualizar);
        txtTelefono = findViewById(R.id.txtTelefonoActualizar);
        txtNota = findViewById(R.id.txtNotaActualizar);
        txtCodigoPais = findViewById(R.id.txtCodigoPaisActualizar);
        btnGuardar = findViewById(R.id.btnGuardarCambios);
        btnRegresar = findViewById(R.id.btnRegresarActualizar);


        // 1. Recibir datos del Intent
        contactoActual = (Contactos) getIntent().getSerializableExtra("contacto_a_editar");

        if (contactoActual != null) {
            // 2. Llenar los campos de texto con los datos actuales
            txtNombre.setText(contactoActual.getNombre());
            txtTelefono.setText(contactoActual.getTelefono());
            txtNota.setText(contactoActual.getNota());
            txtCodigoPais.setText(contactoActual.getCodigoPais());
        }

        // 3. Lógica de Guardar Cambios
        btnGuardar.setOnClickListener(v -> guardarCambios());

        // 4. Botón Cancelar/Regresar
        btnRegresar.setOnClickListener(v -> finish());
    }

    private void guardarCambios() {
        String nombre = txtNombre.getText().toString();
        String telefono = txtTelefono.getText().toString();
        String nota = txtNota.getText().toString();
        String codigoPais = txtCodigoPais.getText().toString();

        if (nombre.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "El nombre y el teléfono son obligatorios.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Llamar al método de la base de datos para actualizar
        int resultado = dbHelper.actualizarContacto(
                contactoActual.getId(), // Usamos el ID del objeto recibido
                nombre,
                telefono,
                nota,
                codigoPais,
                contactoActual.getImagen() // Mantenemos la imagen actual ya que no se implementó la edición de la imagen
        );

        if (resultado > 0) {
            Toast.makeText(this, "Contacto actualizado con éxito.", Toast.LENGTH_LONG).show();

            // 🔑 CRUCIAL: Notificar a ActivitySegunda que la actualización fue exitosa
            setResult(RESULT_OK);

            finish();
        } else {
            Toast.makeText(this, "Error al actualizar el contacto.", Toast.LENGTH_LONG).show();
        }
    }
}
