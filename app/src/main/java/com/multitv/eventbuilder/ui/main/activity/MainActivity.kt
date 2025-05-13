package com.multitv.eventbuilder.ui.main.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.messaging.FirebaseMessaging

import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.base.BaseActivityTwo
import com.multitv.eventbuilder.databinding.ActivityMainBinding

class MainActivity : BaseActivityTwo(),OnClickListener {

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            Log.d("FCM Token Manual", token)
        }.addOnFailureListener {
            Log.e("FCM Token Manual", "Failed to get token", it)
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController

        binding.homell.setOnClickListener(this)
        binding.conferencell.setOnClickListener(this)
        binding.travelandstayll.setOnClickListener(this)
        binding.interactionll.setOnClickListener(this)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> updateTabSelection(binding.homell)
                R.id.conferenceFragment -> updateTabSelection(binding.conferencell)
                R.id.travelandstay -> updateTabSelection(binding.travelandstayll)
                R.id.interactionFragment -> updateTabSelection(binding.interactionll)
            }
        }

        updateTabSelection(binding.homell)
    }

    private fun handleInsets() {
        // Store the original padding so we can add insets on top of it
        val originalPaddingBottom = binding.bottombar.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(binding.bottombar) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Add system bottom inset to original padding
            view.setPadding(
                view.paddingLeft,
                view.paddingTop,
                view.paddingRight,
                originalPaddingBottom + insets.bottom
            )

            WindowInsetsCompat.CONSUMED
        }
    }



    override fun onClick(v: View?) {
        val navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true) // Prevents multiple instances of the same fragment
            .setPopUpTo(R.id.homeFragment, false) // Keeps homeFragment in the stack
            .build()

        when(v?.id){

            R.id.homell -> {navController.navigate(R.id.homeFragment,null,navOptions)
                updateTabSelection(binding.homell)}

            R.id.conferencell ->{  navController.navigate(R.id.conferenceFragment,null,navOptions)
                updateTabSelection(binding.conferencell)}

            R.id.travelandstayll ->{  navController.navigate(R.id.travelandstay,null,navOptions)
                updateTabSelection(binding.travelandstayll)}

            R.id.interactionll ->{  navController.navigate(R.id.interactionFragment,null,navOptions)
                updateTabSelection(binding.interactionll)}
        }
    }

    @SuppressLint("ResourceType")
    private fun updateTabSelection(selectedTab: ConstraintLayout) {

        resetTabColors()

        // Increase selected tab icon size
        when (selectedTab.id) {
            R.id.homell -> {
                /*binding.homeImage.animate().scaleX(1.1f).scaleY(1.1f).setDuration(200).start()*/
                binding.home.setTextColor(ContextCompat.getColor(this, R.color.red))
                binding.homeImage.setColorFilter(ContextCompat.getColor(this, R.color.red))
            }
            R.id.conferencell -> {
               /* binding.conferenceImage.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).start()*/
                binding.conference.setTextColor(ContextCompat.getColor(this, R.color.red))
                binding.conferenceImage.setColorFilter(ContextCompat.getColor(this, R.color.red))
            }
            R.id.travelandstayll -> {
               /* binding.travelAndStayImage.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).start()*/
                binding.travel.setTextColor(ContextCompat.getColor(this, R.color.red))
                binding.travelAndStayImage.setColorFilter(ContextCompat.getColor(this, R.color.red))
            }
            R.id.interactionll -> {
                /*binding.interactionImage.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).start()*/
                binding.interaction.setTextColor(ContextCompat.getColor(this, R.color.red))
                binding.interactionImage.setColorFilter(ContextCompat.getColor(this, R.color.red))
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun resetTabColors() {


        val defaultColor = ContextCompat.getColor(this, R.color.white) // You can define this in `colors.xml`

        // Reset text colors
        binding.home.setTextColor(defaultColor)
        binding.conference.setTextColor(defaultColor)
        binding.travel.setTextColor(defaultColor)
        binding.interaction.setTextColor(defaultColor)

        // Reset icon tints (optional)
        binding.homeImage.setColorFilter(defaultColor)
        binding.conferenceImage.setColorFilter(defaultColor)
        binding.travelAndStayImage.setColorFilter(defaultColor)
        binding.interactionImage.setColorFilter(defaultColor)

    }
}