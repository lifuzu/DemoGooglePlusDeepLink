package com.weimed.rlee.demogoogleplusdeeplink;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.plus.PlusShare;

import java.lang.reflect.Method;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 */
public class DeepLinkActivity extends Activity {

    private static final String TAG = "DeepLink_Activity";
    private static final String URL_TO_LOAD = "http://google.com";

    protected WebView webView;
    protected WebSettings webSettings;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_link);

        String deepLinkId = PlusShare.getDeepLinkId(this.getIntent());
//        Intent launchIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(deepLinkId));
//        launchIntent.setPackage("com.example.app");
//        startActivity(launchIntent);
        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl(URL_TO_LOAD);


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d(TAG, cm.sourceId() + ":" + cm.lineNumber() + " " + cm.message());
                return true;
            }

            @Override
            public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota,
                                                long estimatedDatabaseSize, long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {
                quotaUpdater.updateQuota(estimatedDatabaseSize);
                Log.d(TAG, "onExceededDatabaseQuota " + url + " " + databaseIdentifier + " " + quota
                        + " " + estimatedDatabaseSize + " " + totalQuota + " " + quotaUpdater);
            }

            @Override
            public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
                quotaUpdater.updateQuota(requiredStorage);
                Log.d(TAG, "onReachedMaxAppCacheSize current = " + quota + " required = " + requiredStorage);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            /*@Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                Log.v(TAG, "shouldInterceptRequest: " + url);
                WebResourceResponse response = ResourceManager.createLocalResourceResponse(WebActivity.this, url);
                return response != null ? response : super.shouldInterceptRequest(view, url);
            }*/

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.d(TAG, "onReceivedError " + description + " for " + failingUrl);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.v(TAG, "shouldOverrideUrlLoading: " + url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);

        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDatabasePath(getDir("webapp_db", MODE_PRIVATE).getAbsolutePath());
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(getDir("webapp_cache", MODE_PRIVATE).getAbsolutePath());

        // make sure the viewport meta tag is honored
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        setUniversalAccessEnabled(webSettings, true);
        webView.setWebContentsDebuggingEnabled(true);
    }

    private static boolean setUniversalAccessEnabled(WebSettings webSettings, boolean enabled) {
        if (Build.VERSION.SDK_INT >= 16 /*JELLY_BEAN*/) {
            try {
                Method webSettingsMethod = WebSettings.class.getDeclaredMethod("setAllowUniversalAccessFromFileURLs", Boolean.TYPE);
                webSettingsMethod.invoke(webSettings, enabled);
                Log.d(TAG, "WebView universal access enabled = " + enabled);

                webSettingsMethod = WebSettings.class.getDeclaredMethod("setAllowFileAccessFromFileURLs", Boolean.TYPE);
                webSettingsMethod.invoke(webSettings, enabled);
                Log.d(TAG, "WebView file access enabled = " + enabled);

                return true;
            } catch (Exception e) {
                Log.e(TAG, "Failed to enable WebView universal access", e);
            }
        }
        return false;
    }
}
