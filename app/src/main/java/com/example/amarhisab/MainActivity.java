package com.example.amarhisab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.ViewGroup;
import android.widget.Toast;
import android.util.Base64;

import java.io.OutputStream;

public class MainActivity extends Activity {
  private static final int REQUEST_RESTORE_FILE = 4101;
  private static final int REQUEST_SAVE_FILE = 4102;
  private WebView webView;
  private ValueCallback<Uri[]> filePathCallback;
  private byte[] pendingFileBytes;
  private String pendingFileName;
  private String pendingMimeType;

  @Override public void onCreate(Bundle b) {
    super.onCreate(b);
    webView = new WebView(this);
    webView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    WebSettings s = webView.getSettings();
    s.setJavaScriptEnabled(true);
    s.setDomStorageEnabled(true);
    s.setAllowFileAccess(true);
    s.setAllowContentAccess(true);
    webView.addJavascriptInterface(new AndroidBridge(), "AndroidBridge");
    webView.setWebViewClient(new WebViewClient());
    webView.setWebChromeClient(new WebChromeClient() {
      @Override
      public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> callback,
                                       FileChooserParams params) {
        if (filePathCallback != null) filePathCallback.onReceiveValue(null);
        filePathCallback = callback;
        String mimeType = "application/json";
        String[] accepted = params.getAcceptTypes();
        if (accepted != null) {
          for (String type : accepted) {
            if (type != null && type.startsWith("image/")) { mimeType = "image/*"; break; }
          }
        }
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(mimeType);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
          startActivityForResult(intent, REQUEST_RESTORE_FILE);
          return true;
        } catch (Exception e) {
          filePathCallback = null;
          Toast.makeText(MainActivity.this, "ফাইল নির্বাচন করা যাচ্ছে না", Toast.LENGTH_LONG).show();
          return false;
        }
      }
    });
    setContentView(webView);
    webView.loadUrl("file:///android_asset/index.html");
  }

  private class AndroidBridge {
    @JavascriptInterface
    public void saveFile(String name, String mimeType, String base64) {
      try {
        pendingFileBytes = Base64.decode(base64, Base64.DEFAULT);
        pendingFileName = name == null ? "amar-hisab-file" : name.replaceAll("[^a-zA-Z0-9._-]", "_");
        pendingMimeType = mimeType == null ? "application/octet-stream" : mimeType;
        runOnUiThread(() -> {
          Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
          intent.addCategory(Intent.CATEGORY_OPENABLE);
          intent.setType(pendingMimeType);
          intent.putExtra(Intent.EXTRA_TITLE, pendingFileName);
          try {
            startActivityForResult(intent, REQUEST_SAVE_FILE);
          } catch (Exception e) {
            Toast.makeText(MainActivity.this, "ফাইল সংরক্ষণের ব্যবস্থা পাওয়া যায়নি", Toast.LENGTH_LONG).show();
          }
        });
      } catch (Exception e) {
        runOnUiThread(() -> Toast.makeText(MainActivity.this, "ফাইল তৈরি করা যায়নি", Toast.LENGTH_LONG).show());
      }
    }

    @JavascriptInterface
    public void printReport(String html) {
      runOnUiThread(() -> {
        WebView printView = new WebView(MainActivity.this);
        printView.getSettings().setJavaScriptEnabled(false);
        printView.setWebViewClient(new WebViewClient() {
          @Override public void onPageFinished(WebView view, String url) {
            PrintManager manager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
            if (manager != null) {
              manager.print("Amar-Hisab-Report", view.createPrintDocumentAdapter("Amar Hisab Report"),
                  new PrintAttributes.Builder()
                      .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                      .setResolution(new PrintAttributes.Resolution("report", "Report", 300, 300))
                      .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                      .build());
            }
          }
        });
        printView.loadDataWithBaseURL(null, html, "text/HTML", "UTF-8", null);
      });
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_RESTORE_FILE) {
      if (filePathCallback != null) {
        Uri[] result = resultCode == RESULT_OK && data != null && data.getData() != null
            ? new Uri[]{data.getData()} : null;
        filePathCallback.onReceiveValue(result);
        filePathCallback = null;
      }
    } else if (requestCode == REQUEST_SAVE_FILE) {
      if (resultCode == RESULT_OK && data != null && data.getData() != null && pendingFileBytes != null) {
        try (OutputStream out = getContentResolver().openOutputStream(data.getData())) {
          if (out != null) {
            out.write(pendingFileBytes);
            out.flush();
            Toast.makeText(this, "ফাইল সফলভাবে সংরক্ষণ হয়েছে", Toast.LENGTH_LONG).show();
          }
        } catch (Exception e) {
          Toast.makeText(this, "ফাইল সংরক্ষণ করা যায়নি", Toast.LENGTH_LONG).show();
        }
      }
      pendingFileBytes = null;
      pendingFileName = null;
      pendingMimeType = null;
    }
  }

  @Override public void onBackPressed() {
    if (webView != null && webView.canGoBack()) {
      webView.goBack();
    } else {
      super.onBackPressed();
    }
  }
}
