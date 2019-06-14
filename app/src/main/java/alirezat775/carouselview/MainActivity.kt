package alirezat775.carouselview

import alirezat775.lib.carouselview.Carousel
import alirezat775.lib.carouselview.CarouselView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = SampleAdapter()
        val carousel = Carousel(this, carousel_view, adapter)
        carousel.setOrientation(CarouselView.HORIZONTAL, false)
        carousel.autoScroll(true, 5000, true)
        carousel.scaleView(true)
        carousel.enableSlider(true)

        carousel.add(MyModel(1))
        carousel.add(MyModel(2))
        carousel.add(MyModel(3))
        carousel.add(MyModel(4))
        carousel.add(MyModel(5))
        carousel.add(MyModel(6))
        carousel.add(MyModel(7))
        carousel.add(MyModel(8))
        carousel.add(MyModel(9))
        carousel.add(MyModel(10))


    }
}
