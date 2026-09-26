package af.recharge.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public final class MainActivity extends Activity {
    private static final String ROOT = "https://recharge.af/";
    private static final int INK = Color.rgb(14,45,91);
    private static final int BLUE = Color.rgb(14,75,222);
    private static final int MUTED = Color.rgb(102,119,139);
    private WebView webView;
    private ProgressBar progress;
    private LinearLayout navigation;

    private int dp(float v) { return Math.round(v * getResources().getDisplayMetrics().density); }
    private GradientDrawable background(int color, int radius) {
        GradientDrawable d = new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(radius));return d;
    }
    private boolean internal(Uri uri) {
        String host=uri.getHost();
        return "https".equalsIgnoreCase(uri.getScheme()) &&
            ("recharge.af".equalsIgnoreCase(host) || "www.recharge.af".equalsIgnoreCase(host));
    }
    private void external(Uri uri) {
        try { startActivity(new Intent(Intent.ACTION_VIEW, uri)); }
        catch (Exception ignored) { Toast.makeText(this,"No app can open this link",Toast.LENGTH_SHORT).show(); }
    }
    private void load(String path) { webView.loadUrl(ROOT+path); }

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        LinearLayout page=new LinearLayout(this);page.setOrientation(LinearLayout.VERTICAL);page.setBackgroundColor(Color.WHITE);
        LinearLayout header=new LinearLayout(this);header.setGravity(Gravity.CENTER_VERTICAL);header.setPadding(dp(18),dp(7),dp(18),dp(7));header.setBackgroundColor(Color.WHITE);
        ImageView logo=new ImageView(this);logo.setImageResource(R.drawable.recharge_logo);logo.setScaleType(ImageView.ScaleType.FIT_CENTER);header.addView(logo,new LinearLayout.LayoutParams(dp(180),dp(52)));
        TextView secure=new TextView(this);secure.setText("●  SECURE TOP-UP");secure.setTextColor(MUTED);secure.setTypeface(null,Typeface.BOLD);secure.setTextSize(10);secure.setGravity(Gravity.END|Gravity.CENTER_VERTICAL);header.addView(secure,new LinearLayout.LayoutParams(0,dp(40),1));
        page.addView(header,new LinearLayout.LayoutParams(-1,dp(66)));
        FrameLayout content=new FrameLayout(this);
        webView=new WebView(this);webView.setBackgroundColor(Color.WHITE);webView.getSettings().setJavaScriptEnabled(true);webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportMultipleWindows(true);webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        CookieManager.getInstance().setAcceptCookie(true);CookieManager.getInstance().setAcceptThirdPartyCookies(webView,false);
        webView.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if(internal(request.getUrl()))return false;external(request.getUrl());return true;
            }
            @Override public void onPageFinished(WebView view,String url) {
                CookieManager.getInstance().flush();
                if(internal(Uri.parse(url))) {
                    // The native toolbar and tabs replace the site's header on small screens.
                    view.evaluateJavascript("(function(){if(document.getElementById('recharge-app-style'))return;var s=document.createElement('style');s.id='recharge-app-style';s.textContent='.site-header,.dash-header,footer,.intro{display:none!important}.hero{display:block!important;padding:18px 0 35px!important}.hero:after{display:none!important}.recharge-card{box-shadow:none!important}.dashboard-main{padding-top:20px!important}.auth-shell{min-height:auto!important;padding:18px!important}body{--green:#06baa9;--deep:#0e2d5b;--ink:#123568;--bg:#f3f8fc}.primary-btn,.small-btn{background:#0e4bde!important}.operator.selected,.product.selected{border-color:#06baa9!important}.how{background:#0e2d5b!important}';document.head.appendChild(s)})()",null);
                }
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override public void onProgressChanged(WebView view,int value) { progress.setProgress(value);progress.setVisibility(value<100?View.VISIBLE:View.GONE); }
            @Override public boolean onCreateWindow(WebView view,boolean isDialog,boolean userGesture,Message resultMsg) {
                WebView popup=new WebView(MainActivity.this);
                popup.setWebViewClient(new WebViewClient() {
                    @Override public boolean shouldOverrideUrlLoading(WebView v,WebResourceRequest request) {
                        Uri uri=request.getUrl();if(internal(uri))webView.loadUrl(uri.toString());else external(uri);v.destroy();return true;
                    }
                });
                WebView.WebViewTransport transport=(WebView.WebViewTransport)resultMsg.obj;transport.setWebView(popup);resultMsg.sendToTarget();return true;
            }
        });
        content.addView(webView,new FrameLayout.LayoutParams(-1,-1));
        progress=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);progress.setMax(100);content.addView(progress,new FrameLayout.LayoutParams(-1,dp(3),Gravity.TOP));
        page.addView(content,new LinearLayout.LayoutParams(-1,0,1));
        navigation=new LinearLayout(this);navigation.setGravity(Gravity.CENTER);navigation.setPadding(dp(8),dp(6),dp(8),dp(6));navigation.setBackgroundColor(Color.WHITE);navigation.setElevation(dp(8));
        addNav("⌂","Home",()->load("index.php"),true);addNav("▣","Orders",()->load("account.php"),false);addNav("◉","Balance",()->load("account.php"),false);addNav("✉","Help",()->external(Uri.parse("https://wa.me/971553251586")),false);
        page.addView(navigation,new LinearLayout.LayoutParams(-1,dp(72)));setContentView(page);
        if(state==null)load("index.php");else webView.restoreState(state);
    }
    private void addNav(String icon,String label,Runnable action,boolean selected) {
        LinearLayout item=new LinearLayout(this);item.setGravity(Gravity.CENTER);item.setOrientation(LinearLayout.VERTICAL);
        if(selected)item.setBackground(background(Color.rgb(231,248,248),17));
        TextView symbol=new TextView(this);symbol.setText(icon);symbol.setTextSize(22);symbol.setTextColor(selected?BLUE:MUTED);symbol.setGravity(Gravity.CENTER);
        TextView text=new TextView(this);text.setText(label);text.setTextSize(11);text.setTypeface(null,Typeface.BOLD);text.setTextColor(selected?BLUE:MUTED);text.setGravity(Gravity.CENTER);
        item.addView(symbol);item.addView(text);item.setOnClickListener(v->action.run());navigation.addView(item,new LinearLayout.LayoutParams(0,-1,1));
    }
    @Override protected void onSaveInstanceState(Bundle state) { webView.saveState(state);super.onSaveInstanceState(state); }
    @Override public void onBackPressed() { if(webView.canGoBack())webView.goBack();else super.onBackPressed(); }
    @Override protected void onDestroy() { if(webView!=null)webView.destroy();super.onDestroy(); }
}
