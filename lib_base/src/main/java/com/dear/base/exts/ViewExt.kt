package com.dear.base.exts

import android.animation.Animator
import android.animation.IntEvaluator
import android.animation.ValueAnimator
import android.os.SystemClock
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.dear.base.R

/**
 * View 的点击事件
 */
inline fun <reified T : View> T.click(crossinline block: (T) -> Unit) = setOnClickListener {
    block(it as T)
}

/**
 * View 的长点击事件
 */
inline fun <reified T : View> T.longClick(crossinline block: (T) -> Boolean) =
    setOnLongClickListener {
        block(it as T)
    }

/**
 * 点击事件-防止快速点击
 */
fun View.onClick(wait: Long = 200, block: ((View) -> Unit)) {
    setOnClickListener(throttleClick(wait, block))
}

fun throttleClick(wait: Long = 200, block: ((View) -> Unit)): View.OnClickListener {
    return View.OnClickListener {
        val currentTime = SystemClock.uptimeMillis()
        val lastTime = (it.getTag(R.id.click_time_stamp) as? Long) ?: 0
        if ((currentTime - lastTime) > wait) {
            it.setTag(R.id.click_time_stamp, currentTime)
            block(it)
        }
    }
}

/**
 * View 点击防抖
 */
fun View.onDebounceClick(wait: Long = 200, block: ((View) -> Unit)) {
    setOnClickListener(debounceClick(wait, block))
}

fun debounceClick(wait: Long, block: (View) -> Unit): View.OnClickListener {
    return View.OnClickListener {
        var action = (it.getTag(R.id.click_debounce_action) as? DebounceAction) ?: null
        if (action == null) {
            action = DebounceAction(it, block)
            it.setTag(R.id.click_debounce_action, action)
        } else {
            action.block = block
        }
        it.removeCallbacks(action)
        it.postDelayed(action, wait)
    }
}

class DebounceAction(val view: View, var block: (View) -> Unit) : Runnable {
    override fun run() {
        if (view.isAttachedToWindow) {
            block(view)
        }
    }
}

fun View.margin(
    left: Int = -1,
    top: Int = -1,
    right: Int = -1,
    bottom: Int = -1
): View {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    if (left != -1) {
        params.leftMargin = left
    }
    if (top != -1) {
        params.topMargin = top
    }
    if (right != -1) {
        params.rightMargin = right
    }
    if (bottom != -1) {
        params.bottomMargin = bottom
    }
    layoutParams = params
    return this
}

/**
 * 设置宽度，带有过渡动画
 * @param targetValue 目标宽度
 * @param duration 时长
 * @param action 可选行为
 */
fun View.animateWidth(
    targetValue: Int, duration: Long = 400, listener: Animator.AnimatorListener? = null,
    action: ((Float) -> Unit)? = null
) {
    post {
        ValueAnimator.ofInt(width, targetValue).apply {
            addUpdateListener {
                width(it.animatedValue as Int)
                action?.invoke((it.animatedFraction))
            }
            if (listener != null) addListener(listener)
            setDuration(duration)
            start()
        }
    }
}

/**
 * 设置高度，带有过渡动画
 * @param targetValue 目标高度
 * @param duration 时长
 * @param action 可选行为
 */
fun View.animateHeight(
    targetValue: Int,
    duration: Long = 400,
    listener: Animator.AnimatorListener? = null,
    action: ((Float) -> Unit)? = null
) {
    post {
        ValueAnimator.ofInt(height, targetValue).apply {
            addUpdateListener {
                height(it.animatedValue as Int)
                action?.invoke((it.animatedFraction))
            }
            if (listener != null) addListener(listener)
            setDuration(duration)
            start()
        }
    }
}

/**
 * 设置View的宽度
 */
fun View.width(width: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    params.width = width
    layoutParams = params
    return this
}

/**
 * 设置View的高度
 */
fun View.height(height: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    params.height = height
    layoutParams = params
    return this
}

