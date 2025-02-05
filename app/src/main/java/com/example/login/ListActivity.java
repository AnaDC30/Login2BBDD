package com.example.login;


import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ListView listView_ADC;
    ArrayList<String> listaUsuarios;
    Button btnVolver1_ADC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView_ADC = findViewById(R.id.listView_ADC);
        listaUsuarios = new ArrayList<>();
        btnVolver1_ADC = findViewById(R.id.btnVolver1_ADC);

        // Configuramos el evento onClick para el botón de volver
        btnVolver1_ADC.setOnClickListener(v -> {
            // Cuando el botón es presionado, se vuelve a la actividad anterior
            finish(); // Cierra la actividad actual y regresa a la anterior
        });

        obtenerUsuarios();
    }
private void obtenerUsuarios(){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                //Establecemos el Buffer en null para poder cerrarlo correctamente
                BufferedReader br = null;
                try {
                    URL url = new URL("http://192.168.1.43/Login/consultausuarios.php"); //Cambiamos a IP local
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Accept", "application/json");

                    //Leer respuesta
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return response.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {
                    //Cerramos el Buffer para evitar fugas
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    try {
                        //Analizamos el objeto JSON
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("status");

                        if (status.equals("success")) {
                            //Accedemos a la array de usuarios
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonUser = jsonArray.getJSONObject(i);
                                String usuario = jsonUser.getString("nombre") + "-" + jsonUser.getString("mail");
                                listaUsuarios.add(usuario);
                            }
                                //Configuración del adaptador para la lisview
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(ListActivity.this, android.R.layout.simple_list_item_1, listaUsuarios);
                            listView_ADC.setAdapter(adapter);
                        } else {
                            // Manejamos errores si falla la validación
                            String message = jsonObject.getString("message");
                            Toast.makeText(ListActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ListActivity.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ListActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
        }


}

