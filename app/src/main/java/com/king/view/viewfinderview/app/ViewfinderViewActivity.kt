/*
 * Copyright (C) Jenly
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.king.view.viewfinderview.app

import android.graphics.Point
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.king.view.viewfinderview.ViewfinderView
import com.king.view.viewfinderview.ViewfinderView.ViewfinderStyle

/**
 * ViewfinderView example
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
class ViewfinderViewActivity : AppCompatActivity() {

    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutId = intent.getIntExtra(MainActivity.LAYOUT_ID, 0)
        setContentView(layoutId)
        val viewfinderView = findViewById<ViewfinderView>(R.id.viewfinderView)

        when (intent.getIntExtra(MainActivity.EXAMPLE_TYPE, ExampleType.VIEWFINDER_CLASSIC_STYLE)) {
            ExampleType.VIEWFINDER_CLASSIC_STYLE -> {
                viewfinderView.setViewfinderStyle(ViewfinderStyle.CLASSIC)
            }
            ExampleType.VIEWFINDER_POPULAR_STYLE -> {
                viewfinderView.setViewfinderStyle(ViewfinderStyle.POPULAR)
            }
            ExampleType.SHOW_RESULT_POINT -> {
                // 显示结果点
                viewfinderView.showResultPoints(listOf(Point(400, 750), Point(650, 750)))
                // 结果点Item点击监听
                viewfinderView.setOnItemClickListener {
                    showToast("item: $it")
                }
            }
            ExampleType.SHOW_CUSTOM_RESULT_POINT -> {
                viewfinderView.setPointDrawable(R.drawable.ic_result_point)
                // 显示结果点
                viewfinderView.showResultPoints(listOf(Point(400, 750), Point(650, 750)))
                // 结果点Item点击监听
                viewfinderView.setOnItemClickListener {
                    showToast("item: $it")
                }
            }
        }
    }

    private fun showToast(text: String) {
        toast?.cancel()
        toast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
        toast?.show()
    }
}