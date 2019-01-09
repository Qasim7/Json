package com.example.qasim.json;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.qasim.json.model.Lyrics;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    ProgressBar progressBar;
    EditText editText1;
    EditText editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.tv);
        progressBar=findViewById(R.id.prbar);
        editText1=findViewById(R.id.edittext1);
        editText2=findViewById(R.id.edittext2);
        Button button=findViewById(R.id.btn1);
        Button buttonclear=findViewById(R.id.btn2);
        textView.setMovementMethod(new ScrollingMovementMethod());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLyrics();
            }
        });

        buttonclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText1.getText().clear();
                editText2.getText().clear();
                textView.setText("");
            }
        });
    }




    private void getLyrics() {
        String author=editText1.getText().toString();
        String name=editText2.getText().toString();
        AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
        asyncHttpClient.get("https://api.lyrics.ovh/v1/"+author+"/"+name, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response=new String(responseBody);
                Lyrics lyrics=parseResponse(response);
                updateUI(lyrics);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void updateUI(Lyrics lyrics) {
        textView.setText(lyrics.getLyrics());
    }

    private Lyrics parseResponse(String response) {
        try {
            JSONObject jsonObject=new JSONObject(response);
            String lyrics=jsonObject.getString("lyrics");
            Lyrics l=new Lyrics(lyrics);
            return l;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
