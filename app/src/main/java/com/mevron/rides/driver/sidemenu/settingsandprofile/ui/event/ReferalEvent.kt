package com.mevron.rides.driver.sidemenu.settingsandprofile.ui.event

sealed interface ReferalEvent{
    object GetReferalDetail: ReferalEvent
    object SetReferalDetail: ReferalEvent
    object GetReferalPrefDetail: ReferalEvent

}