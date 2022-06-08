package com.winwang.myapplication.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.material.snackbar.Snackbar
import com.winwang.myapplication.R
import com.winwang.myapplication.TimeBean
import com.winwang.myapplication.databinding.ActivityScrollingBinding
import com.winwang.myapplication.utils.CenterLinearLayoutManager

class ScrollingActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityScrollingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
        binding.fab.setOnClickListener(this)
        initRvAdapter()
    }

    override fun onClick(view: View?) {
        view?.let {
            Snackbar.make(it, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun initRvAdapter() {
        val dataList = ArrayList<TimeBean>()
        val leftList = ArrayList<LeftBean>()
        for (i in 0..1000) {
            val timeBean = TimeBean()
            timeBean.header = i.toString()
            if (i % 10 == 0) {
                timeBean.type = 1
                val leftBean = LeftBean(name = i.toString(), i)
                leftList.add(leftBean)
                timeBean.leftPostion = leftList.size - 1
            } else {
                timeBean.leftPostion = leftList.size - 1
            }
            dataList.add(timeBean)
        }
        //初始化左边的RecycleView
        val leftAdapter = LeftAdapter()
        val centerLinearLayoutManager = CenterLinearLayoutManager(this)
        binding.rvLeft.layoutManager = centerLinearLayoutManager
        binding.rvLeft.adapter = leftAdapter
        leftAdapter.setNewData(leftList)

        //初始化右边的Recycleview
        val puPuAdapter = PuPuAdapter(dataList)
        binding.rvPupu.adapter = puPuAdapter
        binding.rvPupu.layoutManager = LinearLayoutManager(this)

        //监听滚动
        binding.rvPupu.addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstPosition = layoutManager.findFirstVisibleItemPosition()
                val itemViewType = puPuAdapter.getItemViewType(firstPosition)
                Log.d(">>>>>>>>>", "$firstPosition>>>>>>>$itemViewType")
//                if (itemViewType == 1) {
                val timeBean = dataList[firstPosition]
                val leftPostion = timeBean.leftPostion
                Log.e(">>>>>>>>>", "$firstPosition>>>>>>>$itemViewType>>>>>>>>>$leftPostion")
                if (leftPostion >= 0) {
                    leftAdapter.setSelectPosition(leftPostion)
//                    binding.rvLeft.scrollToPosition(timeBean.leftPostion)
                    centerLinearLayoutManager.smoothScrollToPosition(binding.rvLeft, null, timeBean.leftPostion)
                }
//                } else {
//
//                }
            }
        })

        //点击左边的滚动到具体右边的位置
        leftAdapter.setOnItemClickListener { adapter, view, position ->
            val leftBean = leftList[position]
            leftBean.rvPostion?.let {
                leftAdapter.setSelectPosition(position)
                binding.rvPupu.scrollToPosition(it)
            }
        }

    }


}


class PuPuAdapter(dataList: List<TimeBean>) : BaseMultiItemQuickAdapter<TimeBean, BaseViewHolder>(dataList) {

    companion object {
        const val TYPE_HEADER = 1
        const val TYPE_ITEM = 0
    }

    init {
        addItemType(TYPE_HEADER, R.layout.item_pupu_header_layout)
        addItemType(TYPE_ITEM, R.layout.item_pupu_layout)
    }

    override fun convert(holder: BaseViewHolder, data: TimeBean?) {
        when (holder.itemViewType) {
            1 -> {
                holder.setText(R.id.tv_title_header, data?.header)
            }
            0 -> {

            }
        }
    }

}


class LeftAdapter : BaseQuickAdapter<LeftBean, BaseViewHolder>(R.layout.item_pupu_left_layout) {

    var selectPosition: Int? = 0

    override fun convert(holder: BaseViewHolder, data: LeftBean?) {
        holder.setText(R.id.tv_left_title, data?.name)
        if (selectPosition == holder.adapterPosition) {
            holder.setBackgroundColor(R.id.tv_left_title, Color.parseColor("#ffff00"))
        } else {
            holder.setBackgroundColor(R.id.tv_left_title, Color.parseColor("#00ff00"))
        }
    }

    fun setSelectPosition(selectPosition: Int) {
        this.selectPosition = selectPosition
        notifyDataSetChanged()
    }

}


data class LeftBean(
    val name: String?,
    val rvPostion: Int?
) {
    var selectPosition: Int? = 0
}










