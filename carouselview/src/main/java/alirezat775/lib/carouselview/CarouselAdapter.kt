package alirezat775.lib.carouselview

import alirezat775.lib.carouselview.helper.ViewHelper
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    2019-06-14
 * Email:   alirezat775@gmail.com
 */

abstract class CarouselAdapter : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    companion object {
        const val REMOVE = 1
        const val ADD = 2
    }

    private lateinit var recyclerView: RecyclerView
    private var enableSlider = false
    private var items: MutableList<CarouselModel> = ArrayList()

    private fun imageOption(view: View) {
        view.layoutParams.width = Math.round(ViewHelper.getScreenWidth().toDouble()).toInt()
        view.layoutParams.height = Math.round(view.layoutParams.width * 0.70).toInt()
        view.requestLayout()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    /**
     * @return list items
     */
    fun getItems(): MutableList<CarouselModel> {
        return items
    }

    /**
     * @param item      instance CarouselModel
     * @param operation action add or remove
     */
    fun operation(item: CarouselModel, operation: Int) {
        when (operation) {
            ADD -> add(item)
            REMOVE -> remove(item)
        }
    }

    fun addAll(items: MutableList<CarouselModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    /**
     * add item to list and notifyDataSetChanged adapter
     *
     * @param item instance CarouselModel
     */
    private fun add(item: CarouselModel) {
        recyclerView.post {
            notifyItemInserted(itemCount - 1)
            getItems().add(item)
        }
    }

    /**
     * remove item from list and notifyDataSetChanged adapter
     *
     * @param item instance CarouselModel
     */
    private fun remove(item: CarouselModel) {
        notifyItemRemoved(getItems().indexOf(item))
        getItems().remove(item)
    }

    private fun isEnableSlider(): Boolean {
        return enableSlider
    }

    fun enableSlider(enableSlider: Boolean) {
        this.enableSlider = enableSlider
    }

    open inner class CarouselViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            if (isEnableSlider()) imageOption(itemView)
        }
    }
}