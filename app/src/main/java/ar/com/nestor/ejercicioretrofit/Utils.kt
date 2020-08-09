package ar.com.nestor.ejercicioretrofit

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable


/*
 * Just check for Animatables in a Layer-List Drawable and start them
 */
fun StartAnimationsInLayerDrawable(layer: Drawable) : LayerDrawable? {
    if (layer !is LayerDrawable)
        return null
    with(layer) {
        for (i in 0 until numberOfLayers)
            if (getDrawable(i) is Animatable)
                (getDrawable(i) as Animatable).start()
        return this
    }
}

fun StopAnimationsInLayerDrawable(layer: Drawable) : LayerDrawable? {
    if (layer !is LayerDrawable)
        return null
    with(layer) {
        for (i in 0 until numberOfLayers)
            if (getDrawable(i) is Animatable)
                (getDrawable(i) as Animatable).stop()
        return this
    }
}