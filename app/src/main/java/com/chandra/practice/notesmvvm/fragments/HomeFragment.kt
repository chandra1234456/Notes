package com.chandra.practice.notesmvvm.fragments


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chandra.practice.notesmvvm.AppDatabase
import com.chandra.practice.notesmvvm.R
import com.chandra.practice.notesmvvm.User
import com.chandra.practice.notesmvvm.UserAdapter
import com.chandra.practice.notesmvvm.UserRepository
import com.chandra.practice.notesmvvm.UserViewModel
import com.chandra.practice.notesmvvm.UserViewModelFactory
import com.chandra.practice.notesmvvm.databinding.FragmentHomeBinding
import com.chandra.practice.notesmvvm.util.showSnackBar
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale

class HomeFragment : Fragment() , UserAdapter.OnItemClickLister {
    private lateinit var userViewModel : UserViewModel
    private lateinit var userAdapter : UserAdapter
    private lateinit var homeBinding : FragmentHomeBinding
    private var userList : ArrayList<User> = ArrayList()
    private var isLinearLayout = true // To keep track of layout state

    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle? ,
                             ) : View {
        homeBinding = FragmentHomeBinding.inflate(layoutInflater)
        ViewCompat.setOnApplyWindowInsetsListener(homeBinding.root) { view, insets ->
            val systemInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemInsets.left,
                systemInsets.top,
                systemInsets.right,
                systemInsets.bottom
            )
            insets
        }

        val dao = AppDatabase.getDatabase(requireContext()).userDao()
        val repository = UserRepository(dao)
        val factory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this , factory)[UserViewModel::class.java]
        userAdapter = UserAdapter(this , requireContext()) // Initialize your RecyclerView adapter
        homeBinding.recyclerView.adapter = userAdapter
        homeBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // userViewModel.deleteAll()
        lifecycleScope.launchWhenStarted {
            userViewModel.users.collect { users ->
                userList.clear()
                userList.addAll(users)
                Log.d("TAG", "Fetched users: $users")
                if (users.isNotEmpty()){
                userAdapter.setUsers(users)
                }else{
                  homeBinding.ivNoData.visibility = View.VISIBLE
                }
            }
        }

        // To fetch users when needed, like in onCreate or onStart
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                userViewModel.fetchAllUsers()
            }
        }
        homeBinding.addFab.setOnClickListener {
            //addCalenderDateAndRemainder()
            findNavController().navigate(R.id.noteFragment)
        }
        homeBinding.searchBar.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelableArrayList("userList" , userList)  // Convert MutableList to ArrayList
            }
            findNavController().navigate(R.id.searchFragment , bundle)
        }

        homeBinding.ivShuffle.setOnClickListener {
          //  toggleLayoutManager()
            showPopupMenu(it)
        }
        homeBinding.addNotification.setOnClickListener {
            findNavController().navigate(R.id.collapsingToolbarLayoutFragment)
            /*NotificationUtil.showNotification(
                    context = requireContext(),
                    title = "Hello!",
                    message = "This is your notification."
          )*/
        }
        homeBinding.ivDarkTheme.setOnClickListener {
           // Toggle based on current mode
            val currentNightMode = AppCompatDelegate.getDefaultNightMode()
            val enableDark = currentNightMode != AppCompatDelegate.MODE_NIGHT_YES
            setDarkMode(enableDark)
        }
        return homeBinding.root
    }
    fun setDarkMode(enabled: Boolean) {
        val mode = if (enabled) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }


    private fun toggleLayoutManager() {
        if (isLinearLayout) {
            // Switch to StaggeredGridLayoutManager
            homeBinding.recyclerView.layoutManager =
                StaggeredGridLayoutManager(2 , StaggeredGridLayoutManager.VERTICAL)
            homeBinding.ivShuffle.setImageDrawable(
                    ContextCompat.getDrawable(
                            requireContext() ,
                            R.drawable.ic_grid_view
                                             )
                                                  )
        } else {
            homeBinding.ivShuffle.setImageDrawable(
                    ContextCompat.getDrawable(
                            requireContext() ,
                            R.drawable.ic_list
                                             )
                                                  )
            // Switch back to LinearLayoutManager
            homeBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
        // Toggle layout type
        isLinearLayout = ! isLinearLayout
    }

    override fun editTheUser(position : Int , user : User) {
        val action = HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(user)
        findNavController().navigate(action)
        Toast.makeText(requireContext() , "Edit" , Toast.LENGTH_SHORT).show()
    }

    override fun deleteTheUser(position : Int , user : User) {
        //userViewModel.deleteAll(user)
        showSnackBar(
                context = requireContext() ,
                view = homeBinding.rootView ,
                message = "Item deleted" ,
                actionText = "UNDO" ,
                actionListener = {
                    userViewModel.insert(user)
                    // Code to undo the delete action
                }
                    )
    }

    /**
     * TODO
     * This is Google Calender Remainder
     */
    @RequiresApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private fun addCalenderDateAndRemainder() {
        val intent = Intent(Intent.ACTION_INSERT)
        intent.data = CalendarContract.Events.CONTENT_URI
        intent.putExtra(CalendarContract.Events.TITLE , "Hello")
        intent.putExtra(CalendarContract.Events.DESCRIPTION , "Description")
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION , "Location")
        intent.putExtra(CalendarContract.Events.ALL_DAY , "true")
        intent.putExtra(Intent.EXTRA_EMAIL , "")
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
            //finish()
        } else {
            Toast.makeText(
                    requireContext() ,
                    "There is no app that support this action" ,
                    Toast.LENGTH_SHORT
                          ).show()
        }


    }
    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun getLastLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                getAddressFromLocation(location.latitude, location.longitude)
            } else {
                Toast.makeText(context, "Location not available", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getAddressFromLocation(lat: Double, lon: Double) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())

        try {
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                val fullAddress = address.getAddressLine(0) // Full address
                val city = address.locality
                val state = address.adminArea
                val country = address.countryName
                homeBinding.locationAddress.isSelected = true
                homeBinding.locationAddress.text = fullAddress

                // Show or use the address
               // Toast.makeText(context, "Address: $fullAddress", Toast.LENGTH_LONG).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Unable to get address", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun showPopupMenu(view: View?) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_grid_size -> {
                    Toast.makeText(requireContext(), "Grid Size", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_settings -> {
                    findNavController().navigate(R.id.settingsFragment)
                    Toast.makeText(requireContext(), "Settings", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_sort_by -> {
                    Toast.makeText(requireContext(), "Sort By", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }


}