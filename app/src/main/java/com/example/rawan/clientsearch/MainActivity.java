package com.example.rawan.clientsearch;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText title , fromyear , toyear;
    private Button searchbutton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = (EditText) findViewById(R.id.title_edittext);
        fromyear = (EditText) findViewById(R.id.fromyear_editText);
        toyear = (EditText) findViewById(R.id.toYear_editText);
        searchbutton = (Button) findViewById(R.id.searchbtn);
        searchbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //perform action on click

                String titlestr= title.getText().toString();
                String fromyearstr= fromyear.getText().toString();
                String toyearstr= toyear.getText().toString();
                String total[] = new String[4];
                total[0]=titlestr;
                total[1]=fromyearstr;
                total[2]=toyearstr;
                total[3]= "123456";
                Search searchtask = new Search(total);
                searchtask.execute();



            }
        });

    }

    public class Search extends AsyncTask<String[], Void, JSONObject> {


        //    private String _ssfor;
        //    private String _sby;
        //     private String _stxt;
        private String _title;
        private String _userID;
        private String _toyear;
        private String _fromyear;
        //TODO:   private  ArrayList<Item> _Results ;

        public Search(String[] details) {
            _title = details[0];
            _fromyear = details[1];
            _toyear = details[2];
            _userID = details[3];
        }

        @Override
        protected JSONObject doInBackground(String[]... strings) {
            String _server_msg = null;
            HttpURLConnection urlConnection = null;
            String serverJsonStr = null;
            BufferedReader reader = null;


            try {
                final String _SEARCH_URL =
                        "http://ec2-52-43-108-148.us-west-2.compute.amazonaws.com:8080/useraccount/search/dosearchbytitle?";
                Log.v("doinbackground", "Here 1");


                Uri builtUri = Uri.parse(_SEARCH_URL).buildUpon()
                        .appendQueryParameter("userId", _userID)
                        .appendQueryParameter("title", _title)
                        .appendQueryParameter("fromyear", _fromyear)
                        .appendQueryParameter("toyear", _toyear)
                        .build();
                URL url = new URL(builtUri.toString());
                Log.v("URL", builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                serverJsonStr = buffer.toString();
                Log.d("PROBLEM", serverJsonStr);

            } catch (IOException e) {
                Log.e("LOGE", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("LOGE", "Error closing stream", e);
                    }
                }


                return null;


            }
        }
    }
}


