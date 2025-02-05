package com.example.login;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;


public class ErroresActivity extends AppCompatActivity {

    //Establecemos las variables
    ListView listViewIntentos_ADC;
    ArrayList<String> listaErrores;
    Button btnVolver_ADC;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
       setContentView(R.layout.activity_errores);

       listViewIntentos_ADC = findViewById(R.id.listViewIntentos_ADC);
       btnVolver_ADC = findViewById(R.id.btnVolver_ADC);
       listaErrores = new ArrayList<>();
        dbHelper = new DatabaseHelper(this);

       //Obtiene los errores de la base de datos
        obtenerErrores();

        //Configuramos el boton para volver a la actividad inicial
        btnVolver_ADC.setOnClickListener(v -> {
            Intent intent = new Intent(ErroresActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
    //Creamos el metodo obtenerErrores
    private void obtenerErrores() {
        Cursor cursor = dbHelper.obtenerIntentosFallidos();

        // Recorremos el cursor y agregamos los errores a la lista
        if (cursor != null && cursor.moveToFirst()) {
            int nombreIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NOMBRE);
            int fechaIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_FECHA);

            if (nombreIndex != -1 && fechaIndex != -1) {
                do {
                    String nombre = cursor.getString(nombreIndex);
                    String fecha = cursor.getString(fechaIndex);

                    // Formateamos el error en una cadena legible
                    String error = "Cuenta: " + nombre + " - Fecha: " + fecha;
                    listaErrores.add(error);
                } while (cursor.moveToNext());
            } else {
                Log.e("ErroresActivity", "Una o más columnas no se encontraron en el cursor");
            }

            // Cerramos el cursor
            cursor.close();

            // Configuramos el adaptador para mostrar los errores en el ListView
            ArrayAdapter<String> adapter = new ArrayAdapter<>(ErroresActivity.this, android.R.layout.simple_list_item_1, listaErrores);
            listViewIntentos_ADC.setAdapter(adapter);
        } else {
            // Si no hay registros en la base de datos
            Toast.makeText(ErroresActivity.this, "No se encontraron intentos fallidos", Toast.LENGTH_SHORT).show();
        }
    }
}
