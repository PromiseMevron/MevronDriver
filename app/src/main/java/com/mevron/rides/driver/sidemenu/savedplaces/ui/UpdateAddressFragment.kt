<<<<<<<< HEAD:app/src/main/java/com/mevron/rides/driver/sidemenu/savedplaces/ui/updateaddress/UpdateAddressFragment.kt
package com.mevron.rides.driver.sidemenu.savedplaces.ui.updateaddress
========
package com.mevron.rides.driver.sidemenu.savedplaces.ui
>>>>>>>> develop_view_binding_new:app/src/main/java/com/mevron/rides/driver/sidemenu/savedplaces/ui/UpdateAddressFragment.kt

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mevron.rides.driver.R

class UpdateAddressFragment : Fragment() {

    companion object {
        fun newInstance() = UpdateAddressFragment()
    }

    private lateinit var viewModel: UpdateAddressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.update_address_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UpdateAddressViewModel::class.java)
    }

}