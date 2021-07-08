package com.contact.contactapplication

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.mineplatform.api.model.MyViewModel

class ContactRequestModel : MyViewModel() {
    var rquestResponse = MutableLiveData<ArrayList<ContactPojo>>()

    fun getRequestData( c: Context) {
        isLoading.value = true
        ContactRepositary.getRequestData({
            rquestResponse.value = it
            isLoading.value = false
        },c, {
            apiError.value = it
            isLoading.value = false
        }, {
            onFailure.value = it
            isLoading.value = false
        })
    }


}