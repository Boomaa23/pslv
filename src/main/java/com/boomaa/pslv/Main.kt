package com.boomaa.pslv

import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
        val imageMatrix = Imgcodecs.imread(args[0]) as Mat
        PuzzlePiece(imageMatrix)
    }
}