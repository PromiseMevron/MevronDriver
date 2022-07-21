package com.mevron.rides.driver.documentcheck.ui

sealed interface DocumentEvent{
    object GetStatus: DocumentEvent
}
