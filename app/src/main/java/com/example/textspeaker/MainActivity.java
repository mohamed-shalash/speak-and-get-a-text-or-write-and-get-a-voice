package com.example.textspeaker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText tv;
    Button talk,clear,mick_btn;
    TextToSpeech textToSpeech;
    TextView tv_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv =findViewById(R.id.editText_inputtext);
        talk =findViewById(R.id.button_talk);
        clear =findViewById(R.id.button_clear);

        textToSpeech =new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS){
                    int lang =textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s =tv.getText().toString();
                int speech =textToSpeech.speak(s,TextToSpeech.QUEUE_FLUSH,null);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setText("");
            }
        });

        mick_btn =findViewById(R.id.mick_btn);
        tv_text =findViewById(R.id.text_here);

        ActivityResultLauncher<Intent> mStartForResult =  registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        ArrayList<String> results = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        tv_text.setText(results.get(0));
                    }
                }
        );


        mick_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,Locale.ENGLISH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");//Locale.getDefault()
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"hi speak");

                try {
                    mStartForResult.launch(intent);
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}