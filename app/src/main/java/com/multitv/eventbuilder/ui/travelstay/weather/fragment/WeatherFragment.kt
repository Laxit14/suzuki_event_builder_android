package com.multitv.eventbuilder.ui.travelstay.weather.fragment

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.constant.AppConstant
import com.multitv.eventbuilder.databinding.FragmentWeatherBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.ui.travelstay.weather.adaptor.WeatherPagerAdapter
import com.multitv.eventbuilder.model.weathermodel.model.ForecastItem
import com.multitv.eventbuilder.model.weathermodel.model.WeatherResponse
import com.multitv.eventbuilder.ui.travelstay.weather.viewmodel.WeatherViewModel
import com.multitv.eventbuilder.ui.travelstay.weather.viewmodelfactory.WeatherViewModelFactory
import java.util.Locale

class WeatherFragment  : Fragment(), View.OnClickListener{

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var viewModel : WeatherViewModel

    lateinit var weatherAdapter: WeatherPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(),R.id.nav_host)
        binding.backArrow.setOnClickListener(this)

        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = WeatherViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[WeatherViewModel::class.java]

        viewModel.weatherLiveData.observe(viewLifecycleOwner) { response ->
            setData(response)
        }
        getDataFromApi()
//        setupRecyclerView()
        /*updateTemperature(33)*/
    }
    private fun getDataFromApi() {
        viewModel.fetchWeather(AppConstant.WEATHERAPI)
    }

    private fun setData(response: Generic<WeatherResponse>) {
        when (response) {
            is Generic.Loading -> {
                ProgressDialog.showProgresssDialog(requireContext())
            }

            is Generic.Success -> {
                ProgressDialog.hideProgressDialog()
                val foreCastList = response.data.forecast

                binding.title.text = response.data.pageTittle.toString().trim()

                var selectedIndex = 0 // <--- Track selected index manually

                weatherAdapter = WeatherPagerAdapter(foreCastList) { selectedItem ->
                    updateWeatherDetails(selectedItem) // when user taps any item
                }

                binding.weatherViewPager.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.weatherViewPager.adapter = weatherAdapter

                // Initial selection
                updateWeatherDetails(foreCastList[selectedIndex])
                binding.weatherViewPager.scrollToPosition(selectedIndex)

                // ✅ NEXT button
                binding.btnNext.setOnClickListener {
                    val maxIndex = foreCastList.size - 1
                    if (selectedIndex < maxIndex) {
                        selectedIndex++
                        binding.weatherViewPager.smoothScrollToPosition(selectedIndex)
                        updateWeatherDetails(foreCastList[selectedIndex])
                        weatherAdapter.setSelectedPosition(selectedIndex)
                    }
                }

                // ✅ PREV button
                binding.btnPrev.setOnClickListener {
                    if (selectedIndex > 0) {
                        selectedIndex--
                        binding.weatherViewPager.smoothScrollToPosition(selectedIndex)
                        updateWeatherDetails(foreCastList[selectedIndex])
                        weatherAdapter.setSelectedPosition(selectedIndex)
                    }
                }

            }


            is Generic.Error -> {
                ProgressDialog.hideProgressDialog()
                Toast.makeText(requireContext(), "Error: ${response.message}", Toast.LENGTH_SHORT).show()
            }

            is Generic.Failure -> {
                ProgressDialog.hideProgressDialog()
                Toast.makeText(requireContext(), "Failure: ${response.exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun formatToFullDayAndShortMonth(dateStr: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val outputFormat = SimpleDateFormat("EEEE, MMM d", Locale.ENGLISH) // e.g., Wednesday, Apr 14
            val date = inputFormat.parse(dateStr)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            dateStr // fallback in case of error
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.backArrow -> {
                navController.navigateUp()
            }
        }
    }

    private fun updateWeatherDetails(item: ForecastItem) {
        binding.weatherType.text = item.condition
        binding.humidity.text = "Humidity ${item.humidity}"
        binding.wind.text = "Wind Kmp ${item.wind_kph}"
        binding.weatherTemp.text = "${item.avgTempC}°C"
        binding.mainTemprature.text = formatToFullDayAndShortMonth(item.date)

        Glide.with(requireContext())
            .load(item.icon)
            .into(binding.mainImage)
    }


}