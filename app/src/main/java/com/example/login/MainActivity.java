package com.example.login;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    // Creamos las diferentes variables

    EditText editNombre_ADC, editMail_ADC, editContrasena_ADC;
    Button btnLogin_ADC;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        editNombre_ADC = findViewById(R.id.editNombre_ADC);
        editMail_ADC = findViewById(R.id.editMail_ADC);
        editContrasena_ADC = findViewById(R.id.editContrasena_ADC);
        btnLogin_ADC = findViewById(R.id.btnLogin_ADC);
        dbHelper = new DatabaseHelper(this);

        //Acción del botón
        btnLogin_ADC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUsuario();
            }
        });

    }

    private void loginUsuario() {

        //Guardamos las diferentes variables para mayor comodidad
        final String nombre = editNombre_ADC.getText().toString().trim();
        final String mail = editMail_ADC.getText().toString().trim();
        final String contrasena = editContrasena_ADC.getText().toString().trim();

        //Realizamos las comprobaciones y restrincciones

        if (nombre.isEmpty() || mail.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!nombre.matches("[a-zA-Z0-9]+")) {
            Toast.makeText(this, "El nombre solo puede contener letras y números", Toast.LENGTH_SHORT).show();
            return;
        }

        if (contrasena.length() < 4 || contrasena.length() > 8) {
            Toast.makeText(this, "La contraseña debe tener entre 4 y 8 carácteres", Toast.LENGTH_SHORT).show();
            return;
        }

        //Conexión con la base de datos y validación de credenciales

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL("http://192.168.1.43/Login/validacuenta.php"); //Cambiamos a IP local
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json; utf-8");
                    connection.setDoOutput(true);

                    //Creamos el JSON
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("Nombre", nombre);
                    jsonBody.put("Mail", mail);
                    jsonBody.put("Contrasena", contrasena);

                    //Enviar petición
                    try (OutputStream os = connection.getOutputStream()) {
                        byte[] input = jsonBody.toString().getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }
                    //Leer respuesta
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return response.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    try {
                        // Procesamos la respuesta del servidor
                        JSONObject jsonResponse = new JSONObject(result);
                        String status = jsonResponse.getString("status");

                        if (status.equals("success")) {
                            Toast.makeText(MainActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();
                            // Si el login es efectivo, redirigir a ListActivity
                            Intent intent = new Intent(MainActivity.this, ListActivity.class);
                            startActivity(intent);
                        } else if (status.equals("exists")) {
                            Toast.makeText(MainActivity.this, "El usuario ya existe", Toast.LENGTH_SHORT).show();
                        }else {
                            // Si el login no es efectivo, manejar el error e insertar en SQLite
                            manejarErrorDeLogin(nombre, contrasena);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    // Método para manejar el error de login
    private void manejarErrorDeLogin(String nombre, String contrasena) {
        String fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        dbHelper.insertarIntentoFallido(nombre, contrasena, fechaHora);

        // Iniciar la actividad de Errores para mostrar los intentos fallidos
        Intent intent = new Intent(MainActivity.this, ErroresActivity.class);
        startActivity(intent);

        Toast.makeText(MainActivity.this, "Error en los datos de login", Toast.LENGTH_SHORT).show();
    }
}

