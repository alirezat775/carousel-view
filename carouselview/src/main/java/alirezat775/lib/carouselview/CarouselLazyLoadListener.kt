package alirezat775.lib.carouselview

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    12/22/17
 * Email:   alirezat775@gmail.com
 */

interface CarouselLazyLoadListener {

    fun onLoadMore(page: Int, totalItemsCount: Int, view: CarouselView)

}
