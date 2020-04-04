package com.boomaa.pslv

import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.imgproc.Imgproc

object OpenCV {
    class ContourMap(var contours: List<MatOfPoint>, var hierarchy: Mat)

    fun findContours(input: Mat, mode: Int, method: Int): ContourMap {
        val contours = ArrayList<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(input, contours, hierarchy, mode, method)
        return ContourMap(contours, hierarchy)
    }

    fun findContours(input: Mat): ContourMap {
        return findContours(input, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
    }
}