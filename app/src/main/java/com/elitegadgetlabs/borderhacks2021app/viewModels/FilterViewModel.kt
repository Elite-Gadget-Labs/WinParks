package com.elitegadgetlabs.borderhacks2021app.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elitegadgetlabs.borderhacks2021app.R
import com.elitegadgetlabs.borderhacks2021app.models.Filter

class FilterViewModel : ViewModel() {

    var filterList = listOf(
        Filter(
            name = "Gardens",
            iconImageId = R.drawable.ic_local_florist_black_24dp

        ),
        Filter(
            name = "Cricket Pitch",
            iconImageId = R.drawable.ic_sports_cricket_black_24dp
        ),
        Filter(
            name = "Ball Hockey",
            iconImageId = R.drawable.ic_sports_hockey_black_24dp

        ),
        Filter(
            name = "Parking",
            iconImageId = R.drawable.ic_local_parking_black_24dp

        ),
        Filter(
            name = "Pond",
            iconImageId = R.drawable.ic_water_black_24dp

        ),
        Filter(
            name = "Toboggan Hill",
            iconImageId = R.drawable.ic_sledding_black_24dp

        ),
        Filter(
            name = "Cycling Trails",
            iconImageId = R.drawable.ic_directions_bike_black_24dp

        ),
        Filter(
            name = "Play Unit",
            iconImageId = R.drawable.ic_playground

            //Icon by Gan Khoon Lay on freeicons.io
            //(link:https://freeicons.io/children-playing-at-playground-icon-set/active-children-kids-playground-playing-slide-icon-78038)
        ),
        Filter(
            name = "Picnic Tables",
            iconImageId = R.drawable.ic_table_restaurant_black_24dp

        ),
        Filter(
            name = "Community Centre",
            iconImageId = R.drawable.ic_location_city_black_24dp

        ),
        Filter(
            name = "Basketball",
            iconImageId = R.drawable.ic_sports_basketball_black_24dp

        ),
        Filter(
            name = "Washroom",
            iconImageId = R.drawable.ic_wc_black_24dp

        ),
        Filter(
            name = "Baseball",
            iconImageId = R.drawable.ic_sports_baseball_black_24dp

        ),
        Filter(
            name = "Soccer",
            iconImageId = R.drawable.ic_sports_soccer_black_24dp

        ),
        Filter(
            name = "Spray Pad",
            iconImageId = R.drawable.ic_sports_soccer_black_24dp

        ),
        Filter(
            name = "Boardwalk",
            iconImageId = R.drawable.ic_shower_black_24dp

        ),
        Filter(
            name = "Dog Park",
            iconImageId = R.drawable.ic_pets_black_24dp

        ),
        Filter(
            name = "Pool",
            iconImageId = R.drawable.ic_pool_black_24dp

        ),
        Filter(
            name = "Fishing",
            iconImageId = R.drawable.ic_fishinghook
            // Icon by freeicons on freeicons.io (link: "https://freeicons.io/profile/3")

        ),
        Filter(
            name = "Benches",
            iconImageId = R.drawable.ic_chair_alt_black_24dp

        ),
        Filter(
            name = "Gazebo",
            iconImageId = R.drawable.ic_gazebo
            //Icon by Raj Dev on freeicons.io
            //link: https://freeicons.io/free-tourist-icons/ai-tent-icon-9532

        ),
        Filter(
            name = "Walking Trails",
            iconImageId = R.drawable.ic_hiking_black_24dp

        ),
        Filter(
            name = "Concession",
            iconImageId = R.drawable.ic_concession
            //Icon by Gan Khoon Lay on freeicons.io
            //Link: https://freeicons.io/internet-booking-online-ticket-icon-set/cashier-cellphone-man-money-paying-phone-popcorn-icon-73332

        ),
        Filter(
            name = "Sculptures",
            iconImageId = R.drawable.ic_sculpture
            //Icon by icon king1 on freeicons.io
            //link: https://freeicons.io/common-style-icons-18/monument-icon-18531

        ),
        Filter(
            name = "Tennis",
            iconImageId = R.drawable.ic_sports_tennis_black_24dp

        ),
        Filter(
            name = "Monuments",
            iconImageId = R.drawable.ic_monument
            //Icon by DotFix Technologies on freeicons.io
            //https://freeicons.io/app-icons/monument-icon-19223

        ),
        Filter(
            name = "Beach Volleyball",
            iconImageId = R.drawable.ic_sports_volleyball_black_24dp

        ),
        Filter(
            name = "Boat Launch",
            iconImageId = R.drawable.ic_directions_boat_black_24dp

        ),
        Filter(
            name = "Skateboard",
            iconImageId = R.drawable.ic_skateboarding_black_24dp

        ),
        Filter(
            name = "Football",
            iconImageId = R.drawable.ic_sports_football_black_24dp
        )
    )




    private var filterBehaviourList : MutableLiveData<MutableList<Boolean>> = MutableLiveData(createBehaviourList())
    private val behaviour: MutableLiveData<MutableList<Boolean>> get() = filterBehaviourList

    fun getFilter(index: Int): Boolean? {
        return behaviour.value?.get(index)
    }

    fun setFilter(boolean: Boolean, index: Int){
        filterBehaviourList.value = editBehaviourList(boolean, index)
    }

    private fun createBehaviourList(): MutableList<Boolean>{
        Log.d("debug", "I AM SNEAKY AF")
        val list = mutableListOf<Boolean>()
        for(f in filterList){
            list.add(false)
        }
        return list
    }

    private fun editBehaviourList(boolean: Boolean, index: Int): MutableList<Boolean>{
        val list = behaviour.value as MutableList<Boolean>
        filterList.forEachIndexed { i, _ ->
            if(i == index){
                list[i] = boolean
            }
        }
        return list
    }

}