package com.mevron.rides.driver.sidemenu.supportpages.ui.notification.event

sealed interface NotificationEvents{
    object OnBackButtonClicked: NotificationEvents
    object GetNotifications: NotificationEvents
}