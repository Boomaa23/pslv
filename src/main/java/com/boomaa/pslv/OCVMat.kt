package com.boomaa.pslv

import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class OCVMat(var matrix: Mat) {
    private var tempOutput = Mat()

    fun bilateralFilter(d: Int = 20, sigmaColor: Int = 75, sigmaSpace: Int = 20): OCVMat {
        Imgproc.bilateralFilter(matrix, tempOutput, d, sigmaColor.toDouble(), sigmaSpace.toDouble())
        matrix = tempOutput;
        return this
    }

    fun cvtColor(code: Int = Imgproc.COLOR_RGB2GRAY): OCVMat {
        Imgproc.cvtColor(matrix, tempOutput, code)
        matrix = tempOutput
        return this
    }

    fun canny(threshold1: Int = 150, threshold2: Int = 200): OCVMat {
        Imgproc.Canny(matrix, tempOutput, threshold1.toDouble(), threshold2.toDouble())
        matrix = tempOutput
        return this
    }

    fun cornerHarris(blockSize: Int = 2, ksize: Int = 3, k: Double = 0.04): OCVMat {
        Imgproc.cornerHarris(matrix, tempOutput, blockSize, ksize, k)
        tempOutput.convertTo(matrix, CvType.CV_8UC1)
        return this
    }
}