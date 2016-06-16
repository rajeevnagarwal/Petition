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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Sign extends AppCompatActivity {

   Button button1;
    EditText txt5;
    ProgressDialog pDialog;
    private static String url_insert_sign = "http://192.168.225.70:80/petitions/insert_sign.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final String id = getIntent().getExtras().getString("id");
        txt5 =  (EditText)findViewById(R.id.txt5);
        button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = txt5.getText().toString();
                new CreateNewSign(getApplicationContext()).execute(id,name);


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
    class CreateNewSign extends AsyncTask<String, String, String> {

        private Context context;

        public CreateNewSign(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Sign.this);
            pDialog.setMessage("Signing this Petition..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            String id = args[0];
            String name = args[1];
            BufferedReader bufferedReader;
            String result;
            String data;
            try {
                data = "?id=" + URLEncoder.encode(id
                        , "UTF-8");
                data += "&name=" + URLEncoder.encode(name, "UTF-8");
                String link = url_insert_sign + data;
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
