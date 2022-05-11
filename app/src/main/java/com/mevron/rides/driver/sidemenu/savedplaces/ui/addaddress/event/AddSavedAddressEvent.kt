package com.mevron.rides.driver.sidemenu.savedplaces.ui.addaddress.event

sealed interface AddSavedAddressEvent {
    object OnAddNewClicked: AddSavedAddressEvent
    object GetNewAddress: AddSavedAddressEvent
    object OnBackButtonClicked: AddSavedAddressEvent
}