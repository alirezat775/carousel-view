package alirezat775.carouselview

import alirezat775.lib.carouselview.CarouselAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_carousel.view.*

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    2019-06-14
 * Email:   alirezat775@gmail.com
 */

class SampleAdapter : CarouselAdapter() {

    private var vh: MyViewHolder? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.item_carousel, parent, false)
        vh = MyViewHolder(v)
        return vh!!
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        vh = holder as MyViewHolder
        val model = getItems()[position] as MyModel
        vh?.title?.text = model.getId().toString()
    }

    inner class MyViewHolder(itemView: View) : CarouselViewHolder(itemView) {

        var title: TextView = itemView.item_text

    }
}