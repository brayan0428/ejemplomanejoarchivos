package cua.manejoarchivos.com.ejercicio_manejo_archivos;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText nota;
    ListView listView;
    Button guardar;
    List<String> list;
    public static ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nota = findViewById(R.id.nota);
        listView = findViewById(R.id.listview);
        guardar = findViewById(R.id.guardar);

        String[] archivos = fileList();
        if (existe(archivos, "notas.txt")) {
            listarNotas();
        }
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput("notas.txt", Activity.MODE_APPEND));
                    archivo.append(nota.getText().toString() + "\n");
                    archivo.flush();
                    archivo.close();
                    Toast.makeText(getApplicationContext(),"Datos grabados exitosamente",Toast.LENGTH_SHORT).show();
                    nota.setText("");
                    listarNotas();
                }catch (IOException e){
                    Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean existe(String[] archivos, String archbusca) {
        for (int f = 0; f < archivos.length; f++)
            if (archbusca.equals(archivos[f]))
                return true;
        return false;
    }

    public void listarNotas(){
        list = new ArrayList<>();
        try {
            InputStreamReader archivo = new InputStreamReader(
                    openFileInput("notas.txt"));
            BufferedReader br = new BufferedReader(archivo);
            String linea = br.readLine();
            while (linea != null) {
                list.add(linea.toString().trim());
                linea = br.readLine();
            }
            br.close();
            archivo.close();
        } catch (IOException e) {
        }
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,list){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView tv = view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.BLACK);
                return view;
            }
        };
        arrayAdapter.notifyDataSetChanged();
        listView.setAdapter(arrayAdapter);
    }
}
