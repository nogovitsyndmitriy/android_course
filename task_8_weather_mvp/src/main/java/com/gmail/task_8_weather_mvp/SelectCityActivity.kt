package com.gmail.task_8_weather_mvp

import android.app.Activity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.task_8_weather_mvp.adapter.CitiesAdapter
import com.gmail.task_8_weather_mvp.presenter.CityPresenter
import com.gmail.task_8_weather_mvp.presenter.CityPresenterImpl
import com.gmail.task_8_weather_mvp.presenter.viewmodel.CityViewModel
import com.gmail.task_8_weather_mvp.view.CitiesView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SelectCityActivity : AppCompatActivity(), CitiesView {

    private lateinit var backButtonCities: ImageButton
    private lateinit var addCityButton: FloatingActionButton
    private lateinit var citiesRecyclerView: RecyclerView
    private val cityAdapter by lazy { CitiesAdapter(applicationContext as App, this) }
    private lateinit var cityPresenter: CityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cities)
        initViews()
        cityPresenter = CityPresenterImpl(this, applicationContext as App)
        cityPresenter.getCities()
        citiesRecyclerView.apply {
            adapter = cityAdapter
            layoutManager = LinearLayoutManager(this@SelectCityActivity, RecyclerView.VERTICAL, false)
        }
        backButtonCities.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
        addCityButton.setOnClickListener {
            showDialog()
        }
    }

    private fun initViews() {
        backButtonCities = findViewById(R.id.backButtonCities)
        addCityButton = findViewById(R.id.addCityButton)
        citiesRecyclerView = findViewById(R.id.citiesRecyclerView)
    }

    private fun showDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_add_city, null)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Enter the city name")
            .setView(view)
            .setPositiveButton(R.string.ok) { _, _ ->
                val cityNameDialog = view.findViewById<EditText>(R.id.cityNameDialog)
                cityPresenter.addCity(cityNameDialog.text.toString())
            }
            .create()
        dialog.show()
    }

    override fun fillPage(cityViewModelList: MutableList<CityViewModel>) {
        cityAdapter.putAllCityList(cityViewModelList)
    }

    override fun addNewCity(city: CityViewModel) {
        cityAdapter.insertItem(cityAdapter.itemCount, city)
    }
}