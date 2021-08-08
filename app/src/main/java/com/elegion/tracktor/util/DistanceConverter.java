package com.elegion.tracktor.util;
/*
вспомогательный (util) класс DistanceConverter,
который будет осуществлять конвертацию из метров в футы/мили.
 */

public class DistanceConverter {

    public static double convertFromMetersToKm(double distance){return distance/1000;}
    public static double convertFromMetersToMiles(double distance){ return distance/1609; }
    public static double convertFromMetersToFeets(double distance){return distance*3.28084;}

}
