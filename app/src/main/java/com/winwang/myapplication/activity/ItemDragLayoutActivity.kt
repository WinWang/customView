package com.winwang.myapplication.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout
import com.winwang.myapplication.R
import com.winwang.myapplication.utils.AssetsFileUtil
import com.winwang.myapplication.utils.AudioPlayer

/**
 * 侧滑列表
 */
@SuppressLint("MissingInflatedId")
class ItemDragLayoutActivity : AppCompatActivity() {

    var selectSwitch: Boolean = false

    var selectCount = 0

    var selectedAll: Boolean = false

    val dataList = mutableListOf<MultiBean>()

    var expandCount = 3

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n", "LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_drag_layout)

        val tvCount = findViewById<TextView>(R.id.tvCount)
        AudioPlayer.playAssert(this, "audio.mp3")
        val tempLinkHashMap = LinkedHashMap<String, MutableList<MultiBean>>()
        tempLinkHashMap.clear()
        for (index in 0 until 5) {
            val tempList = mutableListOf<MultiBean>()
            if (index == 0) {
                for (i in 0 until 2) {
                    tempList.add(MultiBean("$index", "这是内容文件>>>>>>$i", 1))
                }
            } else {
                for (i in 0 until 10) {
                    tempList.add(MultiBean("$index", "这是内容文件>>>>>>$i", 1))
                }
            }
            tempLinkHashMap["$index"] = tempList
        }
        flatMapData(tempLinkHashMap)
        val nodeAdapter = NodeAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.rv)
        recyclerView.adapter = nodeAdapter
        nodeAdapter.setNewInstance(dataList)
        nodeAdapter.setEmptyView(R.layout.empty_layout)
        nodeAdapter.setOnItemChildClickListener { adapter, view, position ->
            val item = nodeAdapter.getItem(position)
            val itemList = tempLinkHashMap[item.code]
            when (view.id) {
                /**
                 * 删除按钮逻辑
                 */
                R.id.delete -> {
                    itemList?.run {
                        //TODO 查找父节点
                        val parentNodePosition = dataList.indexOfFirst { it.code == item.code }
                        //TODO 删除选中Item
                        itemList.remove(item)
                        adapter.removeAt(position)
                        val itemListSize = itemList.size
                        if (position >= dataList.size) {
                            dataList.indexOfLast { it.code == item.code && it.itemType == NodeAdapter.FOOTER_TYPE }.takeIf { it > -1 }
                                ?.run { adapter.removeAt(this) }
                            if (itemListSize == 0) {
                                adapter.removeAt(parentNodePosition)
                            }
                            return@run
                        }
                        val newItem = dataList[position]
                        /********************************************/
                        //TODO 展开状态
                        if (item.expanded) {
                            if (itemListSize == expandCount) {
                                dataList.indexOfLast { it.code == item.code && it.itemType == NodeAdapter.FOOTER_TYPE }.takeIf { it > -1 }
                                    ?.run { adapter.removeAt(this) }
                            } else if (itemListSize < expandCount) {
                                //查找不到，是脚布局
                                deleteHeadAndFooter(newItem, adapter, position, itemList, parentNodePosition)
                            }
                        } else {
                            //TODO 折叠状态
                            val minusPosition = position - parentNodePosition
                            val deltaPosition = expandCount - minusPosition
                            if (itemListSize > expandCount) {
                                adapter.addData(position + deltaPosition, itemList[minusPosition + deltaPosition - 1])
                            } else if (itemListSize == expandCount) {
                                adapter.addData(position + deltaPosition, itemList[minusPosition + deltaPosition - 1])
                                dataList.indexOfLast { it.code == item.code && it.itemType == NodeAdapter.FOOTER_TYPE }.takeIf { it > -1 }
                                    ?.run { adapter.removeAt(this) }
                            } else {
                                //查找不到，是脚布局
                                deleteHeadAndFooter(newItem, adapter, position, itemList, parentNodePosition)
                            }
                        }
                    }
                }


                R.id.iVSelect -> {
                    val selectedTag = !item.selected
                    val parentNodePosition = dataList.indexOfFirst { it.code == item.code }
                    val itemListSize = itemList?.size ?: 0
                    when (item.itemType) {
                        NodeAdapter.HEADER_TYPE -> {
                            item.selected = selectedTag
                            itemList?.map {
                                if (selectedTag) {
                                    if (!it.selected) selectCount++
                                } else {
                                    if (it.selected) selectCount--
                                }
                                it.selected = selectedTag
                            }
                            adapter.notifyItemRangeChanged(parentNodePosition, if (item.expanded) itemListSize + 1 else 4)
                        }

                        NodeAdapter.CONTENT_TYPE -> {
                            item.selected = selectedTag
                            val allSelected = itemList?.all { it.selected } ?: false
                            dataList[parentNodePosition].selected = allSelected
                            if (selectedTag) selectCount++ else selectCount--
                            adapter.notifyItemChanged(position)
                            adapter.notifyItemChanged(parentNodePosition)
                        }
                    }
                    tvCount.text = "($selectCount)"
                }


                /**
                 * 脚布局折叠展开
                 */
                R.id.footer -> {
                    itemList?.run {
                        when {
                            item.expanded -> collapseItemList(itemList, dataList, item, position, adapter)
                            else -> expandItemList(itemList, dataList, item, position, adapter)
                        }
                    }


                }
            }
        }

        val ivSelectAll = findViewById<ImageView>(R.id.ivAll)
        findViewById<LinearLayoutCompat>(R.id.llAll).setOnClickListener {
            if (selectedAll) {
                changeSelectedState(tempLinkHashMap, false)
                selectCount = 0
            } else {
                selectCount = 0
                changeSelectedState(tempLinkHashMap, selected = true, countPlus = true)
            }
            selectedAll = !selectedAll
            tvCount.text = "($selectCount)"
            ivSelectAll.setImageResource(if (selectedAll) R.drawable.baseline_radio_button_checked_24 else R.drawable.baseline_radio_button_unchecked_24)
            nodeAdapter.notifyDataSetChanged()
        }

        /**
         * 全部删除-按钮操作
         */
        findViewById<LinearLayoutCompat>(R.id.llDel).setOnClickListener {
            tempLinkHashMap.mapValues {
                val value = it.value
                value.removeAll { removeItem -> removeItem.selected }
            }
            flatMapData(tempLinkHashMap)
            switchSelectState(tempLinkHashMap)
            selectCount = 0
            tvCount.text = "(0)"
            nodeAdapter.notifyDataSetChanged()
            findFirstAndLastPosition(recyclerView)
        }

        /**
         * 切换状态按钮动作
         */
        findViewById<View>(R.id.tvSwitch).setOnClickListener {
            switchSelectState(tempLinkHashMap)
            nodeAdapter.notifyDataSetChanged()
        }

        /**
         * 滚动监听
         */
        recyclerView.addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    findFirstAndLastPosition(recyclerView)
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.d("onScrolled", "onScrolled: dx = $dx, dy = $dy")
            }
        })

        /**
         * 动态设置数据
         */
        val editText = findViewById<EditText>(R.id.editCount)
        findViewById<Button>(R.id.button).setOnClickListener {
            val count = editText.text.toString()
            if (count.isNotEmpty()) {
                expandCount = count.toInt()
            }
            tempLinkHashMap.clear()
            for (index in 0 until 5) {
                val tempList = mutableListOf<MultiBean>()
                if (index == 0) {
                    for (i in 0 until 2) {
                        tempList.add(MultiBean("$index", "这是内容文件>>>>>>$i", 1))
                    }
                } else {
                    for (i in 0 until 10) {
                        tempList.add(MultiBean("$index", "这是内容文件>>>>>>$i", 1))
                    }
                }
                tempLinkHashMap["$index"] = tempList
            }
            flatMapData(tempLinkHashMap)
            nodeAdapter.setNewInstance(dataList)
        }


    }

    /**
     * 查找RV的第一个后最后一个对象
     */
    private fun findFirstAndLastPosition(recyclerView: RecyclerView) {
        (recyclerView.layoutManager as LinearLayoutManager).apply {
            val firstVisibleItemPosition = findFirstVisibleItemPosition()
            val lastVisibleItemPosition = findLastVisibleItemPosition()
            Log.d(
                "position",
                "firstVisibleItemPosition: $firstVisibleItemPosition>>>>>>>>>lastVisibleItemPosition:$lastVisibleItemPosition"
            )
        }
    }

    private fun changeSelectedState(tempLinkHashMap: LinkedHashMap<String, MutableList<MultiBean>>, selected: Boolean, countPlus: Boolean = false) {
        tempLinkHashMap.mapValues {
            val value = it.value
            value.forEach { item ->
                item.selected = selected
                if (countPlus) {
                    selectCount++
                }
            }
        }
        dataList.forEach { item ->
            item.selected = selected
        }
    }


    /**
     * 折叠
     */
    private fun collapseItemList(
        itemList: List<MultiBean>,
        dataList: MutableList<MultiBean>,
        item: MultiBean,
        position: Int,
        adapter: BaseQuickAdapter<*, *>
    ) {
        itemList.forEach { it.expanded = false }
        val subList = itemList.subList(expandCount, itemList.size)
        dataList.removeAll(subList)
        dataList.filter { it.code == item.code }.forEach { it.expanded = false }
        item.expanded = false
        adapter.notifyItemChanged(position)
        val notifySize = subList.size
        adapter.notifyItemRangeRemoved(position - notifySize, notifySize)
    }

    /**
     * 展开
     */
    private fun expandItemList(
        itemList: List<MultiBean>,
        dataList: MutableList<MultiBean>,
        item: MultiBean,
        position: Int,
        adapter: BaseQuickAdapter<*, *>
    ) {
        itemList.forEach { it.expanded = true }
        val subtract = itemList.subtract(dataList.toSet())
        dataList.addAll(position, subtract)
        dataList.filter { it.code == item.code }.forEach { it.expanded = true }
        item.expanded = true
        adapter.notifyItemChanged(position)
        adapter.notifyItemRangeInserted(position, subtract.size)
    }


    /**
     * 打开-关闭编辑操作
     */
    private fun switchSelectState(tempLinkHashMap: LinkedHashMap<String, MutableList<MultiBean>>) {
        selectSwitch = !selectSwitch
        dataList.forEach {
            it.showSelect = selectSwitch
            it.canSwipeLayout = !selectSwitch
            if (it.itemType == NodeAdapter.HEADER_TYPE) {
                tempLinkHashMap[it.code]?.forEach { item ->
                    item.showSelect = selectSwitch
                    item.canSwipeLayout = !selectSwitch
                }
            }
        }
    }


    /**
     * 列表数据扁平化处理
     */
    private fun flatMapData(
        tempLinkHashMap: LinkedHashMap<String, MutableList<MultiBean>>,
    ) {
        dataList.clear()
        dataList.addAll(tempLinkHashMap.flatMap { (key, value) ->
            when {
                value.size > expandCount -> listOf(
                    MultiBean(key, "我是头部", NodeAdapter.HEADER_TYPE, expanded = false),
                    *value.take(expandCount).toTypedArray(),
                    MultiBean(key, "我是脚布局", NodeAdapter.FOOTER_TYPE, expanded = false)
                )

                value.isNotEmpty() -> listOf(
                    MultiBean(key, "我是头部", NodeAdapter.HEADER_TYPE, expanded = true),
                    *value.toTypedArray()
                )

                else -> emptyList()
            }
        })
    }

    /**
     * 删除脚布局或者头布局
     */
    private fun deleteHeadAndFooter(
        newItem: MultiBean,
        adapter: BaseQuickAdapter<*, *>,
        position: Int,
        itemList: MutableList<MultiBean>,
        parentNodePosition: Int
    ) {
        when {
            newItem.itemType == NodeAdapter.FOOTER_TYPE -> {
                adapter.removeAt(position)
            }

            itemList.isEmpty() -> {
                adapter.removeAt(parentNodePosition)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AudioPlayer.release()
    }


}

data class MultiBean(
    var code: String,
    var title: String,
    override var itemType: Int,
    var expanded: Boolean = false,
    var selected: Boolean = false,
    var showSelect: Boolean = false,
    var canSwipeLayout: Boolean = true
) : MultiItemEntity


class NodeAdapter : BaseMultiItemQuickAdapter<MultiBean, BaseViewHolder>() {

    companion object {
        const val HEADER_TYPE = 0
        const val CONTENT_TYPE = 1
        const val FOOTER_TYPE = 2
    }

    init {
        addItemType(HEADER_TYPE, R.layout.item_swipe_layout_header)
        addItemType(CONTENT_TYPE, R.layout.item_swipe_layout)
        addItemType(FOOTER_TYPE, R.layout.item_swipe_layout_footer)
        addChildClickViewIds(R.id.delete, R.id.footer, R.id.iVSelect)
    }


    override fun convert(holder: BaseViewHolder, item: MultiBean) {
        when (holder.itemViewType) {
            HEADER_TYPE -> {
                holder.setText(R.id.content, item.title + item.code)
                holder.setGone(R.id.iVSelect, !item.showSelect)
                holder.setImageResource(
                    R.id.iVSelect,
                    if (item.selected) R.drawable.baseline_radio_button_checked_24 else R.drawable.baseline_radio_button_unchecked_24
                )
            }

            CONTENT_TYPE -> {
                holder.setText(R.id.content, "类别:${item.code}     " + item.title)
                holder.setGone(R.id.iVSelect, !item.showSelect)
                holder.setImageResource(
                    R.id.iVSelect,
                    if (item.selected) R.drawable.baseline_radio_button_checked_24 else R.drawable.baseline_radio_button_unchecked_24
                )
                val easySwipeMenuLayout = holder.getView<EasySwipeMenuLayout>(R.id.swipeLayout)
                easySwipeMenuLayout.isCanLeftSwipe = item.canSwipeLayout
            }

            FOOTER_TYPE -> {
                holder.setText(R.id.footer, if (item.expanded) "请收起" else "请展开")
            }
        }
    }
}

fun <T : Any?> deepCopy(list: List<T>): List<T> {
    val result = mutableListOf<T>()
    for (item in list) {
        val newItem = when (item) {
            is List<*> -> deepCopy(item)
            is Array<*> -> item.copyOf().toList()
            else -> item
        }
        @Suppress("UNCHECKED_CAST")
        result.add(newItem as T)
    }
    return result
}




