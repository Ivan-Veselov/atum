package ru.spbau.mit.atum;

import android.support.annotation.NonNull;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Класс, содержащий вспомогательные статические методы для работы с местами.
 */
public final class PlacesUtils {
    public static void setBuilderPositionNearPlace(@NonNull PlacePicker.IntentBuilder builder,
                                                   @NonNull Place place) {
        final double radius = 0.001;

        double latitude = place.getLatLng().latitude;
        double longitude = place.getLatLng().longitude;
        builder.setLatLngBounds(
                new LatLngBounds(new LatLng(latitude - radius, longitude),
                new LatLng(latitude + radius, longitude)));
    }
}
