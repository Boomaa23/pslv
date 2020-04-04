package com.boomaa.pslv

import org.opencv.core.*
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.io.File
import javax.imageio.ImageIO


object Main {
    init {
        nu.pattern.OpenCV.loadLocally()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val inputMatrix = Imgcodecs.imread("puzzle.jpg") as Mat
        val cannyOut = getFilteredEdges(inputMatrix)
        val contours = ArrayList<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(cannyOut, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE)
        val drawing = Mat.ones(cannyOut.size(), CvType.CV_8UC3)
        println(cannyOut.size())

        for (i in contours.indices) {
            Imgproc.drawContours(drawing, contours, -1, scalarAwtColor(CvType.CV_8UC3, Color.WHITE))
            val moments = Imgproc.moments(contours[i])
            val ctrX = moments.m10 / moments.m00
            val ctrY = moments.m01 / moments.m00
            Imgproc.putText(drawing, i.toString(), Point(ctrX, ctrY),
                    Core.FONT_HERSHEY_SIMPLEX, 0.5, scalarAwtColor(CvType.CV_8UC3, Color.WHITE))
        }

        ImageIO.write(toBufferedImage(drawing), "jpg", File("puzzleOut.jpg"))
    }

    private fun getFilteredEdges(input: Mat): Mat {
        val filterOut = Mat()
        val colorOut = Mat()
        val finalOut = Mat()
        Imgproc.bilateralFilter(input, filterOut, 20, 75.0, 75.0)
        Imgproc.cvtColor(filterOut, colorOut, Imgproc.COLOR_RGB2GRAY)
        Imgproc.Canny(colorOut, finalOut, 150.0, 200.0)
        return finalOut
    }

    private fun toBufferedImage(m: Mat): BufferedImage {
        val gray = BufferedImage(m.width(), m.height(), BufferedImage.TYPE_3BYTE_BGR)
        val data = (gray.raster.dataBuffer as DataBufferByte).data
        m.get(0, 0, data)
        return gray
    }

    private fun scalarAwtColor(cvType: Int, color: Color): Scalar {
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