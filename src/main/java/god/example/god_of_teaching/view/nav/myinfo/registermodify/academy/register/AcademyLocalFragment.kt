package god.example.god_of_teaching.view.nav.myinfo.registermodify.academy.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentAcademyLocalBinding
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.viewmodel.AcademyViewModel

//학원 지역 입력 받는 프래그먼트 다음 지도 api사용
@AndroidEntryPoint
class AcademyLocalFragment : Fragment() {

    private var _binding: FragmentAcademyLocalBinding? = null
    private val binding get() = _binding!!
    private var localNum : String? = null
    private var local : String? = null

    private val academyView : AcademyViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAcademyLocalBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()
        loadMap()
        next()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //기본세팅
    private fun basicSetting()
    {
        val navController = findNavController()
        NavigationUtil.handleBackPressRegister(this,navController)
        binding.toolbarTitleAcademyLocal.text = "학원 등록하기"
    }
    //지도 불러오기
    private fun loadMap()
    {
        binding.tvSearchAcademyLocal.setOnClickListener {
            if(binding.webView.isVisible == false)
            {
                binding.webView.isVisible=true
                setupWebView()
                setupButton()
                //나중에 코드 간결히
                binding.viewgroupLocalAcademy.isVisible=false
            }
            else
            {
                setupWebView()
                setupButton()
                binding.viewgroupLocalAcademy.isVisible=false
            }

        }
    }
    //다음 지도 웹뷰 띄우기
    private fun setupWebView() {
            binding.webView.apply {
                settings.javaScriptEnabled = true
                settings.allowFileAccessFromFileURLs = true  // Allow access from file URLs


                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        Log.d("WebView", "Page loaded: $url")
                    }
                }

                webChromeClient = object : WebChromeClient() {
                    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                        Log.d(
                            "WebView",
                            "${consoleMessage?.message()} at ${consoleMessage?.sourceId()}: ${
                                consoleMessage?.lineNumber()
                            }"
                        )
                        return super.onConsoleMessage(consoleMessage)
                    }
                }

                addJavascriptInterface(WebAppInterface { postcode, roadAddress ->
                    activity?.runOnUiThread {
                        local = roadAddress
                        localNum = postcode
                        binding.tvLocalNumberAcademyLocal.text = " " + postcode
                        binding.tvLocalLocalAcademy.text = " "+ roadAddress
                        binding.webView.isVisible = false
                        binding.viewgroupLocalAcademy.isVisible=true
                    }
                }, "Android")
            }

    }
    //url불러오기
    private fun setupButton() {

        // Replace the URL with your actual URL.
        binding.webView.loadUrl("file:///android_asset/postcode.html")

    }
    //데이터 업로드 및 다음 프래그먼트로 이동
    private fun next()
    {
        binding.btnNextLocalAcademy.setOnClickListener {

           val detailAddress = binding.etDetailLocal.text.toString()

             if(local==null||localNum==null||detailAddress.isEmpty())
             {
                 Toast.makeText(context, "모든 정보를 다 입력하지 않으셨습니다.", Toast.LENGTH_SHORT).show()
             }
            else
             {
                 academyView.uploadLocalData(local!!,localNum!!,detailAddress)
                 val navController = findNavController()
                 navController.popBackStack()
                 navController.navigate(R.id.academySearchedAddressFragment)
             }


        }
    }
    //웹과 연결하는 코드
    inner class WebAppInterface(private val onComplete: (String, String) -> Unit) {
        @JavascriptInterface
        fun onAddressSelected(postcode: String, roadAddress: String) {
            onComplete(postcode, roadAddress)
        }
    }


}