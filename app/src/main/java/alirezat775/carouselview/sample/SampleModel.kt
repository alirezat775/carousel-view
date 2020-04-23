package alirezat775.carouselview.sample

import alirezat775.lib.carouselview.CarouselModel

class SampleModel constructor(private var id: Int) : CarouselModel() {

    fun getId(): Int {
        return id
    }
}
