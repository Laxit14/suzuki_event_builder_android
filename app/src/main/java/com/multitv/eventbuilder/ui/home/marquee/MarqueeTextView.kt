package com.multitv.eventbuilder.ui.home.marquee

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class MarqueeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    override fun isFocused(): Boolean {
        // Always return true to enable marquee
        return true
    }

    fun setMarqueeText(originalText: String) {
        post {
            val width = paint.measureText(originalText)
            val viewWidth = width.toInt()

            // If text is short, repeat it programmatically
            if (width < this.width) {
                val builder = StringBuilder()
                while (builder.length < 100) { // Just ensure it's long enough
                    builder.append("     ").append(originalText)
                }
                text = builder.toString()
            } else {
                text = originalText
            }
            isSelected = true
        }
    }
}
