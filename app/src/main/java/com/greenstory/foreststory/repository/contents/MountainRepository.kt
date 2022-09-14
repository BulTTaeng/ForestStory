package com.greenstory.foreststory.repository.contents

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.greenstory.foreststory.model.contents.MountainDto
import com.greenstory.foreststory.model.contents.MountainEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class MountainRepository {
    val db = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()

    val mountainList = ArrayList<MountainDto>()



    suspend fun getMountainData() =flow{

        mountainList.clear()

        try {
            var temp = MountainEntity()
            db.collection("mountain").get().addOnCompleteListener { task ->
                if(task.isSuccessful){
                    for(it in task.result){
                        temp = it.toObject(MountainEntity::class.java)
                        mountainList.add(temp.mapper(0.0F))
                    }
                }
            }.await()
            emit(mountainList)
        }catch (e : Exception){
            Log.d("getMountain Exception" , e.toString())
        }
    }.flowOn(Dispatchers.IO)

    fun setMountainData(data : ArrayList<MountainDto>){
        //_mutableMountainData.value = data
    }

    fun getMountainWithDistance(lati : Double , longi : Double) = flow{

        try {
            var startLoc = Location("start")
            var distance = 0.0F
            startLoc.latitude = lati
            startLoc.longitude = longi

            var endLoc = Location("end")

            for(it in mountainList){
                endLoc.longitude = it.longitude
                endLoc.latitude = it.latitude

                distance = startLoc.distanceTo(endLoc)
                it.distance=distance
            }

            mountainList.sortBy { it.distance }
            emit(mountainList)

        }catch (e : Exception){
            Log.d("Distance Exception" , e.toString())
        }
    }.flowOn(Dispatchers.IO)

    fun MountainEntity.mapper(dis : Float): MountainDto =
        MountainDto(image, name , latitude , longitude, distance = dis)

}