package alirezat775.lib.carouselview

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import androidx.annotation.IntDef
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy


/**
 * Author:  Alireza Tizfahm Fard
 * Date:    2019-06-14
 * Email:   alirezat775@gmail.com
 */

class CarouselView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    companion object {
        //carousel orientation
        const val HORIZONTAL = 0
        const val VERTICAL = 1
    }

    private var listener: CarouselListener? = null
    private var velocityTracker: VelocityTracker? = null
    private var currentPosition: Int = 0
    private var actionDown = true
    private var autoScroll = false
    private var loopMode: Boolean = false
    private var delayMillis: Long = 5000
    private var reverseLoop = true
    private var scheduler: Scheduler? = null
    private var scrolling = true

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        if (adapter?.itemCount!! >= 0)
            initSnap()
    }

    private fun initSnap() {
        clipToPadding = false
        overScrollMode = View.OVER_SCROLL_NEVER
        addOnItemTouchListener(onItemTouchListener())
        post {
            scrolling(0)
            if (isAutoScroll()) {
                getScheduler()
            }
        }
    }

    /**
     * @param positionPlus scroll to new position from previous position
     */
    fun scrolling(positionPlus: Int) {
        if (calculateSnapViewPosition() > -1) {
            var centerViewPosition = calculateSnapViewPosition() + positionPlus
            if (centerViewPosition <= 0)
                centerViewPosition = 0
            else if (centerViewPosition >= adapter!!.itemCount - 1)
                centerViewPosition = adapter!!.itemCount - 1

            smoothScrollToPosition(centerViewPosition)
            getListener()?.onPositionChange(centerViewPosition)
            currentPosition(centerViewPosition)
        }
    }

    fun addListener(listener: CarouselListener) {
        this.listener = listener
    }

    private fun getListener(): CarouselListener? {
        return listener
    }

    /**
     * @return calculate snapping position relation anchor
     */
    private fun calculateSnapViewPosition(): Int {
        val parentAnchor = getParentAnchor()
        val lastVisibleItemPosition = getManager().findLastVisibleItemPosition()
        val firstVisibleItemPosition = getManager().findFirstVisibleItemPosition()

        if (firstVisibleItemPosition > -1) {
            val currentViewClosestToAnchor = getManager().findViewByPosition(firstVisibleItemPosition)
            var currentViewClosestToAnchorDistance = parentAnchor - getViewAnchor(currentViewClosestToAnchor)
            var currentViewClosestToAnchorPosition = firstVisibleItemPosition

            for (i in firstVisibleItemPosition + 1..lastVisibleItemPosition) {
                val view = getManager().findViewByPosition(i)
                val distanceToCenter = parentAnchor - getViewAnchor(view)
                if (Math.abs(distanceToCenter) < Math.abs(currentViewClosestToAnchorDistance)) {
                    currentViewClosestToAnchorPosition = i
                    currentViewClosestToAnchorDistance = distanceToCenter
                }
            }
            return currentViewClosestToAnchorPosition
        } else {
            return -1
        }
    }

    /**
     * @return position fit in screen for parent list
     */
    private fun getParentAnchor(): Int {
        return (if (getManager().orientation == VERTICAL) height else width) / 2
    }

    /**
     * @param view item view
     * @return position fit in screen specific view in parent
     */
    private fun getViewAnchor(view: View?): Int {
        return if (getManager().orientation == VERTICAL) view?.top!! + view.height / 2
        else view?.left!! + view.width / 2
    }


    /**
     * @return scheduler scroll item
     */
    private fun getScheduler(): Scheduler? {
        if (scheduler == null) {
            scheduler = Scheduler(getDelayMillis(), 1)
        }
        return scheduler
    }

    /**
     * pause auto scroll
     */
    fun pauseAutoScroll() {
        getScheduler()?.cancel()
        scrolling = false
    }

    /**
     * resume auto scroll
     */
    fun resumeAutoScroll() {
        getScheduler()?.start()
        scrolling = true
    }

    /**
     * @return support RTL view
     */
    private fun isRTL(): Boolean {
        return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL
    }

    /**
     * @return support RTL view
     */
    private fun isTrustLayout(): Boolean {
        if (isRTL() && getManager().reverseLayout) {
            return true
        } else if (!isRTL() && getManager().reverseLayout) {
            return false
        } else if (isRTL() && !getManager().reverseLayout) {
            return false
        } else if (!isRTL() && !getManager().reverseLayout) {
            return true
        }
        return false
    }

    /**
     * @return current item position
     */
    fun getCurrentPosition(): Int {
        return currentPosition
    }

    /**
     * @param currentPosition go to specific position
     */
    fun currentPosition(currentPosition: Int) {
        this.currentPosition = currentPosition
    }

    /**
     * @return layoutManager
     */
    fun getManager(): CarouselLayoutManager {
        return (layoutManager as CarouselLayoutManager?)!!
    }

    /**
     * @return enable/disable auto scrolling
     */
    private fun isAutoScroll(): Boolean {
        return autoScroll
    }

    /**
     * @param autoScroll enable/disable auto scrolling
     */
    fun autoScroll(autoScroll: Boolean) {
        this.autoScroll = autoScroll
    }

    /**
     * @return change position with delay time
     */
    private fun getDelayMillis(): Long {
        return delayMillis
    }

    /**
     * @param delayMillis for change position
     */
    fun delayMillis(delayMillis: Long) {
        this.delayMillis = delayMillis
    }

    /**
     * @return loop mode scrolling
     */
    fun isLoopMode(): Boolean {
        return loopMode
    }

    /**
     * @param loopMode
     */
    fun loopMode(loopMode: Boolean) {
        this.loopMode = loopMode
    }


    /**
     * @return onItemTouchListener for calculate velocity and position fix view center
     */
    private fun onItemTouchListener(): OnItemTouchListener {
        return object : OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val action = e.actionMasked
                when (action) {
                    MotionEvent.ACTION_MOVE -> if (actionDown) {
                        actionDown = false
                        if (velocityTracker == null) {
                            velocityTracker = VelocityTracker.obtain()
                        } else {
                            velocityTracker?.clear()
                        }
                        velocityTracker?.addMovement(e)
                    } else {
                        velocityTracker?.addMovement(e)
                        velocityTracker?.computeCurrentVelocity(1000)
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        actionDown = true

                        when (getManager().orientation) {
                            HORIZONTAL -> if (velocityTracker?.xVelocity!! <= 0) {
                                if (!isTrustLayout())
                                    scrolling(-1)// rtl or reverse mode
                                else
                                    scrolling(1)//scroll to right
                            } else if (velocityTracker?.xVelocity!! > 0) {
                                if (!isTrustLayout())
                                    scrolling(1)// rtl or reverse mode
                                else
                                    scrolling(-1)//scroll to left
                            }
                            VERTICAL -> if (velocityTracker?.yVelocity!! <= 0) {
                                if (getManager().reverseLayout)
                                    scrolling(-1)// rtl or reverse mode
                                else
                                    scrolling(1)//scroll to up
                            } else if (velocityTracker?.yVelocity!! > 0) {
                                if (getManager().reverseLayout)
                                    scrolling(1)// rtl or reverse mode
                                else
                                    scrolling(-1)//scroll to down
                            }
                        }
                        velocityTracker?.recycle()
                        velocityTracker = null

                    }
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

            }
        }
    }

    inner class Scheduler(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {

        override fun onTick(millisUntilFinished: Long) {
        }

        override fun onFinish() {
            if (isLoopMode()) {
                if (reverseLoop)
                    scrolling(+1)
                else
                    scrolling(-1)

            } else {
                if (getCurrentPosition() >= adapter!!.itemCount - 1)
                    scrollToPosition(0)
            }
            cancel()
            if (scrolling) start()
        }
    }


    interface CarouselListener {

        /**
         * @param position current position
         */
        fun onPositionChange(position: Int)

        /**
         * @param dx delta x
         * @param dy delta y
         */
        fun onScroll(dx: Int, dy: Int)
    }


    @IntDef(VERTICAL, HORIZONTAL)
    @Retention(RetentionPolicy.SOURCE)
    annotation class CarouselOrientation
}