package com.example.mobilesecurityhw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView login_TXT_description;
    private TextView login_TXT_password;
    private EditText login_EXT_password;
    private Button login_BTN_submit;
    private LinearLayout login_LYO_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initViews();

    }

    private void findViews() {
        login_TXT_description = findViewById(R.id.login_TXT_description);
        login_TXT_password = findViewById(R.id.login_TXT_password);
        login_EXT_password = findViewById(R.id.login_EXT_password);
        login_BTN_submit = findViewById(R.id.login_BTN_submit);
        login_LYO_main = findViewById(R.id.login_LYO_main);
    }

    private void initViews() {
        initTextViews();
        initEditView();
        initButton();
    }

    private void initTextViews() {
        login_TXT_description.setText("The password is: \n<BatteryPercentage><ScreenBrightness>\nThe phone volume level must be zero (lowest)");
        login_TXT_password.setText("Insert Password Here");
    }

    private void initEditView() {
        login_EXT_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    private void initButton() {
        login_BTN_submit.setOnClickListener(v -> {
            if (validatePassword(login_EXT_password.getText().toString()))
            {
                login_LYO_main.setBackground(getDrawable(R.color.dark_green));
                login_TXT_password.setText("Correct!!");
            }
            else{
                login_LYO_main.setBackground(getDrawable(R.color.dark_red));
                login_TXT_password.setText("Please Try Again");
            }
        });
    }

    private Boolean validatePassword(String password){
        String dynamicPassword = "" + getPhoneBatteryPercent() + getPhoneBrightness();
        if (isPhoneMuted()) return dynamicPassword.equals(password);
        return false;
    }

    private int getPhoneBatteryPercent() {
        BatteryManager bm = (BatteryManager) this.getSystemService(BATTERY_SERVICE);
        return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }

    private int getPhoneBrightness() {
        // In different phone there are different ranges of brightnesses, please note that in our
        // tests we conclude that this range scales between 0 - 255 but in our  AVD Pixel 4 the
        // range was from 10 - 255, we decided to left it has is with a Logging message to show the
        // current brightness to the testers
        int brightness =  Settings.System.getInt(
                this.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS,
                0
        );
        Log.println(Log.INFO, "brightness", "The current brightness is: " + brightness);
        return brightness;
    }

    private boolean isPhoneMuted() {
        AudioManager audio = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        return audio.getStreamVolume(AudioManager.STREAM_MUSIC) == 0;
    }

}