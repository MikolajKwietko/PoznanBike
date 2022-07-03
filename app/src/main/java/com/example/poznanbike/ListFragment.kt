package com.example.poznanbike

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.poznanbike.bikestations.BikeStation
import com.example.poznanbike.database.BikeStationDatabase
import com.example.poznanbike.databinding.FragmentListBinding
import com.example.poznanbike.network.BikeStationApi
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.lang.Exception

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ListFragment : Fragment() {

    private var bikeStations: List<BikeStation>? = null
    private lateinit var binding: FragmentListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentListBinding.inflate(inflater, container, false)

        return binding.root

    }

    fun clearList() {
        val adapter = binding.bikeStationsList.adapter ?: return
        (adapter as BikeStationsListAdapter).clearList()
        binding.updateInfo.visibility = View.VISIBLE
        binding.progressBar.visibility = View.INVISIBLE
    }




    fun showList(){
        // SHows the list and sets the item click listener
        binding.updateInfo.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.INVISIBLE
        with(binding.bikeStationsList) {
            layoutManager = LinearLayoutManager(context)
            adapter = BikeStationsListAdapter(ArrayList(bikeStations), object :
                BikeStationListEventListener {
                override fun clickListener(
                    position: Int,
                    bikeStation: BikeStation
                ) {
                    val actionListFragmentToDetailFragment =
                        ListFragmentDirections.actionListFragmentToDetailFragment(
                            bikeStation
                        ) // safe args are used to pass data between fragemnts
                    findNavController().navigate(actionListFragmentToDetailFragment)
                }

            }
            )
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(bikeStations != null)
            showList()
    }

    fun populateListFromInternet(){
        binding.updateInfo.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE

        GlobalScope.launch {
            getBikeStationsFromRetrofit()

            withContext(Dispatchers.Main) {
                Snackbar.make(
                    binding.root,
                    "${bikeStations?.size}" + " bike stations",
                    Snackbar.LENGTH_LONG
                ).show()
                showList()
            }
        }
    }

    fun populateListFromDB(queryId: BikeStationDatabase.QUERYID) {
        binding.updateInfo.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE

        GlobalScope.launch {
            getBikeStationsFromRoom(queryId)
            withContext(Dispatchers.Main) {
                Snackbar.make(
                    binding.root,
                    "${bikeStations?.size} bike stations",
                    Snackbar.LENGTH_LONG
                ).show()

                if(bikeStations?.isNotEmpty() == true) {
                    showList()
                } else {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.updateInfo.visibility = View.VISIBLE
                }
            }
        }
    }

    private suspend fun getBikeStationsFromRoom(queryId: BikeStationDatabase.QUERYID) {
        val bikeStationDao =
            BikeStationDatabase.getInstance(requireContext()).bikeStationDatabaseDao

        val async = GlobalScope.async {
            withContext(Dispatchers.IO) {
                when (queryId) {
                    BikeStationDatabase.QUERYID.GET_STATIONS_WITH_BIKES -> {
                        bikeStationDao.getStationsWithBikes(0)
                    }
                    BikeStationDatabase.QUERYID.GET_STATIONS_WITH_FREE_RACKS -> {
                        bikeStationDao.getStationsWithFreeRacks(0)
                    }
                    BikeStationDatabase.QUERYID.GET_ALL -> {
                        bikeStationDao.getAll()
                    }
                    else -> bikeStationDao.getAll()
                }
            }
        }

        bikeStations = Helpers.createBikeStationList(async.await()).toList()
    }

    private suspend fun getBikeStationsFromRetrofit() {
        try {
            bikeStations = GlobalScope.async {
                withContext(Dispatchers.IO) {
                    BikeStationApi.retrofitService.getBikeStations().items
                }
            }.await()
        } catch (e: Exception) {
            Log.e(activity?.localClassName, e.message.toString())
        }
    }

    private suspend fun getBikeStationsFromRetrofitWithQuery() {
        try {
            bikeStations = GlobalScope.async {
                withContext(Dispatchers.IO) {
                    BikeStationApi.retrofitService.getBikeStations(
                        "pub_transport",
                        "stacje_rowerowe"
                    ).items
                }
            }.await()
        } catch (e: Exception) {
            Log.e(activity?.localClassName, e.message.toString())
        }
    }


}
