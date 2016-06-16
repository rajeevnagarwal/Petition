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
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class Show_Petition extends AppCompatActivity {

    TextView txt1, txt2;
    private static String url_fetch_description = "http://192.168.225.70:80/petitions/fetch_description.php";
    ProgressDialog pDialog;
    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__petition);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btn1 = (Button)findViewById(R.id.btn3);
        final String id = getIntent().getStringExtra("id").toString();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Sign.class);
                i.putExtra("id",id);
                startActivity(i);

            }

        });
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener()

                                   {
                                       @Override
                                       public void onClick(View view) {
                                           Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                                   .setAction("Action", null).show();
                                       }
                                   }

            );
            txt1=(TextView)

            findViewById(R.id.txt1);

            txt2=(TextView)

            findViewById(R.id.txt2);


            System.out.println(id);
            new

            LoadDescription(getApplicationContext()

            ).

            execute(id);

        }

        class LoadDescription extends AsyncTask<String, String, String> {

        private Context context;

        public LoadDescription(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Show_Petition.this);
            pDialog.setMessage("Loading the selected petition. Please wait..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        protected String doInBackground(String... args) {
            String id1 = args[0];
            System.out.println(id1);
            String result = "";


            try {
                String data = "?id=" + URLEncoder.encode(id1
                        , "UTF-8");
                String link = url_fetch_description + data;
                System.out.println(link);
                URL url = new URL(link);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader;
                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                result = bufferedReader.readLine();
               // System.out.println(result);
                return result;

            } catch (Exception e) {

            }
            return result;


        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            System.out.println(result);
            String jsonStr = result;
            if (jsonStr != null) {
                try {
                    final JSONObject jsonObj = new JSONObject(jsonStr);
                    final JSONArray jsonArray = jsonObj.getJSONArray("products");
                    System.out.println(jsonArray.getJSONObject(0).getString("description"));
                    JSONArray obj1 = jsonArray.getJSONObject(0).getJSONArray("signee");
                    String s="";
                    for(int i=0;i<obj1.length();i++)
                    {
                        s = s.concat(obj1.get(i).toString()+", ");
                        System.out.println(obj1.get(i).toString());
                    }
                    s = s.substring(0,s.length()-2);
                    final String s1 = s;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                txt1.setText(jsonArray.getJSONObject(0).getString("description"));
                                txt2.setText(s1);
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }


        }
    }
}
