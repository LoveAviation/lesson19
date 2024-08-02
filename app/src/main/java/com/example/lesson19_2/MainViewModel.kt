package com.example.lesson19_2

import androidx.lifecycle.ViewModel
import org.osmdroid.util.GeoPoint

class MainViewModel: ViewModel() {
    val list : List<LandmarkClass> = listOf(
        LandmarkClass(GeoPoint(40.6892, -74.0445), "Statue of Liberty", "A symbol of freedom and independence of the USA, located on Liberty Island."),
        LandmarkClass(GeoPoint(48.8584, 2.2945), "Eiffel Tower", "A famous iron tower, a symbol of Paris."),
        LandmarkClass(GeoPoint(41.8902, 12.4922), "Colosseum", "An ancient Roman amphitheater, one of the most well-known ancient structures."),
        LandmarkClass(GeoPoint(27.1751, 78.0421), "Taj Mahal", "A magnificent white marble mausoleum built in honor of an emperor’s wife."),
        LandmarkClass(GeoPoint(40.4319, 116.5704), "Great Wall of China", "A magnificent white marble mausoleum built in honor of an emperor’s wife."),
        LandmarkClass(GeoPoint(-33.8568, 151.2153), "Sydney Opera House", "A famous building with unique architecture on the Sydney Harbour."),
        LandmarkClass(GeoPoint(-13.1631, -72.5450), "Machu Picchu", "An ancient Incan city high in the Peruvian mountains."),
        LandmarkClass(GeoPoint(30.3285, 35.4444), "Petra", "An ancient city carved into the rocks, known for its architectural monuments."),
        LandmarkClass(GeoPoint(36.1069, -112.1129), "Grand Canyon", "A vast canyon with unique geological formations and stunning views."),
        LandmarkClass(GeoPoint(29.9792, 31.1342), "Giza Pyramids and Sphinx", "Ancient Egyptian pyramids and the Sphinx, symbols of ancient Egyptian civilization."),
    )
}