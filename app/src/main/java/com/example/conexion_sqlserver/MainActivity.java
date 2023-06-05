package com.example.conexion_sqlserver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText edC, edP, edPr, edF;
    RequestQueue requestqueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edC = (EditText)findViewById(R.id.edtCodigo);
        edP = (EditText)findViewById(R.id.edtNombre);
        edPr = (EditText)findViewById(R.id.edtPrecio);
        edF = (EditText)findViewById(R.id.edtFabricante);
    }

    public void Agregar(View v){
        ejecutarServicio("http://192.168.1.171:80/developeru/insertar_producto.php");
    }

    //boton para buscar Producto
    public void BuscarProductobtn(View v){
        BuscarProducto("http://192.168.1.171:80/developeru/buscar_producto.php?codigo="+edC.getText()+"");
    }

    private void ejecutarServicio(String URL){

        //hacemos los request
        StringRequest stringrequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "OPERACION EXITOSA", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            //funcion par aobtener los parametros
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("codigo", edC.getText().toString());
                parameters.put("producto", edP.getText().toString());
                parameters.put("precio", edPr.getText().toString());
                parameters.put("fabricante", edF.getText().toString());
                return parameters;
            }
        };

        //procesar peticiones hechas por la app
        requestqueue = Volley.newRequestQueue(this);
        //mandamos el string reques de la solicitud
        requestqueue.add(stringrequest);
    }

    public void BuscarProducto(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                //recorrer datos del webservices
                for (int i = 0; i < response.length(); i++) {
                    //try catch apra recibir de forma correcta el dato
                    try {
                        //obtenmos el objeto del request
                        jsonObject = response.getJSONObject(i);
                        //mostramos los datos
                        edP.setText(jsonObject.getString("producto"));
                        edPr.setText(jsonObject.getString("precio"));
                        edF.setText(jsonObject.getString("fabricante"));

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //error mostrado en caso la conexion falle
                Toast.makeText(getApplicationContext(), "ERROR DE CONEXION", Toast.LENGTH_LONG).show();
            }
        });

        requestqueue =Volley.newRequestQueue(this);
        requestqueue.add(jsonArrayRequest);
    }


    public void EditrarProducto(View v){
        ejecutarServicio("http://192.168.1.171:80/developeru/editar_producto.php");
    }

    public void EliminarProducto(View v){
        eliminarServicio("http://192.168.1.171:80/developeru/eliminar_producto.php");
    }



    private void eliminarServicio(String URL){

        //hacemos los request
        StringRequest stringrequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "EL PRODUCTO FUE ELIMINADO", Toast.LENGTH_LONG).show();
                limpiarFom();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            //funcion par aobtener los parametros
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("codigo", edC.getText().toString());
                return parameters;
            }
        };

        //procesar peticiones hechas por la app
        requestqueue = Volley.newRequestQueue(this);
        //mandamos el string reques de la solicitud
        requestqueue.add(stringrequest);
    }


    private void limpiarFom(){
        edC.setText("");
        edP.setText("");
        edF.setText("");
        edPr.setText("");
    }
}