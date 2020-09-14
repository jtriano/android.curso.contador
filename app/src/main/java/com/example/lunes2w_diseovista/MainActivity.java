package com.example.lunes2w_diseovista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private int counter;
    private CheckBox negative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establecemos nuestra interfaz
        setContentView(R.layout.activity_main);
        // Inicializamos el contador y lo mostramos
        counter = 0;
        showCounter();
        // Establecemos los escuchadores de eventos
        this.negative = findViewById(R.id.cB_negative);
        negative.setOnClickListener(this);
        Button reset = findViewById(R.id.button_reset);
        reset.setOnClickListener(this);
        Button incr = findViewById(R.id.button_increment);
        incr.setOnClickListener(this);
        Button decr = findViewById(R.id.button_decrement);
        decr.setOnClickListener(this);
        EventoTeclado eTeclado = new EventoTeclado();
        EditText value = findViewById(R.id.default_counter);
        value.setOnEditorActionListener(eTeclado);
    }
/*  Comentamos estos métodos para probar como guardar los datos persistentemente.

    // Se ejecuta antes de detener la actividad
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("counterValue", counter);
        super.onSaveInstanceState(outState);
    }

    //  Se ejecuta tras onCreate() solo si hay un estado guardado para restablecer
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        counter = savedInstanceState.getInt("counterValue");
        showCounter();
    }
*/

    @Override
    protected void onPause() {
        super.onPause();
        // Usaremos la clase SharedPreferences para guardar los datos como preferencias
        SharedPreferences data = (SharedPreferences) PreferenceManager.getDefaultSharedPreferences(this);
        // Usamos la subclase Editor para hacer modificaciones
        SharedPreferences.Editor myEdit = data.edit();
        // Hacemos la modificacion
        myEdit.putInt("counterValue", counter);
        // Aplicamos los cambios
        myEdit.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences data = (SharedPreferences) PreferenceManager.getDefaultSharedPreferences(this);
        counter = data.getInt("counterValue", 0);
        showCounter();
    }

    public void increment(View view) {
        counter++;
        showCounter();
    }

    public void decrement(View view) {
        counter--;
        if (!negative.isChecked() && counter<0){
                counter=0;
        }
        showCounter();
    }

    public void reset(View vista) {
        EditText defaultCounter = findViewById(R.id.default_counter);
        String counterDefaultString = defaultCounter.getText().toString();
        if (counterDefaultString.isEmpty()) {
            counterDefaultString = "0";
        }
        counter = Integer.parseInt(counterDefaultString);
        showCounter();
    }

    public void showCounter() {
        TextView status = findViewById(R.id.result);
        status.setText(String.valueOf(counter));
    }

    public void hideKeyboard(View view){
        InputMethodManager keyboard = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        if (keyboard.isActive()){
            keyboard.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),0);
        }
    }

    @Override
    public void onClick(View view) {
        hideKeyboard(view);
        switch (view.getId()){
            case R.id.cB_negative:
                if (!negative.isChecked() && counter<0){
                    counter=0;
                }
                showCounter();
                break;
            case R.id.button_reset:
                reset(view);
                break;
            case R.id.button_increment:
                increment(view);
                break;
            case R.id.button_decrement:
                decrement(view);
                break;
        }
    }

    public class EventoTeclado implements TextView.OnEditorActionListener {

        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i== EditorInfo.IME_ACTION_DONE){
                reset(null);
            }
            return false;
        }
    }
}

