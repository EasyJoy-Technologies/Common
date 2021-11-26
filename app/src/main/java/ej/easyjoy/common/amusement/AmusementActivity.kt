package ej.easyjoy.common.amusement

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import ej.easyjoy.common.R
import ej.easyjoy.common.constants.EJConstants
import ej.easyjoy.common.databinding.ActivityAmusementBinding
import ej.easyjoy.common.newAd.AdListener
import ej.easyjoy.common.newAd.AdManager


class AmusementActivity : AppCompatActivity() {

    lateinit var binding: ActivityAmusementBinding
    private var gameFragment: GameFragment? = null
    private var couponFragment: CouponFragment? = null
    private var mainColor: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainColor = intent.getIntExtra(EJConstants.INTENT_MAIN_COLOR_KEY,R.color.ad_button_color)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(mainColor!!)
        }
        binding = ActivityAmusementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.titleBar.setBackgroundResource(mainColor!!)
        binding.titleDivider1.setBackgroundResource(mainColor!!)
        binding.titleDivider2.setBackgroundResource(mainColor!!)
        binding.titleDivider3.setBackgroundResource(mainColor!!)
        binding.titleDivider4.setBackgroundResource(mainColor!!)
        var index = intent.getIntExtra(EJConstants.INTENT_AMUSEMENT_PAGE_INDEX_KEY, 0)
        var applicationId = intent.getIntExtra(EJConstants.INTENT_EJ_APPLICATION_ID_KEY, 0)
        var adFragment = AdFragment()
        adFragment.setAppId(applicationId)
        var baiduAdFragment = BaiduAdFragment()
        gameFragment = GameFragment()
        couponFragment = CouponFragment()
        var fragments = ArrayList<Fragment>()
        fragments.add(adFragment)
        fragments.add(baiduAdFragment)
        fragments.add(gameFragment!!)
        fragments.add(couponFragment!!)
        binding.apply {
            titleView.text = "广告中心"
            leftButton.setImageResource(R.mipmap.exit)
            leftButton.setOnClickListener {
                if (viewPager.currentItem == 2) {
                    gameFragment!!.goBack()
                } else if (viewPager.currentItem == 3) {
                    couponFragment!!.goBack()
                } else {
                    finish()
                }
            }
            rightButton.setOnClickListener {
                when (viewPager.currentItem) {
                    0 -> {
                        var adId = getFullVideoId(applicationId)
                        if(!TextUtils.isEmpty(adId)){
                            AdManager.instance.showGMFullVideoAd(this@AmusementActivity, adId, object : AdListener() {

                            })
                        }else{
                            Toast.makeText(this@AmusementActivity,"没有广告填充",Toast.LENGTH_SHORT).show()
                        }
                    }
                    1 -> {
                        baiduAdFragment.goHome()
                    }
                    2 -> {
                        gameFragment!!.goHome()
                    }
                    3 -> {
                        couponFragment!!.goHome()
                    }
                }
            }
            viewPager.adapter = object: FragmentPagerAdapter(supportFragmentManager){
                override fun getCount(): Int {
                    return fragments.size
                }
                override fun getItem(position: Int): Fragment {
                    return fragments[position]
                }
            }
            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    updateTitleView(position)
                }

                override fun onPageScrollStateChanged(state: Int) {
                }
            })
            titleGroup1.setOnClickListener{
                viewPager.currentItem = 0
                updateTitleView(0)
            }
            titleGroup2.setOnClickListener{
                viewPager.currentItem = 1
                updateTitleView(1)
            }
            titleGroup3.setOnClickListener{
                viewPager.currentItem = 2
                updateTitleView(2)
            }
            titleGroup4.setOnClickListener{
                viewPager.currentItem = 3
                updateTitleView(3)
            }
            viewPager.currentItem = index
            updateTitleView(index)
        }
    }

    private fun getFullVideoId(applicationId: Int): String{
        when(applicationId){
            EJConstants.EJApplicationId.TOOLSBOX-> {
                return EJConstants.ToolsBox.GM_VIDEO_AD_ID
            }
        }
        return ""
    }

    private fun updateTitleView(index: Int){
        setTitleBarVisible(View.VISIBLE)
        when(index){
            0 -> {
                binding.titleView.text = "广告推荐"
                binding.rightButton.setImageResource(R.mipmap.video_ad_icon)
                binding.titleView1.setTextColor(resources.getColor(mainColor!!))
                binding.titleView2.setTextColor(resources.getColor(R.color.ad_main_text_color_3))
                binding.titleView3.setTextColor(resources.getColor(R.color.ad_main_text_color_3))
                binding.titleView4.setTextColor(resources.getColor(R.color.ad_main_text_color_3))
                binding.titleDivider1.visibility = View.VISIBLE
                binding.titleDivider2.visibility = View.INVISIBLE
                binding.titleDivider3.visibility = View.INVISIBLE
                binding.titleDivider4.visibility = View.INVISIBLE
            }
            1 -> {
                binding.titleView.text = "资讯内容"
                binding.rightButton.setImageResource(R.mipmap.web_view_top_icon)
                binding.titleView1.setTextColor(resources.getColor(R.color.ad_main_text_color_3))
                binding.titleView2.setTextColor(resources.getColor(mainColor!!))
                binding.titleView3.setTextColor(resources.getColor(R.color.ad_main_text_color_3))
                binding.titleView4.setTextColor(resources.getColor(R.color.ad_main_text_color_3))
                binding.titleDivider1.visibility = View.INVISIBLE
                binding.titleDivider2.visibility = View.VISIBLE
                binding.titleDivider3.visibility = View.INVISIBLE
                binding.titleDivider4.visibility = View.INVISIBLE
            }
            2 -> {
                binding.titleView.text = "休闲中心"
                binding.rightButton.setImageResource(R.mipmap.go_home_icon)
                binding.titleView1.setTextColor(resources.getColor(R.color.ad_main_text_color_3))
                binding.titleView2.setTextColor(resources.getColor(R.color.ad_main_text_color_3))
                binding.titleView3.setTextColor(resources.getColor(mainColor!!))
                binding.titleView4.setTextColor(resources.getColor(R.color.ad_main_text_color_3))
                binding.titleDivider1.visibility = View.INVISIBLE
                binding.titleDivider2.visibility = View.INVISIBLE
                binding.titleDivider3.visibility = View.VISIBLE
                binding.titleDivider4.visibility = View.INVISIBLE
            }
            3 -> {
                binding.titleView.text = "优惠权益"
                binding.rightButton.setImageResource(R.mipmap.go_home_icon)
                binding.titleView1.setTextColor(resources.getColor(R.color.ad_main_text_color_3))
                binding.titleView2.setTextColor(resources.getColor(R.color.ad_main_text_color_3))
                binding.titleView3.setTextColor(resources.getColor(R.color.ad_main_text_color_3))
                binding.titleView4.setTextColor(resources.getColor(mainColor!!))
                binding.titleDivider1.visibility = View.INVISIBLE
                binding.titleDivider2.visibility = View.INVISIBLE
                binding.titleDivider3.visibility = View.INVISIBLE
                binding.titleDivider4.visibility = View.VISIBLE
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(binding.viewPager.currentItem == 2){
                gameFragment!!.goBack()
            }else if(binding.viewPager.currentItem==3){
                couponFragment!!.goBack()
            }else {
                finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    fun setTitleBarVisible(visible: Int){
        binding.titleBar.visibility = visible
        binding.indicationView.visibility = visible
    }
}