/**
 * 设置宽度和高度，带有过渡动画
 * @param targetWidth 目标宽度
 * @param targetHeight 目标高度
 * @param duration 时长
 * @param action 可选行为
 */
fun View.animateWidthAndHeight(
    targetWidth: Int,
    targetHeight: Int,
    duration: Long = 400,
    listener: Animator.AnimatorListener? = null,
    action: ((Float) -> Unit)? = null
) {
    post {
        val startHeight = height
        val evaluator = IntEvaluator()
        ValueAnimator.ofInt(width, targetWidth).apply {
            addUpdateListener {
                widthAndHeight(
                    it.animatedValue as Int,
                    evaluator.evaluate(it.animatedFraction, startHeight, targetHeight)
                )
                action?.invoke((it.animatedFraction))
            }
            if (listener != null) addListener(listener)
            setDuration(duration)
            start()
        }
    }
}

/**
 * 设置View的宽度和高度
 * @param width 要设置的宽度
 * @param height 要设置的高度
 */
fun View.widthAndHeight(width: Int, height: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    params.width = width
    params.height = height
    layoutParams = params
    return this
}

/*** 可见性相关 ****/
fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

val View.isGone: Boolean
    get() {
        return visibility == View.GONE
    }

val View.isVisible: Boolean
    get() {
        return visibility == View.VISIBLE
    }

val View.isInvisible: Boolean
    get() {
        return visibility == View.INVISIBLE
    }

/**
 * 切换View的可见性
 */
fun View.toggleVisibility() {
    visibility = if (visibility == View.GONE) View.VISIBLE else View.GONE
}

/**
 * 设置View高度，限制在min和max范围之内
 * @param h
 * @param min 最小高度
 * @param max 最大高度
 */
fun View.limitHeight(h: Int, min: Int, max: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    when {
        h < min -> params.height = min
        h > max -> params.height = max
        else -> params.height = h
    }
    layoutParams = params
    return this
}

/**
 * 设置View宽度，限制在min和max范围之内
 * @param w
 * @param min 最小宽度
 * @param max 最大宽度
 */
fun View.limitWidth(w: Int, min: Int, max: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    when {
        w < min -> params.width = min
        w > max -> params.width = max
        else -> params.width = w
    }
    layoutParams = params
    return this
}

/**
 * 设置View的margin
 * @param startMargin 默认保留原来的
 * @param topMargin 默认是保留原来的
 * @param endMargin 默认是保留原来的
 * @param bottomMargin 默认是保留原来的
 * @param rtl
 */
fun View.margin(
    startMargin: Int = Int.MAX_VALUE,
    topMargin: Int = Int.MAX_VALUE,
    endMargin: Int = Int.MAX_VALUE,
    bottomMargin: Int = Int.MAX_VALUE,
    supportRTL: Boolean = true
): View {
    val params = layoutParams as? ViewGroup.MarginLayoutParams
    if (startMargin != Int.MAX_VALUE) {
        if (supportRTL)
            params?.marginStart = startMargin
        else
            params?.leftMargin = startMargin
    }
    if (topMargin != Int.MAX_VALUE)
        params?.topMargin = topMargin
    if (endMargin != Int.MAX_VALUE) {
        if (supportRTL)
            params?.marginEnd = endMargin
        else
            params?.rightMargin = endMargin
    }
    if (bottomMargin != Int.MAX_VALUE)
        params?.bottomMargin = bottomMargin
    params?.let { layoutParams = it }
    return this
}

val View.layoutGravity: Int
    get() = when (val lp = layoutParams) {
        is FrameLayout.LayoutParams -> lp.gravity
        is LinearLayout.LayoutParams -> lp.gravity
        else -> Gravity.NO_GRAVITY
    }

fun View.layoutGravity(gravity: Int) {
    when (val lp = layoutParams) {
        is FrameLayout.LayoutParams -> lp.gravity = gravity
        is LinearLayout.LayoutParams -> lp.gravity = gravity
        else -> return
    }
    invalidate()
}

// 所有子View
inline val ViewGroup.children
    get() = (0 until childCount).map { getChildAt(it) }



