package com.boomaa.pslv

import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class OCVMat(var matrix: Mat) {
    private var tempOutput = Mat()

    fun bilateralFilter(d: Int, sigmaColor: Int, sigmaSpace: Int): OCVMat {
        Imgproc.bilateralFilter(matrix, tempOutput, d, sigmaColor.toDouble(), sigmaSpace.toDouble())
        matrix = tempOutput;
        return this
    }

    fun cvtColor(code: Int): OCVMat {
        Imgproc.cvtColor(matrix, tempOutput, code)
        matrix = tempOutput
        return this;
    }

    fun canny(threshold1: Int, threshold2: Int): OCVMat {
        Imgproc.Canny(matrix, tempOutput, threshold1.toDouble(), threshold2.toDouble())
        matrix = tempOutput
        return this;
    }

    inner class Defaults {
        fun getOCVMat(): OCVMat {
            return this@OCVMat;
        }

        fun bilateralFilter(): Defaults {
            bilateralFilter(20, 75, 75)
            return this;
        }

        fun cvtColor(): Defaults {
            cvtColor(Imgproc.COLOR_RGB2GRAY)
            return this;
        }

        fun canny(): Defaults {
            canny(150, 200)
            return this;
        }
    }
}