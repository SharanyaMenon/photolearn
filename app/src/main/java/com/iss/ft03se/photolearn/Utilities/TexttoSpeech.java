package com.iss.ft03se.photolearn.Utilities;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Button;

import java.util.Locale;

/**
 * Created by toshimishra on 20/03/18.
 */

public class TexttoSpeech implements TextToSpeech.OnInitListener {
    private TextToSpeech textToSpeech;
    private String text;
    private Button button;

    public TexttoSpeech(Context context, String text, Button button) {
        textToSpeech = new TextToSpeech(context, this);
        this.text = text;
        this.button = button;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = textToSpeech.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                button.setEnabled(true);
            }

        } else {
            Log.e("TexttoSpeech", "Initialization Failed!");
        }
    }
}
