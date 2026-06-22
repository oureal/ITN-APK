package at.ourednik.tcbrunnitn;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView webView;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private static final String URL = "https://www.noetv.at/rangliste/itn#tcbrunn";

    private static final String FILTER_JS =
        "javascript:(()=>{"
        + "const q='TC Brunn/Geb';"
        + "const vis=e=>!!(e.offsetWidth||e.offsetHeight||e.getClientRects().length);"
        + "const run=()=>{"
        + "const inputs=[...document.querySelectorAll('input')].filter(vis);"
        + "const el=inputs.find(i=>/such|search/i.test((i.placeholder||'')+(i.name||'')+(i.id||'')+(i.className||'')))||inputs[0];"
        + "if(!el)return false;"
        + "el.focus();el.value=q;"
        + "['input','change','keyup'].forEach(t=>el.dispatchEvent(new Event(t,{bubbles:true})));"
        + "el.dispatchEvent(new KeyboardEvent('keydown',{key:'Enter',code:'Enter',bubbles:true}));"
        + "el.dispatchEvent(new KeyboardEvent('keyup',{key:'Enter',code:'Enter',bubbles:true}));"
        + "setTimeout(()=>{"
        + "const r=el.getBoundingClientRect();"
        + "const bs=[...document.querySelectorAll('button,input[type=submit],a')].filter(vis).filter(b=>!/filter|zurück|reset/i.test((b.innerText||'')+(b.value||'')));"
        + "bs.sort((a,b)=>{const A=a.getBoundingClientRect(),B=b.getBoundingClientRect();return Math.hypot(A.left-r.right,A.top-r.top)-Math.hypot(B.left-r.right,B.top-r.top)});"
        + "bs[0]?.click();"
        + "},500);"
        + "return true;"
        + "};"
        + "let n=0;"
        + "const timer=setInterval(()=>{n++; if(run()||n>12) clearInterval(timer);},500);"
        + "})()";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView, new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ));

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                handler.postDelayed(() -> webView.loadUrl(FILTER_JS), 1200);
                handler.postDelayed(() -> webView.loadUrl(FILTER_JS), 2500);
                handler.postDelayed(() -> webView.loadUrl(FILTER_JS), 4000);
            }
        });

        webView.loadUrl(URL);
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
