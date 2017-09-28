package com.example.android.camera2video;

import android.media.Image;
import android.util.Log;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ImageTransform {

    public static byte[] perspectiveTransform(Image image, float[] transformMatrix) {
//        Log.d("ImageTransform", Float.toString(transformMatrix[0]) + "x"
//                + Float.toString(transformMatrix[1]) + "x"
//                + Float.toString(transformMatrix[2]) + "x"
//                + Float.toString(transformMatrix[3]) + "x"
//                + Float.toString(transformMatrix[4]) + "x"
//                + Float.toString(transformMatrix[5]) + "x"
//                + Float.toString(transformMatrix[6]) + "x"
//                + Float.toString(transformMatrix[7]) + "x"
//                + Float.toString(transformMatrix[8]));
        int height = image.getHeight();
        int width = image.getWidth();
        Mat bgr = new Mat(height, width, CvType.CV_8UC4);
//        Mat mYUV = new Mat(height + height / 2, width, CvType.CV_8UC1);

//        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Mat mYUV = ImageUtils.imageToMat(image);
        Imgproc.cvtColor(mYUV, bgr, Imgproc.COLOR_YUV2BGR_I420);
        Mat bgrr = new Mat(height, width, CvType.CV_8UC4);

//        Mat dst = mYUV.clone();

        Mat rotation = new Mat(3, 3, CvType.CV_32F);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                rotation.put(i, j, transformMatrix[3 * i + j]);
            }
        }

        Mat yuvOut = new Mat(height + height / 2, width, CvType.CV_8UC1);
//        Imgproc.warpPerspective(bgr, bgrr, rotation, new Size(height, width));
//        Imgproc.cvtColor(bgrr, mYUV, Imgproc.COLOR_BGR2YUV_I420);
//        Imgproc.cvtColor(bgrr, yuvOut, Imgproc.COLOR_BGR2YUV_I420);
//        final Bitmap bitmap = Bitmap.createBitmap(bgr.cols(), bgr.rows(), Bitmap.Config.ARGB_8888);
//        bitmap.b
        Log.d("bt", Integer.toString(height * width / 2));
        byte[] bytes = new byte[height * width * 3 / 2];
        mYUV.get(0, 0, bytes);

        return bytes;
    }
}
