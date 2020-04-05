package com.boomaa.pslv

import org.opencv.core.CvType
import org.opencv.core.Scalar
import java.awt.Color

object Util {
    fun scalarAwtColor(cvType: Int, color: Color): Scalar {
        val red = color.red.toDouble()
        val blue = color.blue.toDouble()
        val green = color.green.toDouble()
        val alpha = color.alpha.toDouble()
        val grayscale = ((red + blue + green) / 3).coerceAtMost(255.0);

        return when (cvType) {
            CvType.CV_8UC1 -> Scalar(grayscale)
            CvType.CV_8UC3 -> Scalar(red, blue, green)
            CvType.CV_8UC4 -> Scalar(red, blue, green, alpha)
            else -> throw IllegalArgumentException("cvType must be CV_8UC1 or 3 or 4");
        }
    }
}