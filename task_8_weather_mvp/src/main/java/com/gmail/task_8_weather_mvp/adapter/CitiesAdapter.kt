package com.gmail.task_8_weather_mvp.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gmail.task_8_weather_mvp.App
import com.gmail.task_8_weather_mvp.R
import com.gmail.task_8_weather_mvp.SelectCityActivity
import com.gmail.task_8_weather_mvp.presenter.viewmodel.CityViewModel


class CitiesAdapter(private val app: App, private val selectCityActivity: SelectCityActivity) :
    RecyclerView.Adapter<CitiesAdapter.CityViewHolder>() {
    private var list: MutableList<CityViewModel> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CityViewHolder(app, inflater, parent, selectCityActivity as Activity)
    }

    fun putAllCityList(list: MutableList<CityViewModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount - position);
    }

    fun insertItem(position: Int, city: CityViewModel) {
        list.add(position, city)
        notifyItemInserted(position)
    }

    fun itemEdited(position: Int, city: CityViewModel) {
        list.removeAt(position)
        list.add(position, city)
        notifyItemChanged(position, city)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city: CityViewModel = list[position]
        holder.bind(city, position)
    }

    class CityViewHolder(
        val app: App,
        inflater: LayoutInflater,
        parent: ViewGroup,
        private val selectCityActivity: Activity
    ) : RecyclerView.ViewHolder(
        inflater.inflate(R.layout.item_city, parent, false)
    ) {
        private val dataBase = app.dataBase
        private var isChecked = itemView.findViewById<ImageView>(R.id.isChecked)
        private var cityName = itemView.findViewById<TextView>(R.id.cityName)

        @SuppressLint("SetTextI18n")
        fun bind(city: CityViewModel, position: Int) {
            setVisibility(city.checked)
            cityName.text = city.en
            itemView.setOnClickListener {
                val cityList = dataBase.cityDAO().getAll()
                val oldActiveCity = cityList.find { item -> item.checked }.apply { this?.checked = false }
                val newActiveCity = cityList.find { item -> item.uid == city.uid }.apply { this?.checked = true }
                if (newActiveCity != null && oldActiveCity != null) {
                    dataBase.cityDAO().update(oldActiveCity)
                    dataBase.cityDAO().update(newActiveCity)
                    setVisibility(newActiveCity.checked)
                }
                selectCityActivity.setResult(Activity.RESULT_OK)
                selectCityActivity.finish()
            }
        }

        private fun setVisibility(checked: Boolean) {
            if (checked) {
                isChecked.visibility = ImageView.VISIBLE
            } else {
                isChecked.visibility = ImageView.INVISIBLE
            }
        }
    }
}