package io.livekit.android.websockettester

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import io.livekit.android.websockettester.databinding.ActivityMainBinding
import okhttp3.*
import okio.ByteString

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var okHttpClient: OkHttpClient
    private var currentWs: WebSocket? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        okHttpClient = OkHttpClient()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            currentWs?.cancel()
            val url = "wss://demo.piesocket.com/v3/channel_1?api_key=VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV&notify_self"
            val request = Request.Builder()
                .url(url)
                .build()
            currentWs = okHttpClient.newWebSocket(request, object : WebSocketListener() {
                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    Log.e(TAG, "onClosed: $reason")
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    Log.e(TAG, "onClosing: $reason")
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    Log.e(TAG, "onFailure", t)
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    Log.e(TAG, "onMessage: $text")
                }

                override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                    Log.e(TAG, "onMessageBytes")
                }

                override fun onOpen(webSocket: WebSocket, response: Response) {
                    Log.e(TAG, "onOpen")
                }
            })
        }
    }

    override fun onDestroy() {
        currentWs?.cancel()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    companion object {
        const val TAG = "WebsocketTester"
    }
}