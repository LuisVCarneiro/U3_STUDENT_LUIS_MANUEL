package com.luis.u3_student_luis_manuel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etEntrada;
    private CheckBox checkBox;
    private Button btAddClear;
    private TextView tvSalida;
    private RadioGroup rgRadio;
    private RadioButton rbRed;
    private RadioButton rbBlue;
    private Spinner spProvincias;
    private Chronometer chCrono;
    private Switch swChrono;
    private int temporizador;
    private ImageView ivImagen;
    /*
    Este objeto TextView es a mayores de lo especificado en la tarea pero como no me funciona el
    Toast, me sierve para comprobar que el código funciona.
     */
    private TextView tvPrueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpView();

        /*
        Aqui compruebo cual es la horientación de la pantalla para inicializar tanto el ImageView
        como el método que lanza el toast al hacer click en ella
         */
        if (getRotation() == 0){
            ivImagen = findViewById(R.id.ivImagen);
            infoImagen(ivImagen);
        }

        /*
        El método comprobarcheckBox chequea que el checkBox clear está o no activado.
         */
        comprobarcheckBox(checkBox);

        /*
        gestionarEventos maneja los métodos del cronómetro
         */
        gestionarEventos();

        /*
        Método que recoje el item del spinner provincias que está seleccionado para lanzar el
        toast de si es provincia gallega o no
         */
        spProvincias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spProvincias.getSelectedItemId() < 4){
                    Toast.makeText(getApplicationContext(), getString(R.string.text_toast_gal),Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), getString(R.string.text_toast_no_gal),Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setUpView (){
        etEntrada = findViewById(R.id.etEntrada);
        checkBox = findViewById(R.id.checkBox);
        btAddClear = findViewById(R.id.btAddClear);
        tvSalida = findViewById(R.id.tvSalida);
        rgRadio = findViewById(R.id.rgRadio);
        rbRed = findViewById(R.id.rbRed);
        rbBlue = findViewById(R.id.rbBlue);
        spProvincias = findViewById(R.id.spProvincias);
        chCrono = findViewById(R.id.chCrono);
        swChrono = findViewById(R.id.swChrono);
        tvPrueba = findViewById(R.id.tvSpinner);

        /*
        Método que introduce el texto escrito en la etiqueta verde (etEntrada) dentro de la etiqueta roja (tvSalida).
        Este método funcionaría la primera vez que se inicializa la app, ya que asi el texto de muestra se elimina y
        no se concatena con lo nuevo.
         */

        btAddClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Recojo en una variable String el texto escrito en el TextView y lo concateno con lo que
                se ha introducido en el EditText junto con un espacio para ir sumando cadenas de texto
                 */
                String entrada = tvSalida.getText()+ " " + String.valueOf(etEntrada.getText());
                /*
                Aquí borro lo que ponía en el EditText,
                e introduzco la cadena en el TextView.
                 */
                etEntrada.setText("");
                tvSalida.setText(entrada);

            }
        });
    }

    /*
    Este método comprueba si  el CheckBox está seleccionado para darle al botón la acción de añadir
    el texto al TextView o borrar lo que haya escrito en él.
     */
    public void comprobarcheckBox(View view){
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    btAddClear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String entrada = tvSalida.getText()+ " " + String.valueOf(etEntrada.getText());
                            tvSalida.setText(entrada);
                        }
                    });
                }else {
                    btAddClear.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            tvSalida.setText("");
                        }
                    });
                }
            }
        });
    }

    /*
    Método que chequea el RadioGroup con los RadioButton rojo y azul para cambiar el color del
    texto del TextView.
    En función de si el id del RadioButton se corresponde con el del RadioButton azul o rojo, así
    establecerá el color del texto.
     */
    public void comprobarRadioButton(View view) {

        switch (view.getId()) {
            case R.id.rbBlue:
                tvSalida.setTextColor(getResources().getColor(R.color.blue));
                break;
            case R.id.rbRed:
                tvSalida.setTextColor(getResources().getColor(R.color.red));
                break;
        }
    }

    /*
    Aquí gestionamos los eventos del cronómetro
     */
    public void gestionarEventos(){

        chCrono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                /*
                Recojo el tiempo transcurrido que es el real menos el base
                 */
                long tiempoPasado = SystemClock.elapsedRealtime() - chronometer.getBase();
                /*
                Lo casteo a int y divido entre 1000 para obtener segundos puesto que el método devuelve
                milisegundos, obteniendo el tiempo transcurrido.
                 */
                int tiempoSeg = (int) tiempoPasado /1000;
                /*
                Si el tiempo transcurrido es igual al programado en el temporizador, finaliza la
                ejecución de la Actividad.
                 */
                if (tiempoSeg == temporizador)
                    finish();
            }
        });
        /*
        Manejo del Switch del cronómetro. Si está en modo off, inicia el cronómetro.
        Si está en modo on, para el cronómetro. Cuando empieza de nuevo lo hace en 0,
         que setBase establece el mismo valor de inicio que tenía al empezar la primera vez.
         */
        swChrono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swChrono.isChecked()){
                    temporizador = 15;
                    chCrono.setBase(SystemClock.elapsedRealtime());
                    chCrono.start();
                }else{
                    chCrono.stop();
                }
            }
        });
    }

    /*
    Método que mediante getRotation de Display, devuelve un int con valor 0 (vertical) o 1 (horizontal)
     */
    public int getRotation(){
        Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int orientacion = display.getRotation();
        return orientacion;
    }

    /*
    Método que mediante onClick, lanza el toast con la info de la imagen
     */
    public void infoImagen(View view){
        ivImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = (String)ivImagen.getTag();
                tvPrueba.setText(texto);
                Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_LONG).show();
            }
        });

    }

}