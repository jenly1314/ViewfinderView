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

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.king.view.viewfinderview.ViewfinderView.ViewfinderStyle
/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun startPreview(
        @LayoutRes layoutId: Int,
        @ExampleType exampleType: Int,
    ) {
        val intent = Intent(this, ViewfinderViewActivity::class.java)
        intent.apply {
            putExtra(LAYOUT_ID, layoutId)
            putExtra(EXAMPLE_TYPE, exampleType)
        }
        startActivity(intent)
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.btnLineLaserStyle -> {
                startPreview(
                    R.layout.activity_line_style_viewfinder,
                    ExampleType.VIEWFINDER_CLASSIC_STYLE
                )
            }
            R.id.btnGridLaserStyle1 -> {
                startPreview(
                    R.layout.activity_grid_style_viewfinder,
                    ExampleType.VIEWFINDER_CLASSIC_STYLE
                )
            }
            R.id.btnGridLaserStyle2 -> {
                startPreview(
                    R.layout.activity_full_grid_style_viewfinder,
                    ExampleType.VIEWFINDER_CLASSIC_STYLE
                )
            }
            R.id.btnImageLaserStyle -> {
                startPreview(
                    R.layout.activity_image_style_viewfinder,
                    ExampleType.VIEWFINDER_CLASSIC_STYLE
                )
            }
            R.id.btnPopularViewfinderStyle -> {
                startPreview(
                    R.layout.activity_image_style_viewfinder,
                    ExampleType.VIEWFINDER_POPULAR_STYLE
                )
            }
            R.id.btnShowPoints -> {
                startPreview(R.layout.activity_line_style_viewfinder, ExampleType.SHOW_RESULT_POINT)
            }
            R.id.btnCustomShowPoints -> {
                startPreview(
                    R.layout.activity_line_style_viewfinder,
                    ExampleType.SHOW_CUSTOM_RESULT_POINT
                )
            }
        }
    }

    companion object {
        const val LAYOUT_ID = "layoutId"
        const val EXAMPLE_TYPE = "exampleType"
    }
}