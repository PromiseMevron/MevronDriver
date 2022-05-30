package com.mevron.rides.driver.sidemenu.savedplaces.ui.addressdetail.event

sealed interface SaveAddressDetailsEvent{
    object BackButtonPressed: SaveAddressDetailsEvent
    object SaveAddressClicked: SaveAddressDetailsEvent
}