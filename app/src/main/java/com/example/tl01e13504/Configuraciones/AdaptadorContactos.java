package com.example.tl01e13504.Configuraciones;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tl01e13504.R;

import java.util.List;

public class AdaptadorContactos extends ArrayAdapter<Contactos> {

    public AdaptadorContactos(Context context, List<Contactos> contactos) {
        super(context, 0, contactos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Contactos contacto = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_contacto, parent, false);
        }

        // 1. Inicializar todas las vistas
        TextView txtNombre = convertView.findViewById(R.id.txtNombre);
        TextView txtTelefono = convertView.findViewById(R.id.txtTelefono);
        TextView txtCodigoPais = convertView.findViewById(R.id.txtCodigoPais); // 👈 NUEVO
        TextView txtNota = convertView.findViewById(R.id.txtNota);             // 👈 NUEVO

        if (contacto != null) {
            // 2. Asignar todos los datos
            txtNombre.setText(contacto.getNombre());

            // Mostrar Código de País y Teléfono juntos
            txtCodigoPais.setText("(" + contacto.getCodigoPais() + ")");
            txtTelefono.setText(contacto.getTelefono());

            // Mostrar la Nota
            txtNota.setText("Nota: " + contacto.getNota());
        }

        return convertView;
    }
}

