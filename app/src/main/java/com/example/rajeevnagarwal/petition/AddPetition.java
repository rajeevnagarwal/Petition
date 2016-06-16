package com.example.rajeevnagarwal.petition;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class AddPetition extends AppCompatActivity {

    ProgressDialog pDialog;
    Button btn;
    EditText editText1, editText2, editText3;
    private static String url_create_product = "http://192.168.225.70:80/petitions/create_petition.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_petition);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btn = (Button) findViewById(R.id.btn);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new CreateNewPetition(getApplicationContext()).execute(editText1.getText().toString(), editText2.getText().toString(), editText3.getText().toString());

            }

        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    class CreateNewPetition extends AsyncTask<String, String, String> {

        private Context context;

        public CreateNewPetition(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(AddPetition.this);
            pDialog.setMessage("Creating Petition..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            String heading = args[0];
            String description = args[1];
            String sign = args[2];
            String data;
            BufferedReader bufferedReader;
            String result;
            try {
                data = "?heading=" + URLEncoder.encode(heading
                        , "UTF-8");
                data += "&description=" + URLEncoder.encode(description, "UTF-8");
                data += "&sign=" + URLEncoder.encode(sign, "UTF-8");


                String link = url_create_product + data;
                URL url = new URL(link);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                result = bufferedReader.readLine();
                System.out.println(result);
                return result;

            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            String jsonStr = result;
            System.out.println(jsonStr);
           if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String query_result = jsonObj.getString("success");
                    System.out.println(query_result);
                    if(query_result.equals("1"))
                        Toast.makeText(context, "Data inserted successfully.", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context,"Data insertion failure.",Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error parsing JSON data.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Couldn't get any JSON data.", Toast.LENGTH_SHORT).show();
            }
        }

    }
        }



