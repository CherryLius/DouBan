package cherry.android.douban.web;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.base.BaseActivity;
import cherry.android.router.annotations.Extra;
import cherry.android.router.annotations.Route;
import cherry.android.router.api.Router;

/**
 * Created by Administrator on 2017/6/12.
 */

@Route("/activity/web")
public class WebActivity extends BaseActivity {
    @Extra(name = "ticket_url")
    String ticketUrl;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.web_view)
    WebView webView;

    @Override
    protected int getViewLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    protected void onViewInflated() {
        ButterKnife.bind(this);
        Router.inject(this);
        initView();
    }

    @Override
    protected void registerListener() {

    }

    @Override
    protected void unregisterListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.stopLoading();
        webView.removeAllViews();
        webView.destroy();
    }

    void initView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpWebViewDefaults(webView);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl(ticketUrl);
                refreshLayout.setRefreshing(false);
            }
        });

        refreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
                return webView.getScrollY() > 0;
            }
        });
        webView.loadUrl(ticketUrl);
    }

    void setUpWebViewDefaults(final WebView webView) {
        WebSettings settings = webView.getSettings();
        // 告诉WebView启用JavaScript执行。默认的是false。
        settings.setJavaScriptEnabled(true);
        // 设置此属性，可任意比例缩放。
        settings.setUseWideViewPort(true);
        // 网页内容的宽度是否可大于WebView控件的宽度
        settings.setLoadWithOverviewMode(false);
        settings.setBuiltInZoomControls(true);
        // 使用localStorage则必须打开
        settings.setDomStorageEnabled(true);
        // 是否应该支持使用其屏幕缩放控件和手势缩放
        settings.setSupportZoom(true);
        // 保存表单数据
        settings.setSaveFormData(true);
        //启用应用缓存
        settings.setAppCacheEnabled(true);
        //设置缓存模式
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //设置缓存路径
        settings.setAppCachePath(new File(getCacheDir(), "cache_web_view").getAbsolutePath());
        // 排版适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // WebView是否支持多个窗口。
        settings.setSupportMultipleWindows(true);
        //  页面加载好以后，再放开图片
        settings.setBlockNetworkImage(false);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            settings.setDisplayZoomControls(false);
        }
        // WebView从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setInitialScale(1);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getSupportActionBar().setTitle(title);
            }
        });
    }
}
