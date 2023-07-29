package com.example.trackkudushuttle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    static String username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

//    public void om(View v){
//        Intent i=new Intent(MainActivity.this, radar.class);
//        startActivity(i);
//
//    }

    public void doCreate(View v){
        Intent i=new Intent(MainActivity.this, Register.class);
        startActivity(i);
    }
















        public String getUsername(){
            return username;
        }









        public void doLogin(View v) throws ExecutionException, InterruptedException {
            EditText username = findViewById(R.id.editTextUsername);
            EditText password = findViewById(R.id.editTextPassword);
            String user_username = username.getText().toString().trim();
            String user_password = password.getText().toString().trim();
            JsonTask m= (JsonTask) new JsonTask().execute("https://lamp.ms.wits.ac.za/home/s2592255/logintrack.php?brand="+user_username);
            String web=m.get();
            m.onPostExecute(web);
            if(user_password.equals(this.password) && user_username.equals(this.username)){
                Intent i = new Intent(MainActivity.this, Homepage.class);
                startActivity(i);}
            else if(user_password.length()==0 || user_username.length()==0){
                Toast.makeText(this,"Leave no entry blank", Toast.LENGTH_SHORT).show();
            }
            else if(web.equals("[]\n")){
                Toast.makeText(this, "Account does not exist", Toast.LENGTH_SHORT).show();
            }
            else{ Toast.makeText(this, "Wrong Password. Try again.", Toast.LENGTH_SHORT).show(); }
        }

        public void processJSON(String json){
            try {
                JSONArray all = new JSONArray(json);
                for (int i=0; i<all.length(); i++){
                    JSONObject item=all.getJSONObject(i);
                    String name = item.getString("D_USERNAME");
                    String description = item.getString("P_WORD");
                    //System.out.println(name+" "+description);
                    this.password=description;
                    this.username=name;
                }
                //if(this.password !=null && this.password.equals(password))
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        private class JsonTask extends AsyncTask<String, String, String> {
            protected void onPreExecute() {
                super.onPreExecute();
//            pd = new ProgressDialog(Login.this);
//            pd.setMessage("Please wait");
//            pd.setCancelable(false);
//            pd.show();
            }

            protected String doInBackground(String... params) {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(params[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    InputStream stream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line+"\n");
                        Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
                    }
                    return buffer.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
//           R

                processJSON(result);
                //txtJson.setText(result);
            }
        }

    }















