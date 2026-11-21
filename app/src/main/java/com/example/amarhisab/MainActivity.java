package com.example.amarhisab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.util.Base64;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.OutputStream;

public class MainActivity extends Activity {
  private static final int REQUEST_RESTORE = 4101;
  private static final int REQUEST_SAVE = 4102;
  private WebView webView;
  private ValueCallback<Uri[]> fileCallback;
  private byte[] pendingBytes;
  private String pendingName;
  private String pendingMime;

  @Override public void onCreate(Bundle state) {
    super.onCreate(state);
    webView = new WebView(this);
    webView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    WebSettings settings = webView.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setDomStorageEnabled(true);
    settings.setAllowFileAccess(true);
    settings.setAllowContentAccess(true);
    webView.addJavascriptInterface(new AndroidBridge(), "AndroidBridge");
    webView.setWebViewClient(new WebViewClient());
    webView.setWebChromeClient(new WebChromeClient() {
      @Override public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> callback,
                                                   FileChooserParams params) {
        if (fileCallback != null) fileCallback.onReceiveValue(null);
        fileCallback = callback;
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
          startActivityForResult(intent, REQUEST_RESTORE);
          return true;
        } catch (Exception e) {
          fileCallback = null;
          Toast.makeText(MainActivity.this, "Backup ফাইল নির্বাচন করা যাচ্ছে না", Toast.LENGTH_LONG).show();
          return false;
        }
      }
    });
    setContentView(webView);
    webView.loadUrl("file:///android_asset/index.html");
  }

  private class AndroidBridge {
    @JavascriptInterface public void saveFile(String name, String mime, String base64) {
      try {
        pendingBytes = Base64.decode(base64, Base64.DEFAULT);
        pendingName = name == null ? "amar-hisab-backup.json" : name.replaceAll("[^a-zA-Z0-9._-]", "_");
        pendingMime = mime == null ? "application/octet-stream" : mime;
        runOnUiThread(() -> {
          Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
          intent.addCategory(Intent.CATEGORY_OPENABLE);
          intent.setType(pendingMime);
          intent.putExtra(Intent.EXTRA_TITLE, pendingName);
          try { startActivityForResult(intent, REQUEST_SAVE); }
          catch (Exception e) {
            Toast.makeText(MainActivity.this, "ফাইল সংরক্ষণের অপশন পাওয়া যায়নি", Toast.LENGTH_LONG).show();
          }
        });
      } catch (Exception e) {
        runOnUiThread(() -> Toast.makeText(MainActivity.this, "ফাইল তৈরি করা যায়নি", Toast.LENGTH_LONG).show());
      }
    }

    @JavascriptInterface public void printReport(String html) {
      runOnUiThread(() -> {
        WebView printView = new WebView(MainActivity.this);
        printView.setWebViewClient(new WebViewClient() {
          @Override public void onPageFinished(WebView view, String url) {
            PrintManager manager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
            if (manager != null) {
              manager.print("Amar-Hisab-Complete-Report", view.createPrintDocumentAdapter("Amar Hisab Report"),
                new PrintAttributes.Builder()
                  .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                  .setResolution(new PrintAttributes.Resolution("report", "Report", 300, 300))
                  .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build());
            }
          }
        });
        printView.loadDataWithBaseURL(null, html, "text/HTML", "UTF-8", null);
      });
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_RESTORE) {
      if (fileCallback != null) {
        Uri[] result = resultCode == RESULT_OK && data != null && data.getData() != null
          ? new Uri[]{data.getData()} : null;
        fileCallback.onReceiveValue(result);
        fileCallback = null;
      }
    } else if (requestCode == REQUEST_SAVE) {
      if (resultCode == RESULT_OK && data != null && data.getData() != null && pendingBytes != null) {
        try (OutputStream out = getContentResolver().openOutputStream(data.getData())) {
          if (out != null) {
            out.write(pendingBytes);
            out.flush();
            Toast.makeText(this, "ফাইল সফলভাবে সংরক্ষণ হয়েছে", Toast.LENGTH_LONG).show();
          }
        } catch (Exception e) {
          Toast.makeText(this, "ফাইল সংরক্ষণ করা যায়নি", Toast.LENGTH_LONG).show();
        }
      }
      pendingBytes = null;
      pendingName = null;
      pendingMime = null;
    }
  }

  @Override public void onBackPressed() {
    if (webView != null && webView.canGoBack()) webView.goBack();
    else super.onBackPressed();
  }
}
