package ej.easyjoy.common.base

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ej.easyjoy.common.databinding.DialogRecommendAdapterBinding

class RecommendDialogAdapter: RecyclerView.Adapter<RecommendDialogAdapter.RecommendViewHolder>(){

    var mData = ArrayList<Product>()
    private var buttonRes: Int? = null

    fun submit(data: List<Product>){
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }

    fun setButtonRes(res: Int?){
        buttonRes = res
    }

    class RecommendViewHolder(var binding: DialogRecommendAdapterBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product, res: Int?){
            binding.apply {
                if(res!=null){
                    binding.installButton.setBackgroundResource(res)
                }
                iconView.setBackgroundResource(product.icon)
                appNameView.text = product.appName
                tipsView.text = product.tips
                rootView.setOnClickListener{
                    if(Build.BRAND=="samsung"){
                        val uri = Uri.parse("http://www.samsungapps.com/appquery/appDetail.as?appId=" + product.packageName)
                        val goToMarket = Intent()
                        goToMarket.setClassName("com.sec.android.app.samsungapps", "com.sec.android.app.samsungapps.Main")
                        goToMarket.data = uri
                        try {
                            itemView.context.startActivity(goToMarket)
                        } catch (e: ActivityNotFoundException) {
                            e.printStackTrace()
                        }
                    }else {
                        val uri = Uri.parse("market://details?id=" + product.packageName)
                        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                        try {
                            itemView.context.startActivity(goToMarket)
                        } catch (e: ActivityNotFoundException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendViewHolder {
        return RecommendViewHolder(DialogRecommendAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecommendViewHolder, position: Int) {
        holder.bind(mData[position],buttonRes)
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}