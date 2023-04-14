package com.poc.firstprojectinfnet.home.presentation.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.*
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.poc.firstprojectinfnet.databinding.FragmentWeatherBinding
import com.poc.firstprojectinfnet.home.data.WeatherState
import com.poc.firstprojectinfnet.home.presentation.HomeAction
import com.poc.firstprojectinfnet.home.presentation.HomeViewModel
import com.poc.firstprojectinfnet.home.presentation.HomeViewModel.Companion.REQUEST_PERMISSION_LOCATION
import org.koin.android.ext.android.inject


class WeatherFragment : Fragment(), LocationListener {

    private var binding: FragmentWeatherBinding? = null
    private val homeViewModel: HomeViewModel by inject()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWeatherBinding.inflate(inflater)

        binding?.progressBar?.visibility = View.VISIBLE
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAction()
        observeState()
        homeViewModel.givePermissionLocation()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private fun observeState() = with(homeViewModel) {
        state.observe(this@WeatherFragment.viewLifecycleOwner) { state ->
            state.weatherState?.let { setupView(weatherState = it) }
        }
    }

    private fun getWeatherColor(conditionCode: Int): String {
        return when (conditionCode) {
            in 200..232 -> "#637E90"
            in 300..321 -> "#29B3FF"
            in 500..531 -> "#14C2DD"
            in 600..622 -> "#E5F2F0"
            in 701..781 -> "#FFFEA8"
            800 -> "#FBC740"
            801 -> "#BCECE0"
            802 -> "#BCECE0"
            803, 804 -> "#36EEE0"
            1183 -> "#14C2DD"
            else -> "#FBC740"
        }
    }

    private fun getWeatherIcon(conditionCode: Int): String {
        return when (conditionCode) {
            in 200..232 -> "wi_thundeerstorm"
            in 300..321 -> "wi_showers"
            in 500..531 -> "wi_rain"
            in 600..622 -> "wi_snow"
            in 701..781 -> "wi_fog"
            800 -> "wi_day_syunny"
            801 -> "wi_day_cloud"
            802 -> "wi_cloud"
            803, 804 -> "wi_day_cloud_high"
            1183 -> "wi_day_light_wind"
            else -> "wi_day_sunny"
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setupView(weatherState: WeatherState) {
        binding?.progressBar?.visibility = View.GONE
        binding?.temperature?.text = "${weatherState.weatherData.temperature} ºC"
        binding?.description?.text = weatherState.weatherData.txtDescription
        binding?.neigh?.text =
            "${weatherState.districtDate.district}, ${weatherState.districtDate.city}"
        try {
            val imageView = binding?.imageView
            val drawableId = resources.getIdentifier(
                getWeatherIcon(weatherState.weatherData.code),
                "drawable",
                requireContext().packageName
            )
            imageView?.setImageResource(drawableId)

            val hexColor = getWeatherColor(weatherState.weatherData.code)
            val color = Color.parseColor(hexColor)
            imageView?.setColorFilter(color)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun observeAction() = with(homeViewModel) {
        action.observe(this@WeatherFragment.viewLifecycleOwner) { action ->
            when (action) {
                HomeAction.GivePermissionLocation -> {
                    this@WeatherFragment.givePermissionLocation()
                }
                HomeAction.OpenLocationManager -> {
                    openLocationManager()
                }
            }
        }
    }

    private fun openLocationManager() {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                location?.latitude?.let {
                    location.longitude.let { it1 ->
                        homeViewModel.requestWeatherData(
                            it,
                            it1
                        )
                    }
                } ?: also {
                    binding?.progressBar?.visibility = View.GONE
                }
            }

    }

    private fun givePermissionLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSION_LOCATION
            )
        } else {
            homeViewModel.openALocationManager()
        }
    }

    override fun onLocationChanged(location: Location) {

    }
}