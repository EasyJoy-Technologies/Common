package ej.easyfone.adcommon.newAd

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import ej.easyfone.adcommon.databinding.FragmentRecommendDialogBinding


class RecommendDialogFragment : DialogFragment() {

    lateinit var binding: FragmentRecommendDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = FragmentRecommendDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            var recommendDialogAdapter = RecommendDialogAdapter()
            recyclerView.adapter = recommendDialogAdapter
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recommendDialogAdapter.submit(ProductUtils.getOurProduct())
        }
    }

    override fun onStart() {
        super.onStart()
        val window: Window? = dialog!!.window
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var layoutParams = window!!.attributes
        var manager = requireActivity().windowManager
        var outMetrics = DisplayMetrics()
        manager.defaultDisplay.getMetrics(outMetrics)
        var width = outMetrics.widthPixels
        var height = outMetrics.heightPixels
        layoutParams.width = width * 7 / 8
        layoutParams.height = height * 2 / 3
        window!!.attributes = layoutParams
    }
}