package com.github.widgets.itemdecorators

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.github.utils.simpleName
import kotlin.math.roundToInt

/**
 * Copied [DividerItemDecoration] from the framework and modified it a bit.
 */
class IndicesSkippingDividerItemDecoration(context: Context, orientation: Int) :
    ItemDecoration() {

    companion object {
        const val HORIZONTAL = LinearLayout.HORIZONTAL
        const val VERTICAL = LinearLayout.VERTICAL
        private val ATTRS = intArrayOf(R.attr.listDivider)
    }

    private var divider: Drawable?

    /**
     * Current orientation. Either [.HORIZONTAL] or [.VERTICAL].
     */
    private var mOrientation = 0
    private val mBounds = Rect()

    private val skipIndices = mutableSetOf<Int>()

    /**
     * Creates a divider [RecyclerView.ItemDecoration] that can be used with a
     * [LinearLayoutManager].
     *
     * @param context Current context, it will be used to access resources.
     * @param orientation Divider orientation. Should be [.HORIZONTAL] or [.VERTICAL].
     */
    init {
        val a = context.obtainStyledAttributes(ATTRS)
        divider = a.getDrawable(0)
        if (divider == null) {
            Log.w(
                simpleName,
                "@android:attr/listDivider was not set in the theme used for this "
                        + "SparseDividerItemDecoration. Please set that attribute all call setDrawable()"
            )
        }
        a.recycle()
        setOrientation(orientation)
    }

    constructor(context: Context, orientation: Int, divider: Drawable) : this(
        context,
        orientation
    ) {
        setDivider(divider)
    }

    fun setSkipIndices(skipIndices: Set<Int>) {
        this.skipIndices.clear()
        this.skipIndices.addAll(skipIndices)
    }

    /**
     * Sets the [Drawable] for this divider.
     *
     * @param divider Drawable that should be used as a divider.
     */
    fun setDivider(divider: Drawable) {
        this.divider = divider
    }

    /**
     * Sets the orientation for this divider. This should be called if
     * [RecyclerView.LayoutManager] changes orientation.
     *
     * @param orientation [.HORIZONTAL] or [.VERTICAL]
     */
    fun setOrientation(orientation: Int) {
        require(!(orientation != HORIZONTAL && orientation != VERTICAL)) { "Invalid orientation. It should be either HORIZONTAL or VERTICAL" }
        mOrientation = orientation
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        divider?.also { divider ->
            if (mOrientation == VERTICAL) {
                drawVertical(c, parent, divider)
            } else {
                drawHorizontal(c, parent, divider)
            }
        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView, divider: Drawable) {
        canvas.save()
        val left: Int
        val right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(
                left, parent.paddingTop, right,
                parent.height - parent.paddingBottom
            )
        } else {
            left = 0
            right = parent.width
        }
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val adapterPosition = parent.getChildAdapterPosition(child)

            if (!skipIndices.contains(adapterPosition)) {
                parent.getDecoratedBoundsWithMargins(child, mBounds)
                val bottom = mBounds.bottom + child.translationY.roundToInt()
                val top = bottom - divider.intrinsicHeight
                divider.setBounds(left, top, right, bottom)
                divider.draw(canvas)
            }
        }
        canvas.restore()
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView, divider: Drawable) {
        canvas.save()
        val top: Int
        val bottom: Int
        if (parent.clipToPadding) {
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
            canvas.clipRect(
                parent.paddingLeft, top,
                parent.width - parent.paddingRight, bottom
            )
        } else {
            top = 0
            bottom = parent.height
        }
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val adapterPosition = parent.getChildAdapterPosition(child)

            if (!skipIndices.contains(adapterPosition)) {
                parent.getDecoratedBoundsWithMargins(child, mBounds)
                val right = mBounds.right + Math.round(child.translationX)
                val left = right - divider.intrinsicWidth
                divider.setBounds(left, top, right, bottom)
                divider.draw(canvas)
            }
        }
        canvas.restore()
    }

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val adapterPosition = parent.getChildAdapterPosition(view)


        divider?.also { divider ->
            if (!skipIndices.contains(adapterPosition)) {
                if (mOrientation == VERTICAL) {
                    outRect[0, 0, 0] = divider.intrinsicHeight
                } else {
                    outRect[0, 0, divider.intrinsicWidth] = 0
                }
            } else {
                outRect[0, 0, 0] = 0
            }
        } ?: run {
            outRect[0, 0, 0] = 0
        }

    }
}
