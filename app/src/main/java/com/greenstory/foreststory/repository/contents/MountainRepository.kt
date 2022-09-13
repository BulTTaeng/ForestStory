package com.greenstory.foreststory.repository.contents

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.greenstory.foreststory.model.contents.MountainDto
import com.greenstory.foreststory.model.contents.MountainEntity
import kotlinx.coroutines.tasks.await

class MountainRepository {
    val db = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    val mountainList = ArrayList<MountainDto>()

    val _mutableMountainData = MutableLiveData<MutableList<MountainDto>>()

    val mountainData: LiveData<MutableList<MountainDto>>
        get() = _mutableMountainData



    suspend fun getMountainData() : Boolean{

        mountainList.clear()

        return try {
            var temp = MountainEntity()
            db.collection("mountain").get().addOnCompleteListener { task ->

                if(task.isSuccessful){
                    for(it in task.result){
                        temp = it.toObject(MountainEntity::class.java)
                        mountainList.add(temp.mapper(0.0F))
                    }
                    _mutableMountainData.value = mountainList
                }
            }.await()
            true
        }catch (e : Exception){
            Log.d("getMountain Exception" , e.toString())
            false
        }
    }

    fun setMountainData(data : ArrayList<MountainDto>){
        _mutableMountainData.value = data
    }

    fun MountainEntity.mapper(dis : Float): MountainDto =
        MountainDto(image, name , latitude , longitude, distance = dis)

}