package com.royce.tripbotify.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.royce.tripbotify.R;
import com.royce.tripbotify.core.ApiClient;
import com.royce.tripbotify.database.RealmTranslation;
import com.royce.tripbotify.utils.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.Response;

public class TranslateActivity extends AppCompatActivity {

    private String languageCode = "de";
    private EditText mInputText;
    private boolean isAPICalled = false;
    private TextView mLabel,mOutput;
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
            if(TextUtils.isEmpty(mInputText.getText().toString()))
                Toast.makeText(TranslateActivity.this,"Please enter a valid text to be translated",Toast.LENGTH_SHORT).show();
            else
                translate();
        });
    }

    private void saveTranslation() {
        if(TextUtils.isEmpty(mOutput.getText().toString())) {
            Toast.makeText(TranslateActivity.this,"No translation found to be saved",Toast.LENGTH_SHORT).show();
            return;
        }
        if(savedText.contentEquals(mInputText.getText().toString())) {
            Toast.makeText(TranslateActivity.this,"This translation is already saved locally",Toast.LENGTH_SHORT).show();
            return;
        }
        realm.beginTransaction();
        RealmTranslation translation = realm.createObject(RealmTranslation.class);
        translation.setInput(mInputText.getText().toString());
        translation.setOutput(mOutput.getText().toString());
        translation.setLanguageCode(languageCode);
        savedText = mInputText.getText().toString();
        realm.commitTransaction();
    }


    private void translate() {

        if(isAPICalled)
            return;
        isAPICalled = true;
        mLabel.setVisibility(View.GONE);
        mOutput.setText("");
        AsyncTask.execute(() -> {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("text", mInputText.getText().toString());
                JSONArray array = new JSONArray();
                array.put(0,jsonObject);
                Response response = ApiClient.getTranslatedText(array.toString(), languageCode);
                JSONArray r = null;
                if (response.body() != null) {
                    r = new JSONArray(response.body().string());
                    setUI(r);
                }
                if (r != null)
                    Log.i(AppConstants.LOG_TAG, r.toString());
                isAPICalled = false;
            } catch (JSONException | IOException | NullPointerException e) {
                Log.i(AppConstants.LOG_TAG, e.getLocalizedMessage());
                isAPICalled = false;
            }
        });
    }

    private void setUI(JSONArray result) {
        try {
            String text = result.getJSONObject(0).getJSONArray("translations").getJSONObject(0).getString("text");
            runOnUiThread(() -> {
                mOutput.setText(text);
                mLabel.setVisibility(View.VISIBLE);
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
