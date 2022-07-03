package com.example.poznanbike.database

import androidx.room.*
import com.example.poznanbike.bikestations.BikeStation

@Dao
interface BikeStationDAO {

    @Insert
    fun insert(bikeStation: BikeStationDB)

    @Update
    fun update(bikeStation: BikeStationDB)

    @Query("SELECT * FROM saved_bike_stations WHERE bikes > :num")
    fun getStationsWithBikes(num: Int): List<BikeStationDB>

    @Query("SELECT * FROM saved_bike_stations WHERE freeRacks > :num")
    fun getStationsWithFreeRacks(num: Int): List<BikeStationDB>

    @Query("SELECT * FROM saved_bike_stations")
    fun getAll() : List<BikeStationDB>

    @Query("SELECT * FROM saved_bike_stations WHERE label = :label")
    fun getByLabel(label: String): List<BikeStationDB>

    @Query("DELETE from saved_bike_stations")
    fun clear()

    @Delete
    fun delete(bikeStation: BikeStationDB)
}