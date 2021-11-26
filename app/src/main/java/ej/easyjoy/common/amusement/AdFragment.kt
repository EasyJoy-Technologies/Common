package ej.easyjoy.common.amusement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ej.easyjoy.common.constants.EJConstants
import ej.easyjoy.common.databinding.FragmentAdBinding
import ej.easyjoy.common.newAd.AdListener
import ej.easyjoy.common.newAd.AdManager

class AdFragment : Fragment() {

    lateinit var binding: FragmentAdBinding
    private var mApplicationId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun setAppId(appId: Int){
        mApplicationId = appId
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAdBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var nativeAdId = ""
        var bannerAdId = ""
        when(mApplicationId){
            EJConstants.EJApplicationId.TOOLSBOX -> {
                nativeAdId = EJConstants.ToolsBox.GM_NATIVE_AD_ID
                bannerAdId = EJConstants.ToolsBox.GM_BANNER_ID
            }
        }
        AdManager.instance.showGMNativeAd(requireActivity(),binding.adContainer1, nativeAdId,object: AdListener() {
            override fun adClick() {

            }
            override fun adClose() {
            }
            override fun adError(error: String) {
            }
            override fun adShow() {
            }

        })
        AdManager.instance.showGMNativeAd(requireActivity(),binding.adContainer2, nativeAdId,object: AdListener(){
            override fun adClick() {

            }
            override fun adClose() {
            }
            override fun adError(error: String) {
            }
            override fun adShow() {
            }

        })
        AdManager.instance.showGMBannerAd(requireActivity(),binding.adContainer3, bannerAdId, object: AdListener(){
            override fun adClick() {
            }
            override fun adClose() {
            }
            override fun adError(error: String) {
            }
            override fun adShow() {
            }
        })
    }
}