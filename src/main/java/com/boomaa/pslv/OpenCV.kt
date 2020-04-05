package com.boomaa.pslv

import org.opencv.core.*
import org.opencv.imgproc.Imgproc

object OpenCV {
    class ContourMap(var contours: List<MatOfPoint>, var hierarchy: Mat)

    fun findContours(input: Mat, mode: Int = Imgproc.RETR_EXTERNAL, method: Int = Imgproc.CHAIN_APPROX_SIMPLE): ContourMap {
        val contours = ArrayList<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(input, contours, hierarchy, mode, method)
        return ContourMap(contours, hierarchy)
    }

    fun approxPolyDP(contours: List<MatOfPoint>, epsilonMod: Double): List<MatOfPoint> {
        val out = ArrayList<MatOfPoint>()
        for (i in contours.indices) {
            val curve = MatOfPoint2f()
            val approxCurve = MatOfPoint2f()
            contours[i].convertTo(curve, CvType.CV_32FC2)

            val epsilon = epsilonMod * Imgproc.arcLength(curve, true)
            Imgproc.approxPolyDP(curve, approxCurve, epsilon, true)
            val tempOut = MatOfPoint()
            approxCurve.convertTo(tempOut, CvType.CV_32S)
            out.add(tempOut)
        }
        return out;
    }
}