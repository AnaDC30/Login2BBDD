package com.example.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "usuarios.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_INTENTOS = "intentos";
    private static final String COLUMN_ID = "id";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_CONTRASENA = "contrasena";
    public static final String COLUMN_FECHA = "fecha";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_INTENTOS_TABLE = "CREATE TABLE " + TABLE_INTENTOS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Asegúrate de que haya un espacio
                COLUMN_NOMBRE + " TEXT, " +
                COLUMN_CONTRASENA + " TEXT, " +
                COLUMN_FECHA + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

        db.execSQL(CREATE_INTENTOS_TABLE);
        Log.d("DatabaseHelper", "Tabla " + TABLE_INTENTOS + " creada con columnas: " +
                COLUMN_NOMBRE + ", " +
                COLUMN_CONTRASENA + ", " +
                COLUMN_FECHA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTENTOS);
        onCreate(db);
    }

    // Método para insertar un intento fallido en la base de datos
    public void insertarIntentoFallido(String nombre, String contrasena, String fechaHora) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_CONTRASENA, contrasena);
        values.put(COLUMN_FECHA, fechaHora);

        // Insertar los datos en la tabla
        long result = db.insert(TABLE_INTENTOS, null, values);

        if (result != -1) {
            Log.d("DatabaseHelper", "Intento fallido insertado correctamente");
        } else {
            Log.e("DatabaseHelper", "Error al insertar intento fallido.");
        }

        db.close();
    }

    // Método para obtener todos los intentos fallidos
    public Cursor obtenerIntentosFallidos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_INTENTOS, null, null, null, null, null, COLUMN_FECHA + " DESC"); // Ordenar por fecha descendente
    }

    // Método para eliminar todos los intentos fallidos si es necesario limpiar la tabla
    public void eliminarTodosLosIntentos() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INTENTOS, null, null); // Eliminar todos los registros
        db.close();
    }
}
