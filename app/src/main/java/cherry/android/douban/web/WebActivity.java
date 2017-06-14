package cherry.android.douban.web;

import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.base.BaseActivity;
import cherry.android.douban.widget.FixedSwipeRefreshLayout;
import cherry.android.router.annotations.Route;
import cherry.android.router.annotations.RouteField;
import cherry.android.router.api.Router;

/**
 * Created by Administrator on 2017/6/12.
 */

@Route("movie://activity/web")
public class WebActivity extends BaseActivity {
    @RouteField(name = "ticket_url")
    String ticketUrl;
    @BindView(R.id.swipe_refresh)
    FixedSwipeRefreshLayout refreshLayout;
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
        Router.bind(this);
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
        refreshLayout.setCanChildScrollUpCallback(new FixedSwipeRefreshLayout.ChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp() {
                return webView.getScrollY() > 0;
            }
        });
        webView.loadUrl(ticketUrl);
    }

    void setUpWebViewDefaults(final WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            settings.setDisplayZoomControls(false);
        }
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
