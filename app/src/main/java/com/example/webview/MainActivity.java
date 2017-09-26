package com.example.webview;

import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private WebView wb;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wb= (WebView) findViewById(R.id.wb);
        btn= (Button) findViewById(R.id.btn);

        //支持js
        WebSettings webSettings = wb.getSettings();
        webSettings.setJavaScriptEnabled(true);


        wb.loadUrl("https://www.baidu.com/");

        /**
         * setWebViewClient（如果用户设置了WebViewClient，则在点击新的链接以后
         就不会跳转到系统浏览器了，而是在本WebView中显示。)
         *
         * WebViewClient主要用来辅助WebView处理各种通知、请求等事件，通过setWebViewClient方法设置。
         */

        wb.setWebViewClient(new WebViewClient(){

            //加载网页时替换某个资源(将网页的logo替换掉)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                WebResourceResponse response = null;
                if (url.contains("logo")) {
                    try {
                        InputStream logo = getAssets().open("tishi_3x.png");
                        response = new WebResourceResponse("image/png", "UTF-8", logo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return response;
            }

            //处理https请求
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();  // 接受信任所有网站的证书
                // handler.cancel();   // 默认操作 不处理
                // handler.handleMessage(null);  // 可做其他处理
            }

        });

        /**
         * WebChromeClient主要用来辅助WebView处理Javascript的对话框、
         网站图标、网站标题以及网页加载进度等
         */

        wb.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                setTitle("页面加载中，请稍候..." + progress + "%");
                setProgress(progress * 100);
                setProgressBarVisibility(true);
                if (progress == 100) {
                   setTitle("加载完毕");
                    setProgressBarVisibility(false);
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wb.canGoBack()){
                    wb.goBack();
                }else{
                    Toast.makeText(MainActivity.this, "不能在返回了", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
