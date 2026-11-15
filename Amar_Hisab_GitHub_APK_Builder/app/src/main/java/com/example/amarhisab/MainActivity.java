package com.example.amarhisab;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.view.ViewGroup;

public class MainActivity extends Activity {
  private WebView webView;

  @Override public void onCreate(Bundle b) {
    super.onCreate(b);
    webView = new WebView(this);
    webView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    WebSettings s = webView.getSettings();
    s.setJavaScriptEnabled(true);
    s.setDomStorageEnabled(true);
    s.setAllowFileAccess(true);
    webView.setWebViewClient(new WebViewClient());
    setContentView(webView);
    webView.loadUrl("file:///android_asset/index.html");
  }

  @Override public void onBackPressed() {
    if (webView != null && webView.canGoBack()) {
      webView.goBack();
    } else {
      super.onBackPressed();
    }
  }
}
