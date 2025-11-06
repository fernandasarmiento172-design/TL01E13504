package com.example.tl01e13504;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

// ➡️ IMPORTACIONES DE SQLite
import com.example.tl01e13504.Configuraciones.SQLLiteConexion;
import com.example.tl01e13504.Configuraciones.Transacciones; // Asegúrate de que esta clase exista y sea 'Transacciones'

import java.io.ByteArrayOutputStream;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    //Declaracion de variables Globales
    EditText nombre, telefono, nota, buscarpais;
    ListView listaPaises;
    ArrayAdapter<String> adaptador;
    String[] paises = {
            "Honduras (504)", "Costa Rica (506)", "Guatemala (502)",
            "El Salvador (503)", "Nicaragua (505)", "Panamá (507)"
    };
    ImageView imageView;
    Button btnfoto, btnsalvarcontacto, btncontactossalvados;
    private static final int PERMISO_CAMARA = 101;
    private String fotoBase64 = null;
    private File fotoFile;
    private String paisSeleccionadoStr = "";
    ActivityResultLauncher<Intent> tomarFotoLauncher;
    private String codigoPaisSeleccionado = "";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 1. Inicialización de Vistas
        buscarpais = findViewById(R.id.buscarPais);
        listaPaises = findViewById(R.id.listapaises);
        nombre = findViewById(R.id.nombre);
        telefono = findViewById(R.id.telefono);
        nota = findViewById(R.id.nota);
        imageView = findViewById(R.id.imageView);

        btnfoto = findViewById(R.id.btnfoto);
        btnsalvarcontacto = findViewById(R.id.btnsalvarcontacto);
        btncontactossalvados = findViewById(R.id.btncontactossalvados);

        // --- 2. LÓGICA DEL LISTVIEW DE PAÍSES ---
        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, paises);
        listaPaises.setAdapter(adaptador);

        // Filtro de búsqueda (TextWatcher)
        buscarpais.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adaptador.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        // Listener que guarda el código de país (CLAVE)
        listaPaises.setOnItemClickListener((parent, view, position, id) -> {
            String paisSeleccionado = (String) parent.getItemAtPosition(position);
            paisSeleccionadoStr = paisSeleccionado;
            int inicio = paisSeleccionado.indexOf("(");
            int fin = paisSeleccionado.indexOf(")");
            if (inicio != -1 && fin != -1) {
                codigoPaisSeleccionado = paisSeleccionado.substring(inicio + 1, fin);
            } else {
                codigoPaisSeleccionado = "";
            }
            Toast.makeText(MainActivity.this, "Código de País Seleccionado: " + codigoPaisSeleccionado, Toast.LENGTH_SHORT).show();
        });

        // 3. LISTENERS DE BOTONES

        // ➡️ BOTÓN SALVAR CONTACTO: Llama a la validación antes de guardar en SQLite
        btnsalvarcontacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarCampos();

            }
        });

        btnfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Permisos();
            }
        });

        // ➡️ BOTÓN CONTACTOS SALVADOS: Navega a la ActivitySegunda
        btncontactossalvados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActivitySegunda.class);
                startActivity(intent);
            }
        });

        // Inicialización del ActivityResultLauncher para la cámara
        inicializarCamaraLauncher();
    }

    private void inicializarCamaraLauncher() {
        tomarFotoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        if (fotoFile != null && fotoFile.exists()) {
                            try {
                                Bitmap foto = BitmapFactory.decodeFile(fotoFile.getAbsolutePath());
                                ExifInterface exif = new ExifInterface(fotoFile.getAbsolutePath());
                                int orientation = exif.getAttributeInt(
                                        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                                int rotationInDegrees = exifToDegrees(orientation);

                                Bitmap rotatedBitmap = foto;
                                if (rotationInDegrees != 0) {
                                    Matrix matrix = new Matrix();
                                    matrix.preRotate(rotationInDegrees);
                                    rotatedBitmap = Bitmap.createBitmap(foto, 0, 0,
                                            foto.getWidth(), foto.getHeight(), matrix, true);
                                }
                                imageView.setImageBitmap(rotatedBitmap);
                                fotoBase64 = bitmapToBase64(rotatedBitmap);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "Error al procesar la foto", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "No se pudo obtener la foto", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    // ... [MÉTODOS AUXILIARES: bitmapToBase64, exifToDegrees, Permisos, onRequestPermissionsResult, OpenCamara] ...

    // [Nota: Dejé los métodos auxiliares de la cámara/permisos fuera de esta respuesta para no duplicar código,
    // pero deben estar presentes y completos en tu archivo.]

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private int exifToDegrees(int exifOrientation) {
        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            default:
                return 0;
        }
    }

    private void Permisos() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PERMISO_CAMARA);
        } else {
            OpenCamara();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISO_CAMARA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                OpenCamara();
            } else {
                Toast.makeText(this, "Permiso de Camara denegado ", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void OpenCamara() {
        try {
            fotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "foto_" + System.currentTimeMillis() + ".jpg");
            Uri fotoUri = FileProvider.getUriForFile(this,
                    getPackageName() + ".provider",
                    fotoFile
            );
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
            tomarFotoLauncher.launch(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, "Error al abrir cámara: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void validarCampos() {
        String nombreStr = nombre.getText().toString().trim();
        String telefonoStr = telefono.getText().toString().trim();
        // String notaStr = nota.getText().toString().trim(); // Se recoge más abajo

        StringBuilder mensajeError = new StringBuilder();

        if (nombreStr.isEmpty()) {
            mensajeError.append("- El campo **Nombre** está vacío.\n");
        }
        if (telefonoStr.isEmpty()) {
            mensajeError.append("- El campo **Teléfono** está vacío.\n");
        }
        // Nota: Si 'nota' es obligatorio, descomenta:
        // if (notaStr.isEmpty()) { mensajeError.append("- El campo Nota está vacío.\n"); }

        if (codigoPaisSeleccionado.isEmpty()) {
            mensajeError.append("- Por favor, selecciona un **código de país**.\n");
        }

        if (mensajeError.length() > 0) {
            mostrarAlerta(mensajeError.toString());
        } else {
            guardarContacto();
        }
    }

    private void mostrarAlerta (String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("⚠️ Campos Obligatorios");
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    /**
     * 🚀 IMPLEMENTACIÓN FINAL DEL GUARDADO EN SQLite
     */
    private void guardarContacto() {
        String nombreStr = nombre.getText().toString();
        String telefonoStr = telefono.getText().toString();
        String notaStr = nota.getText().toString();
        String listapais = paisSeleccionadoStr;

        // 1. Crear la instancia de la conexión
        SQLLiteConexion dbContactos = new SQLLiteConexion(
                this,
                Transacciones.DBNAME, // Nombre de la BD
                null,
                Transacciones.DBVERSION // Versión
        );

        // 2. Llamar al método de inserción (debe existir en SQLLiteConexion)
        long id = dbContactos.insertarContacto(
                nombreStr,
                telefonoStr,
                notaStr,
               codigoPaisSeleccionado ,
                fotoBase64
        );

        // 3. Manejar el resultado
        if (id > 0) {
            Toast.makeText(MainActivity.this, "✅ Contacto Guardado Exitosamente (ID: " + id + ")", Toast.LENGTH_LONG).show();

            // Limpiar campos
            nombre.setText("");
            telefono.setText("");
            nota.setText("");
            imageView.setImageDrawable(null);
            fotoBase64 = null;
            codigoPaisSeleccionado = "";

            // Navegar a ActivitySegunda después de guardar
            Intent intent = new Intent(MainActivity.this, ActivitySegunda.class);
            startActivity(intent);

        } else {
            Toast.makeText(MainActivity.this, "❌ Error al guardar el contacto. Revise las restricciones de la BD o Logcat.", Toast.LENGTH_LONG).show();
        }
    }
}