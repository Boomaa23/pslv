package com.boomaa.pslv

import org.opencv.core.*
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.awt.image.DataBufferInt
import java.io.File
import javax.imageio.ImageIO


object Main {
    init {
        nu.pattern.OpenCV.loadLocally()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val inputMatrix = Imgcodecs.imread("puzzle.jpg") as Mat
        val filteredMatrix = OCVMat(inputMatrix)
                .bilateralFilter().cvtColor().cornerHarris().matrix
        val contours = OpenCV.findContours(filteredMatrix).contours
        val drawing = Mat.ones(filteredMatrix.size(), CvType.CV_8UC3)
        val blocks = OpenCV.approxPolyDP(contours, 0.05);
        Imgproc.drawContours(drawing, contours, -1, Util.scalarAwtColor(CvType.CV_8UC3, Color.WHITE))
        Imgproc.drawContours(drawing, OpenCV.findContours(OCVMat(inputMatrix)
                .bilateralFilter().cvtColor().canny().matrix).contours, -1, Util.scalarAwtColor(CvType.CV_8UC3, Color.WHITE))

        val pieces = ArrayList<PuzzlePiece>()
        for (i in contours.indices) {
            pieces.add(PuzzlePiece(contours[i]))
            val moments = Imgproc.moments(contours[i])
            val ctrX = moments.m10 / moments.m00
            val ctrY = moments.m01 / moments.m00
            Imgproc.putText(drawing, i.toString(), Point(ctrX, ctrY),
                    Core.FONT_HERSHEY_SIMPLEX, 0.5, Util.scalarAwtColor(CvType.CV_8UC3, Color.WHITE))
        }

        ImageIO.write(toBufferedImage(drawing), "jpg", File("puzzleOut.jpg"))
    }

    private fun toBufferedImage(m: Mat): BufferedImage {
        val gray = BufferedImage(m.width(), m.height(), BufferedImage.TYPE_3BYTE_BGR)
        val data = (gray.raster.dataBuffer as DataBufferByte).data
        m.get(0, 0, data)
        return gray
    }
}