package com.example.bhagavadgitaapp.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import com.example.bhagavadgitaapp.R
import com.example.bhagavadgitaapp.databinding.FragmentViewVerseWebViewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class FragmentViewVerseWebView : Fragment(), MenuProvider {

    lateinit var binding: FragmentViewVerseWebViewBinding
    private val navArgs: FragmentViewVerseWebViewArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewVerseWebViewBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupUI() {
        setupMenuBar()
        binding.webView.webViewClient = WebViewClient()
        binding.webView.webChromeClient = WebChromeClient()
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.loadWithOverviewMode = true

        val url = navArgs.url
        binding.webView.loadUrl(url)
    }

    private fun setupMenuBar() {
        (requireActivity()).addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun captureWebPage() {

        try {
            // Create a WebView screenshot
            binding.webView.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            binding.webView.layout(
                0,
                0,
                1800,
                2000
            )
            val bitmap = Bitmap.createBitmap(1750, 1600, Bitmap.Config.ARGB_8888)
            binding.webView.draw(Canvas(bitmap))

            val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "${timeStamp}_bhagavad_geeta.png"
            val file = File(directory, fileName)

            val outputStream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            Snackbar.make(binding.root, "Image saved in Download folder!", Snackbar.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Failed to Save please try again!", Toast.LENGTH_SHORT).show()
            e.localizedMessage?.let { Log.e("img", it) }
            Log.e("img", e.stackTrace.toString())
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_download, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId) {
            R.id.downloadItem -> {
                captureWebPage()
            }
        }
        return false
    }
}