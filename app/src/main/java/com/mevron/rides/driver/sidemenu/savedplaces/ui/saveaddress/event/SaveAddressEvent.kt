package com.mevron.rides.driver.sidemenu.savedplaces.ui.saveaddress.event

sealed interface SaveAddressEvent{
    object OnTextChanged: SaveAddressEvent
    object OnBackButtonPressed: SaveAddressEvent
    object SaveHomeWorkAddress: SaveAddressEvent
    object SaveOtherAddress: SaveAddressEvent
    object AddressSelected: SaveAddressEvent
}