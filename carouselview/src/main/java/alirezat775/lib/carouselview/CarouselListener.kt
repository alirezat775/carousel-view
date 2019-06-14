package alirezat775.lib.carouselview

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    12/22/17
 * Email:   alirezat775@gmail.com
 */
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
