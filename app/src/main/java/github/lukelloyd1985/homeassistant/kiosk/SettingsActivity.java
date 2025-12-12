package github.lukelloyd1985.homeassistant.kiosk;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity {

    private EditText urlInput;
    private Button saveButton;
    private Button cancelButton;
    private Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        urlInput = (EditText) findViewById(R.id.url_input);
        saveButton = (Button) findViewById(R.id.save_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        exitButton = (Button) findViewById(R.id.exit_button);

        // Load current URL from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("KioskPrefs", MODE_PRIVATE);
        String currentUrl = prefs.getString("kiosk_url", getString(R.string.kiosk_url));
        urlInput.setText(currentUrl);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUrl();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitApp();
            }
        });
    }

    private void saveUrl() {
        String url = urlInput.getText().toString().trim();

        if (url.isEmpty()) {
            Toast.makeText(this, "URL cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Basic URL validation
        if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("file://")) {
            Toast.makeText(this, "URL must start with http://, https://, or file://", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save to SharedPreferences
        SharedPreferences prefs = getSharedPreferences("KioskPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("kiosk_url", url);
        editor.commit();

        Toast.makeText(this, "URL saved. Please restart the app.", Toast.LENGTH_LONG).show();
        finish();
    }

    private void exitApp() {
        // Close all activities and remove task from recent apps
        finishAffinity();
        System.exit(0);
    }
}
