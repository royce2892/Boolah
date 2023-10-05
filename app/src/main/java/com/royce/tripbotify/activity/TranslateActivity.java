package com.royce.tripbotify.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.royce.tripbotify.R;
import com.royce.tripbotify.core.ApiClient;
import com.royce.tripbotify.database.RealmTranslation;

import io.realm.Realm;

public class TranslateActivity extends AppCompatActivity {

    private String languageCode = "de";
    private EditText mInputText;
    private boolean isAPICalled = false;
    private TextView mLabel, mOutput;
    private Realm realm;
    private String savedText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
        setContentView(R.layout.activity_translate);
        languageCode = getIntent().getStringExtra("languageCode");
        mInputText = findViewById(R.id.et_text);
        mLabel = findViewById(R.id.label);
        mOutput = findViewById(R.id.output);
        realm = Realm.getDefaultInstance();

        findViewById(R.id.button_translate_save).setOnClickListener(v -> saveTranslation());

        findViewById(R.id.button_translate).setOnClickListener(v -> {
            if (TextUtils.isEmpty(mInputText.getText().toString()))
                Toast.makeText(TranslateActivity.this, "Please enter a valid text to be translated", Toast.LENGTH_SHORT).show();
            else
                translate();
        });
    }

    private void saveTranslation() {
        if (TextUtils.isEmpty(mOutput.getText().toString())) {
            Toast.makeText(TranslateActivity.this, "No translation found to be saved", Toast.LENGTH_SHORT).show();
            return;
        }
        if (savedText.contentEquals(mInputText.getText().toString())) {
            Toast.makeText(TranslateActivity.this, "This translation is already saved locally", Toast.LENGTH_SHORT).show();
            return;
        }
        realm.beginTransaction();
        RealmTranslation translation = realm.createObject(RealmTranslation.class);
        translation.setInput(mInputText.getText().toString());
        translation.setOutput(mOutput.getText().toString());
        translation.setLanguageCode(languageCode);
        savedText = mInputText.getText().toString();
        realm.commitTransaction();
        Toast.makeText(TranslateActivity.this, "Translation saved locally", Toast.LENGTH_SHORT).show();

    }


    private void translate() {

        if (isAPICalled)
            return;
        Toast.makeText(TranslateActivity.this, "Please wait while Boolah translates for you", Toast.LENGTH_SHORT).show();
        isAPICalled = true;
        mLabel.setVisibility(View.GONE);
        mOutput.setText("");
        AsyncTask.execute(() -> {
            setUI(ApiClient.getYandexTranslatedText(mInputText.getText().toString(), languageCode));
            isAPICalled = false;
        });
    }

    private void setUI(String result) {
        runOnUiThread(() -> {
            mOutput.setText(result);
            mLabel.setVisibility(View.VISIBLE);
            if (result.contentEquals(""))
                Toast.makeText(TranslateActivity.this, "Some error occurred, please try again later", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void initActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Learn");
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

}
