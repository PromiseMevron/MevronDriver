package com.mevron.rides.driver.sidemenu.driverprefrence.ui.event

sealed interface DriverPrefrenceEvent{
    object UpdatePref: DriverPrefrenceEvent
    object FetchPref: DriverPrefrenceEvent
}