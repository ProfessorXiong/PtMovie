package com.xiong.ptmovieday1.module.movie.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.xiong.ptmovieday1.R;

public class VideoPlayActivity extends AppCompatActivity {
    private String videoUrl;
    private String moviename;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        videoUrl = getIntent().getStringExtra("videourl");
        moviename = getIntent().getStringExtra("moviename");
        ((TextView)findViewById(R.id.tv_moviename)).setText(moviename);
        bindViews();
        webView.loadUrl(videoUrl);
    }

    private void bindViews(){
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
//                if(url.contains(".mp4?")){
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setDataAndType(Uri.parse(url),"video/mp4");
//                    startActivity(intent);
//                    finish();
//                    return;
//                }
                super.onLoadResource(view,url);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
}
