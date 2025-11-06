package com.example.tl01e13504;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
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

import com.example.tl01e13504.Configuraciones.DbContactos;


import java.io.ByteArrayOutputStream;
import java.io.File;



public class MainActivity extends AppCompatActivity {

    //Declaracion de variables Globales
    EditText nombre, telefono, nota, buscarpais;
    ListView listaPaises;
    ArrayAdapter<String> adaptador;
    String[] paises = {
            "Honduras (504)",
            "Costa Rica (506)",
            "Guatemala (502)",
            "El Salvador (503)",
            "Nicaragua (505)",
            "Panamá (507)"
    };
    ImageView imageView;
    Button btnfoto, btnsalvarcontacto, btncontactossalvados;
    private static final int PERMISO_CAMARA = 101;
    private String fotoBase64 = null;
    private File fotoFile;

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
        nombre = (EditText) findViewById(R.id.nombre);
        telefono = (EditText) findViewById(R.id.telefono);
        nota = (EditText) findViewById(R.id.nota);
        imageView = (ImageView) findViewById(R.id.imageView);

        btnfoto = (Button) findViewById(R.id.btnfoto);
        btnsalvarcontacto = (Button) findViewById(R.id.btnsalvarcontacto);
        btncontactossalvados = (Button) findViewById(R.id.btncontactossalvados);

        // --- 2. LÓGICA DEL LISTVIEW DE PAÍSES (CONSOLIDADA AQUÍ) ---

        // Configuramos el adaptador del ListView
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

            // Extraer el código de país (ej. 504)
            int inicio = paisSeleccionado.indexOf("(");
            int fin = paisSeleccionado.indexOf(")");
            if (inicio != -1 && fin != -1) {
                // Guarda el código de país en la variable global
                codigoPaisSeleccionado = paisSeleccionado.substring(inicio + 1, fin);
            } else {
                codigoPaisSeleccionado = "";
            }

            Toast.makeText(MainActivity.this, "Seleccionado: " + paisSeleccionado, Toast.LENGTH_SHORT).show();

            // Opcional: Ocultar el ListView después de seleccionar (si lo deseas)
            // listaPaises.setVisibility(View.GONE);
        });

        // --- 3. LISTENERS DE BOTONES Y CÁMARA ---

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

        // Listener para la cámara (ActivityResultLauncher)
        tomarFotoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        if (fotoFile != null && fotoFile.exists()) {
                            try {
                                // ... (Lógica de procesamiento de foto y Base64)
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

        btncontactossalvados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lanza la actividad para ver la lista
                Intent intent = new Intent(MainActivity.this, ActivitySegunda.class);
                startActivity(intent);
            }
        });
    }

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, int deviceId) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId);
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
            // Crear archivo temporal
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

        // **IMPORTANTE:** Toda la lógica del ListView de países ha sido eliminada de aquí.
        // Solo debe estar la lógica de la cámara.
    }

    private void validarCampos() {
        String nombreStr = nombre.getText().toString().trim();
        String telefonoStr = telefono.getText().toString().trim();
        String notaStr = nota.getText().toString().trim();

        StringBuilder mensajeError = new StringBuilder();

        if (nombreStr.isEmpty()) {
            mensajeError.append("El campo Nombre está vacío.\n");
        }
        if (telefonoStr.isEmpty()) {
            mensajeError.append("El campo Teléfono está vacío.\n");
        }
        if (notaStr.isEmpty()) {
            mensajeError.append("El campo Nota está vacío.\n");
        }

        // AGREGAR LA VERIFICACIÓN DEL CÓDIGO DE PAÍS AQUÍ
        // Si el usuario no ha hecho clic en la lista, la variable estará vacía.
        if (codigoPaisSeleccionado.isEmpty()) {
            mensajeError.append("Por favor, selecciona un código de país.\n");
        }


        if (mensajeError.length() > 0) {
            // Muestra la alerta con el mensaje específico
            mostrarAlerta(mensajeError.toString());
        } else {
            // Si todos los campos y el país están seleccionados, guardar
            guardarContacto();
        }
    }

    private void mostrarAlerta (String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Campos Obligatorios");
        builder.setMessage(mensaje); // Usa el mensaje específico
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void guardarContacto() {
        String nombreStr = nombre.getText().toString();
        String telefonoStr = telefono.getText().toString();
        String notaStr = nota.getText().toString();

        // La validación ya se hizo en validarCampos(), aquí se asume que codigoPaisSeleccionado NO está vacío.

        // Insertar en la Base de Datos
        DbContactos dbContactos = new DbContactos(MainActivity.this);
        long id = dbContactos.insertarContacto(
                nombreStr,
                telefonoStr,
                notaStr,
                codigoPaisSeleccionado,
                fotoBase64 // Puede ser null si no se tomó foto
        );

        if (id > 0) {
            Toast.makeText(MainActivity.this, "Contacto Guardado (ID: " + id + ")", Toast.LENGTH_LONG).show();

            // Navegar a ActivitySegunda
            Intent intent = new Intent(MainActivity.this, ActivitySegunda.class);
            startActivity(intent);

            // Limpiar campos
            nombre.setText("");
            telefono.setText("");
            nota.setText("");
            imageView.setImageDrawable(null);
            fotoBase64 = null;
            codigoPaisSeleccionado = ""; // Limpiar también el código de país seleccionado
        }
        else if (id == -1) { // Si id es -1, hubo un error de inserción
        Toast.makeText(MainActivity.this, "ERROR: La inserción falló (ID: -1). Revise el logcat y restricciones de la BD.", Toast.LENGTH_LONG).show();
    } else {
        // Cualquier otro valor (aunque improbable si no es una excepción)
        Toast.makeText(MainActivity.this, "Error al guardar el contacto (ID: " + id + ")", Toast.LENGTH_LONG).show();
    }

    }
}