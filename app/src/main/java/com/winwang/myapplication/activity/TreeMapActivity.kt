package com.winwang.myapplication.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.winwang.myapplication.R
import com.winwang.myapplication.view.Item
import com.winwang.myapplication.view.SquarifiedTreeMap
import java.util.Random


class TreeMapActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tree_map)

        val treeMap = findViewById<SquarifiedTreeMap>(R.id.tree_map)
        val items = listOf(
            Item(10, 0),
            Item(20, 0),
            Item(30, 0),
            Item(15, 1),
            Item(25, 1),
            Item(20, 2)
        )
        treeMap.setItems(items)
        treeMap.setColors(getRandomColors(items.size))
        treeMap.setItemClickListener { item ->
            Toast.makeText(this, "Clicked item: ${item.value}", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getRandomColors(count: Int): List<Int> {
        val colors = mutableListOf<Int>()
        val random = Random()
        for (i in 0 until count) {
            colors.add(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)))
        }
        return colors
    }
}

