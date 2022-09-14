package com.mevron.rides.driver.ride

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.text.capitalize
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.mevron.rides.driver.App
import com.mevron.rides.driver.R
import com.mevron.rides.driver.domain.ISocketManager
import com.mevron.rides.driver.home.HomeViewModel
import com.mevron.rides.driver.sidemenu.settingsandprofile.data.model.GetProfileData
import com.mevron.rides.driver.sidemenu.vehicle.ui.SelectVehicle
import com.mevron.rides.driver.sidemenu.vehicle.ui.VehicleAdapter
import com.mevron.rides.driver.sidemenu.vehicle.ui.VehicleViewModel
import com.mevron.rides.driver.sidemenu.vehicle.ui.event.VehicleEvent
import com.mevron.rides.driver.util.Constants
import com.mevron.rides.driver.util.Screen
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class RideActivity : AppCompatActivity(), SelectVehicle {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawer: ImageButton
    private lateinit var addVehicle: ImageButton
    private lateinit var vehicleClick: MaterialButton
    private lateinit var name: TextView
    private lateinit var phone: TextView
    private lateinit var type: TextView
    private lateinit var image: CircleImageView
    private lateinit var rating: RatingBar
    private lateinit var bottomSheetLayout: ConstraintLayout
    private lateinit var blurBKG: View
    private val viewModel: HomeViewModel by viewModels()
    private val vehicleViewModel: VehicleViewModel by viewModels()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var adapter: VehicleAdapter

    @Inject
    lateinit var socketManager: ISocketManager

    val sPref= App.ApplicationContext.getSharedPreferences(Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride)
        socketManager.connect()

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        blurBKG = findViewById(R.id.bg)
        val widthOfNav = (Screen.width)
        navView.layoutParams.width = widthOfNav
        navView.requestLayout()
        navController = findNavController(R.id.nav_host_fragment)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?

        appBarConfiguration = AppBarConfiguration(setOf(R.id.my_rides_fragment,
            R.id.notificationFragment, R.id.balanceFragment, R.id.helpFragment,
            R.id.settingsFragment), drawerLayout)
        // NavigationUI.setupWithNavController(navigationView, navController)
        navView.setupWithNavController(navController)
        val headerView = navView.getHeaderView(0)
        name = findViewById(R.id.full_name)
        phone = findViewById(R.id.phone_number)
        image = findViewById(R.id.profile_image)
        rating = findViewById(R.id.rating)
        type = findViewById(R.id.type)
        drawer = headerView.findViewById(R.id.close_drawer)
        addVehicle = headerView.findViewById(R.id.add_vehicle)
        vehicleClick = headerView.findViewById(R.id.default_vehicle_click)
        bottomSheetLayout = findViewById<ConstraintLayout>(R.id.bottom_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
        adapter = VehicleAdapter(this, true)

        vehicleClick.setOnClickListener {
            blurBKG.visibility = View.VISIBLE
            bottomSheetBehavior.peekHeight = vehicleViewModel.state.value.peakHeight
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        blurBKG.setOnClickListener {
            bottomSheetBehavior.peekHeight = 0
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            blurBKG.visibility = View.GONE
        }
        drawer.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        bottomSheetLayout.findViewById<ConstraintLayout>(R.id.add_a_vehicle).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            bottomSheetBehavior.peekHeight = 0
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
            navController.navigateUp()
            navController.navigate(R.id.addNewVehicleFragment)
        }
        bottomSheetLayout.findViewById<RecyclerView>(R.id.recycler_view).adapter = adapter

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        blurBKG.visibility = View.GONE
                    }
                    else -> {
                        blurBKG.visibility = View.VISIBLE
                    }
                }
            }
        })

        addVehicle.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
            navController.navigateUp()
            navController.navigate(R.id.addNewVehicleFragment)
        }
        image.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
            navController.navigateUp()
            navController.navigate(R.id.profileFragment)
        }
        updateSideMenu()
        viewModel.getProfile()
        vehicleViewModel.onEvent(VehicleEvent.MakeAPICall)

        lifecycleScope.launch {
            viewModel.state.collect {
                if (it.gottenProfile) {
                    updateSideMenu()
                }
            }
        }

        lifecycleScope.launch {
            vehicleViewModel.state.collect {
                if (it.vehicle.isEmpty()) {
                    addVehicle.visibility = View.VISIBLE
                    vehicleClick.visibility = View.GONE
                }else{
                    addVehicle.visibility = View.GONE
                    vehicleClick.visibility = View.VISIBLE
                }
                vehicleClick.text = it.defaultCar
                adapter = VehicleAdapter(this@RideActivity, true)
                bottomSheetLayout.findViewById<RecyclerView>(R.id.recycler_view).adapter = adapter
                adapter.submitList(it.vehicle)
            }
        }

    }
private fun updateSideMenu(){
    val json = sPref.getString(Constants.PROFILE, null)
    json?.let {
        val gson = Gson()
        val user = gson.fromJson(it, GetProfileData::class.java)
        name.text = user.firstName.toString()
        phone.text = user.phoneNumber
        rating.rating = user.rating.toString().toFloat()
        Picasso.get().load(user.profilePicture.toString()).placeholder(R.drawable.ic_logo).error(R.drawable.ic_logo).into(image)
        name.text = user.firstName.toString()
        type.text = user.type?.capitalize()


    }
}
    /*  override fun onCreateOptionsMenu(menu: Menu): Boolean {
          // Inflate the menu; this adds items to the action bar if it is present.
          menuInflater.inflate(R.menu.menu_drawer, menu)
          return true
      }*/


    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun selectVehicle(uuid: String, clickedPosition: Int) {
        vehicleViewModel.updateState(uuid = uuid, default = vehicleViewModel.state.value.vehicle[clickedPosition].make)
        adapter = VehicleAdapter(this@RideActivity, true, clickedPosition)
        bottomSheetLayout.findViewById<RecyclerView>(R.id.recycler_view).adapter = adapter
        adapter.submitList(vehicleViewModel.state.value.vehicle)
        vehicleViewModel.updateAVehicle()
    }

}