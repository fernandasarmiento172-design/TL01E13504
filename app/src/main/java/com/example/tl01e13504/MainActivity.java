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
import android.widget.Spinner;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        buscarpais = findViewById(R.id.buscarPais);
        listaPaises = findViewById(R.id.listapaises);
        nombre = (EditText) findViewById(R.id.nombre);
        telefono = (EditText) findViewById(R.id.telefono);
        nota = (EditText) findViewById(R.id.nota);
        imageView = (ImageView) findViewById(R.id.imageView);

        btnfoto = (Button) findViewById(R.id.btnfoto);
        btnsalvarcontacto = (Button) findViewById(R.id.btnsalvarcontacto);
        btncontactossalvados = (Button) findViewById(R.id.btncontactossalvados);


        btnfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {Permisos();}
        }) ;
        tomarFotoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        if (fotoFile != null && fotoFile.exists()) {
                            try {
                                // Cargar el bitmap desde el archivo
                                Bitmap foto = BitmapFactory.decodeFile(fotoFile.getAbsolutePath());

                                // Leer orientación EXIF
                                ExifInterface exif = new ExifInterface(fotoFile.getAbsolutePath());
                                int orientation = exif.getAttributeInt(
                                        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                                int rotationInDegrees = exifToDegrees(orientation);

                                // Rotar bitmap si es necesario
                                Bitmap rotatedBitmap = foto;
                                if (rotationInDegrees != 0) {
                                    Matrix matrix = new Matrix();
                                    matrix.preRotate(rotationInDegrees);
                                    rotatedBitmap = Bitmap.createBitmap(foto, 0, 0,
                                            foto.getWidth(), foto.getHeight(), matrix, true);
                                }

                                // Mostrar en ImageView
                                imageView.setImageBitmap(rotatedBitmap);

                                // Convertir a Base64
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
    private String bitmapToBase64(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private int exifToDegrees(int exifOrientation) {
        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_ROTATE_90: return 90;
            case ExifInterface.ORIENTATION_ROTATE_180: return 180;
            case ExifInterface.ORIENTATION_ROTATE_270: return 270;
            default: return 0;
        }
    }


    private void Permisos() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{ Manifest.permission.CAMERA}, PERMISO_CAMARA);
        }
        else
        {
            OpenCamara();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, int deviceId) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId);
        if(requestCode == PERMISO_CAMARA)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                OpenCamara();
            }
            else
            {
                Toast.makeText(this, "Permiso de Camara denegado "  , Toast.LENGTH_LONG).show();
            }
        }
    }

    private void OpenCamara(){
        try {
            // Crear archivo temporal
            fotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "foto_" + System.currentTimeMillis() + ".jpg");
            Uri fotoUri = FileProvider.getUriForFile(this,
                    "com.example.ucenm3p.provider", fotoFile);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
            tomarFotoLauncher.launch(intent);

        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, "Error al abrir cámara: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }




        // Configuramos el adaptador del ListView
        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, paises);
        listaPaises.setAdapter(adaptador);

        // Filtro de búsqueda (cuando escribes, se filtra la lista)
        buscarpais.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adaptador.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Cuando seleccionas un país
        listaPaises.setOnItemClickListener((parent, view, position, id) -> {
            String paisSeleccionado = (String) parent.getItemAtPosition(position);
            Toast.makeText(MainActivity.this, "Seleccionado: " + paisSeleccionado, Toast.LENGTH_SHORT).show();


        });

        // Define un listener para cuando se selecciona una opción
        listaPaises.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Obtiene la opción seleccionada
                String opcionSeleccionada = (String)
                        parent.getItemAtPosition(position);
                // Muestra un mensaje con la opción seleccionada
                Toast.makeText(MainActivity.this, "Seleccionado: " + opcionSeleccionada, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se ha seleccionado nada
            }
        });

        btnsalvarcontacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarCampos();
            }
        });

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

        if (mensajeError.length() > 0) {
            // Muestra la alerta con el mensaje específico
            mostrarAlerta(mensajeError.toString());
        } else {
            // Guarda el contacto
            guardarContacto();
        }
    }


    private void mostrarAlerta(String mensaje) {
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
        // Aquí va el código para guardar el contacto
        // Por ejemplo:
        String nombreStr = nombre.getText().toString().trim();
        String telefonoStr = telefono.getText().toString().trim();
        String notaStr = nota.getText().toString().trim();

        // Muestra un mensaje de éxito
        Toast.makeText(this, "Contacto guardado con éxito", Toast.LENGTH_SHORT).show();
    }
}
