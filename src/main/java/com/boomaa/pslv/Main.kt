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
        val filteredMatrix = OCVMat(inputMatrix).Defaults()
                .bilateralFilter().cvtColor().canny()
                .getOCVMat().matrix
        val contours = OpenCV.findContours(filteredMatrix).contours
        val drawing = Mat.ones(filteredMatrix.size(), CvType.CV_8UC3)
        Imgproc.drawContours(drawing, contours, -1, scalarAwtColor(CvType.CV_8UC3, Color.WHITE))

        for (i in contours.indices) {
            //TODO separate contours into PuzzlePieces
            val moments = Imgproc.moments(contours[i])
            val ctrX = moments.m10 / moments.m00
            val ctrY = moments.m01 / moments.m00
            Imgproc.putText(drawing, i.toString(), Point(ctrX, ctrY),
                    Core.FONT_HERSHEY_SIMPLEX, 0.5, scalarAwtColor(CvType.CV_8UC3, Color.WHITE))
        }

        ImageIO.write(toBufferedImage(drawing), "jpg", File("puzzleOut.jpg"))
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