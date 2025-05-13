package com.multitv.eventbuilder.ui.travelstay.aboutanatalya.fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.material.shape.ShapeAppearanceModel
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.constant.AppConstant
import com.multitv.eventbuilder.databinding.FragmentAntalyaTourBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.aboutdoha.model.AboutDohaItems
import com.multitv.eventbuilder.model.aboutdoha.model.CategoryItem
import com.multitv.eventbuilder.ui.travelstay.aboutanatalya.viewmodel.AboutDohaViewModel
import com.multitv.eventbuilder.ui.travelstay.aboutanatalya.viewmodelfactory.AboutDohaViewModelFactory

class AntalyaFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentAntalyaTourBinding? = null
    private val binding get() = _binding!!

    private var navController: NavController? = null

    private lateinit var viewModel: AboutDohaViewModel
    private lateinit var aboutDohaItems: AboutDohaItems

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAntalyaTourBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        binding.backArrow.setOnClickListener(this)
        binding.sightseeing.setOnClickListener(this)
        binding.restaurantNearby.setOnClickListener(this)
        binding.shopping.setOnClickListener(this)

        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = AboutDohaViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[AboutDohaViewModel::class.java]

        observeViewModel()

        viewModel.getAboutDohaData(AppConstant.ABOUT_DOHA)

        val shapeAppearanceModel1 = ShapeAppearanceModel.builder()
            .setBottomLeftCornerSize(100f)
            .setTopRightCornerSize(100f)
            .build()

        val shapeAppearanceModel2 = ShapeAppearanceModel.builder()
            .setTopLeftCornerSize(100f)
            .setBottomRightCornerSize(100f)
            .build()

        binding.firstMaterialCard.shapeAppearanceModel = shapeAppearanceModel1
        binding.secondCard.shapeAppearanceModel = shapeAppearanceModel2
        binding.thirdCard.shapeAppearanceModel = shapeAppearanceModel2
        binding.fourthCard.shapeAppearanceModel = shapeAppearanceModel1
        binding.location4.shapeAppearanceModel = shapeAppearanceModel1
        binding.lacation3.shapeAppearanceModel = shapeAppearanceModel2
        binding.location2.shapeAppearanceModel = shapeAppearanceModel2
        binding.lacation1.shapeAppearanceModel = shapeAppearanceModel1


        /*val shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)
        val shapeDrawable2 = MaterialShapeDrawable(shapeAppearanceModel2)

        binding.firstMaterialCard.background = shapeDrawable
        binding.secondCard.background = shapeDrawable2
        binding.thirdCard.background = shapeDrawable2
        binding.fourthCard.background = shapeDrawable*/
    }

    fun observeViewModel() {
        viewModel.aboutResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Generic.Loading -> {
                    ProgressDialog.showProgresssDialog(requireContext())
                }

                is Generic.Success -> {
                    ProgressDialog.hideProgressDialog()

                    /*Toast.makeText(
                        requireContext(),
                        "Error: ${response.data.success}",
                        Toast.LENGTH_SHORT
                    ).show()*/

                    val responseData = response.data
                    if (responseData.success) {
                        aboutDohaItems = responseData.data

                        binding.title.text = response.data.data.pageTittle.toString().trim()
                        binding.heading.text = response.data.data.bannerTitle.toString().trim()

                        binding.subTittle.text = Html.fromHtml(aboutDohaItems.description, Html.FROM_HTML_MODE_LEGACY)

                        // Load default category - Sightseeing
                        updateCategoryUI(aboutDohaItems.sightseeing)
                        updateCategorySelectionUI("Sightseeing")
                    } else {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT)
                            .show()
                    }

                }

                is Generic.Error -> {
                    ProgressDialog.hideProgressDialog()
                    Toast.makeText(
                        requireContext(),
                        "Error: ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("About Doha Error", response.message ?: "Unknown error")
                }

                is Generic.Failure -> {
                    ProgressDialog.hideProgressDialog()
                    Toast.makeText(
                        requireContext(),
                        "Failure: ${response.exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("About Doha Failure", response.exception.message ?: "Unknown failure")
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.backArrow -> {
                navController?.navigateUp()
            }

            R.id.sightseeing -> {
                updateCategoryUI(aboutDohaItems.sightseeing)
                updateCategorySelectionUI("Sightseeing")
            }

            R.id.restaurantNearby -> {
                updateCategoryUI(aboutDohaItems.rastaurents)
                updateCategorySelectionUI("Restaurant")
            }

            R.id.shopping -> {
                updateCategoryUI(aboutDohaItems.shopping)
                updateCategorySelectionUI("Shopping")
            }
        }
    }

    private fun updateCategoryUI(locations: List<CategoryItem>) {
        val locationList = locations.firstOrNull()?.location ?: return

        val imageViews = listOf(
            binding.lacation1, binding.location2, binding.lacation3, binding.location4
        )

        val textViews = listOf(
            binding.locationText,
            binding.locationText2,
            binding.locationText3,
            binding.locationText4
        )

        for (i in imageViews.indices) {
            if (i < locationList.size) {
                val item = locationList[i]

                Glide.with(requireContext())
                    .load(item.image)
                    .into(imageViews[i])

                textViews[i].text = item.location

                val link = item.link

                // Check if link is a full Maps/Address URL (you can adjust conditions)
                if (!link.isNullOrEmpty() && (link.startsWith("http") && link.contains("maps"))) {
                    imageViews[i].isClickable = true
                    imageViews[i].setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                        startActivity(intent)
                    }
                } else {
                    // Disable touch/click if not a valid maps link
                    imageViews[i].isClickable = false
                    imageViews[i].setOnClickListener(null)
                }
            }
        }
    }

    private fun updateCategorySelectionUI(selected: String) {
        val selectedBg = R.drawable.place_selected
        val unselectedBg = R.drawable.not_selected_location

        fun updateButton(view: TextView, isSelected: Boolean) {
            view.background = ContextCompat.getDrawable(
                requireContext(),
                if (isSelected) selectedBg else unselectedBg
            )
            view.setTextColor(if (isSelected) Color.parseColor("#5171ED") else Color.WHITE)
        }

        updateButton(binding.sightseeing, selected == "Sightseeing")
        updateButton(binding.restaurantNearby, selected == "Restaurant")
        updateButton(binding.shopping, selected == "Shopping")
    }


}