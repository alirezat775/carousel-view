package alirezat775.carouselview

import alirezat775.lib.carouselview.CarouselModel

class MyModel constructor(private var id: Int) : CarouselModel() {

    fun getId(): Int {
        return id
    }
}
