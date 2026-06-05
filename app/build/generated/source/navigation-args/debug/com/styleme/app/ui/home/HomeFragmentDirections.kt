package com.styleme.app.ui.home

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.styleme.app.R

public class HomeFragmentDirections private constructor() {
  public companion object {
    public fun actionHomeToHairColour(): NavDirections =
        ActionOnlyNavDirections(R.id.action_home_to_hairColour)

    public fun actionHomeToHairStyle(): NavDirections =
        ActionOnlyNavDirections(R.id.action_home_to_hairStyle)
  }
}
