package com.example.bhagavadgitaapp.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
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
import androidx.core.content.FileProvider
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import com.example.bhagavadgitaapp.R
import com.example.bhagavadgitaapp.databinding.FragmentViewVerseWebViewBinding
import com.example.bhagavadgitaapp.helper.PreferenceHelper
import com.example.bhagavadgitaapp.utils.AppConstants
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
    private lateinit var preferenceHelper: PreferenceHelper

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
        preferenceHelper = PreferenceHelper(requireContext())
        setupMenuBar()
        binding.webView.webViewClient = WebViewClient()
        binding.webView.webChromeClient = WebChromeClient()
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.loadWithOverviewMode = true

        binding.webView.loadUrl(navArgs.url)
    }

    private fun setupMenuBar() {
        (requireActivity()).addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun captureWebPage() {
        val chapter = preferenceHelper.getString(AppConstants.lastReadChapter, "0")
        val verse = preferenceHelper.getString(AppConstants.lastReadVerseNum, "0")
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

            val cacheDir = requireContext().externalCacheDir
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "${timeStamp}_chapter-${chapter}-verse-${verse}.png"
            val file = File(cacheDir, fileName)

            val outputStream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            // Create an intent to share the image
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(requireContext(), requireContext().packageName + ".provider", file))

            // Grant temporary read permission to the receiver app
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            // Start the sharing intent
            startActivity(Intent.createChooser(shareIntent, "Share Image"))

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