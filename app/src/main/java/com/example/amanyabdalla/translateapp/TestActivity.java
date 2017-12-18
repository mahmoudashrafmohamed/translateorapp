package com.example.amanyabdalla.translateapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TestActivity extends AppCompatActivity {

    EditText et;
    TextView txt;
    TryLanguage tr;
    ListView listView;
    Switch sw;
    String language;
    String langForAn;
    String transWordForHistory;
    String wordForHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        et = (EditText) findViewById(R.id.textEditor);

        txt = (TextView) findViewById(R.id.checkThread);

        tr = new TryLanguage();
        listView = (ListView)findViewById(R.id.listView);
        sw = (Switch)findViewById(R.id.switch1);
        sw.setChecked(false);
        language = "en-ar";
        langForAn="ar";
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    language = "ar-en";
                    langForAn = "en";
                }else {
                    language = "en-ar";
                    langForAn="ar";
                }

            }
        });



    }




    public void pushButt(View view) {
        Toast.makeText(getApplicationContext(),"please wait to load data...",Toast.LENGTH_SHORT).show();
        TryThread tr = new TryThread();

        tr.execute(et.getText().toString());
    }



    public class TryThread extends AsyncTask<String, Void, String> {
        ArrayAdapter<String> arrar;
        String s = et.getText().toString();
        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String a = tr.translatedWord(langForAn, params[0]);





           GetAllListArrays lll = new GetAllListArrays();
            try {
                lll.handParse(lll.translate123(language, s));
                Log.d("BUMP:", lll.getTranslatedWords.toString());

                String [] transl = new String[lll.getTranslatedWords.size()];
                lll.getTranslatedWords.toArray(transl);

                String [] transExtr = new String[lll.getTranslatedExp.size()];
                lll.getTranslatedExp.toArray(transExtr);

                String [] unTrans = new String[lll.getUnTranslatedExp.size()];
                lll.getUnTranslatedExp.toArray(unTrans);

                String [] correct = new String[transExtr.length];
                if(transExtr.length<transl.length) {
                    for (int i = 0; i < transExtr.length; i++) {
                        correct[i] = transl[i];
                    }
                    transl = correct;
                }
                String [] allTrans = new String[0];
                try {
                    lll.getAllTrans(lll.translate123(language, s));
                    allTrans = new String [lll.getTransForNonPrevious.size()];
                    lll.getTransForNonPrevious.toArray(allTrans);
                }catch (RuntimeException e) {

                }


                MyOwnArrayAdapter myOwnArrayAdapter = new MyOwnArrayAdapter(getBaseContext(),transl,transExtr,unTrans);
                AnotherAdapter anotherAdapter = new AnotherAdapter(getBaseContext(),allTrans);


                if(transl.length == 0&&allTrans.length!=0){
                    arrar = anotherAdapter;
                }else {
                    arrar = myOwnArrayAdapter;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return a;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            listView.setAdapter(arrar);
            txt.setText(s);
            wordForHistory = et.getText().toString();
            transWordForHistory = s;

        }
    }


}

