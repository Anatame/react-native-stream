package com.bluemist

import android.os.Build
import android.view.View
import android.webkit.*
import com.bluemist.utils.BlockHosts
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.uimanager.events.RCTEventEmitter
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.security.AccessController.getContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule

import androidx.lifecycle.Transformations.map
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import androidx.lifecycle.Transformations.map





class HeadlessWebView : SimpleViewManager<WebView>() {

    override fun getName() = "HeadlessWebView"

    private var webView: WebView? = null
    private var url: String? = null

    @ReactProp(name = "url")
    fun setWebViewSource(view: WebView, newId: String?) {
        if (newId == null || newId == url) return
        url = newId

        val map = HashMap<String, String>()
        map["referer"] = "https://fmoviesto.cc"

        webView?.let {
            it.loadUrl(url!!, map)
        }
    }

    override fun createViewInstance(reactContext: ThemedReactContext): WebView {
        webView = WebView(reactContext)

        webView?.let {
            webViewLayerType()
            it.settings.userAgentString =
                "Mozilla/5.0 (Android 4.4; Tablet; rv:41.0) Gecko/41.0 Firefox/41.0"
            it.settings.javaScriptEnabled = true
            it.settings.domStorageEnabled = true
            it.settings.cacheMode = WebSettings.LOAD_DEFAULT
            it.settings.databaseEnabled = true
            it.settings.mediaPlaybackRequiresUserGesture = false
            it.settings.blockNetworkImage = true
            it.settings.loadsImagesAutomatically = true

            it.webViewClient = object : WebViewClient() {

                override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                    if (BlockHosts().hosts.contains(request!!.url.host)) {
                        val textStream: InputStream = ByteArrayInputStream("".toByteArray())
                        return getTextWebResource(textStream)
                    }
                    return super.shouldInterceptRequest(view, request)
                }

                override fun onLoadResource(view: WebView?, url: String?) {
                    super.onLoadResource(view, url)
                    if (url != null) {
                        if(url.endsWith("playlist.m3u8")){
                          //  streamUrl.postValue(url!!)
                            val eventData = Arguments.createMap()
                            eventData.putString("message", url!!);

                            reactContext.getJSModule(RCTDeviceEventEmitter::class.java)
                                .emit("loaded", eventData)
                        }
                    }
                }
            }

        }

        return webView as WebView
    }


    private fun getTextWebResource(data: InputStream): WebResourceResponse {
        return WebResourceResponse("text/plain", "UTF-8", data);
    }

    private fun webViewLayerType() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView?.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView?.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }
}
