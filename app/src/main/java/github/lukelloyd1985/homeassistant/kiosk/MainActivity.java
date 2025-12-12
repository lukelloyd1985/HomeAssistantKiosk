package github.lukelloyd1985.homeassistant.kiosk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends Activity {

    private WebView webView;
    private GestureDetector gestureDetector;
    private static final int LONG_PRESS_DURATION = 3000; // 3 seconds
    private long touchStartTime = 0;
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Allow kiosk to show when screen wakes, but don't prevent screensaver
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        // Initialize WakeLock for touch-to-wake functionality
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "HomeAssistantKiosk::WakeLock"
        );

        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webview);

        // Configure WebView settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);

        // Fix viewport settings for better Home Assistant compatibility
        webSettings.setLoadWithOverviewMode(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(false);

        // Set proper user agent to ensure Home Assistant recognizes the browser
        String userAgent = webSettings.getUserAgentString();
        webSettings.setUserAgentString(userAgent + " HomeAssistantKiosk/1.0");

        // Enable mixed content mode for compatibility (API 21+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        // Enable caching for better performance and authentication persistence
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        // Enable cookies for authentication
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        // Third-party cookies require API 21+ (automatically allowed on API 17-19)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }

        // Prevent links from opening in external browser
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        // Hide navigation bar and status bar for true kiosk mode
        hideSystemUI();

        // Load kiosk URL from SharedPreferences (or default from resources)
        SharedPreferences prefs = getSharedPreferences("KioskPrefs", MODE_PRIVATE);
        String kioskUrl = prefs.getString("kiosk_url", getString(R.string.kiosk_url));
        webView.loadUrl(kioskUrl);

        // Setup long-press gesture to access settings
        setupSettingsGesture();
    }

    private void setupSettingsGesture() {
        // Long-press on top-right corner (within 100px area) for 3 seconds to open settings
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Wake screen on touch
                        wakeScreen();

                        // Check if touch is in top-right corner
                        if (event.getX() > v.getWidth() - 100 && event.getY() < 100) {
                            touchStartTime = System.currentTimeMillis();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (touchStartTime > 0) {
                            long pressDuration = System.currentTimeMillis() - touchStartTime;
                            if (pressDuration >= LONG_PRESS_DURATION) {
                                openSettings();
                            }
                            touchStartTime = 0;
                        }
                        break;
                }
                return false; // Allow WebView to handle the touch event
            }
        });
    }

    private void wakeScreen() {
        if (wakeLock != null && !wakeLock.isHeld()) {
            wakeLock.acquire(3000); // Keep screen on for 3 seconds, then allow normal timeout
        }
    }

    private void openSettings() {
        Toast.makeText(this, "Opening settings...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LOW_PROFILE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Disable back button
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        // Disable home button (requires system app for full effect)
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release wake lock when activity is destroyed
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
    }
}
