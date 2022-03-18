package com.mevron.rides.driver.util

object Constants {

   // const val BASE_URL = "https://mevron-driver.herokuapp.com/"
    //
    const val BASE_URL = "http://staging.mevron.com:8082/"
    const val DATABASE_NAME = "alphally_db"
    const val SHARED_PREF_KEY = "alphally"
    const val TOKEN = "TOKEN"
    const val UUID = "UUID"
    const val LAT = "LAT"
    const val LNG = "LNG"
    const val TRIP_ID = "TRIP_ID"
    const val LOCATION_REQUEST_CODE = 4555
    const val REQUEST_TAKE_PHOTO = 1
    const val REQUEST_PICK_IMAGE = 2

    fun isNewNumberType(number: String): Boolean {
        return if (number.isEmpty() || number.length < 4) {
            false
        }
        else {
            (
                    number.substring(0, 4) == "0904" ||
                            number.substring(0, 4) == "0901" ||
                            number.substring(0, 3) == "904" ||
                            number.substring(0, 3) == "901" ||
                            number.substring(0, 4) == "0913" ||
                            number.substring(0, 3) == "913")
        }
    }
}