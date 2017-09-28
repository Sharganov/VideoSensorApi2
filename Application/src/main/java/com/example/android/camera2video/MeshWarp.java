package com.example.android.camera2video;

import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.util.ArrayList;
import java.util.List;

public class MeshWarp {

    private static final String TAG = "MeshWarp";

    public static Mat warp(Mat src, Mat rotation) {
        Size size = src.size();
        int width = (int)size.width;
        int height = (int)size.height;
        Log.d(TAG, "Size: " + height + "x" + width);

        int type = src.type();
        Log.d(TAG, "Type: " + type);

        Mat dst = new Mat(size, type, new Scalar(0, 0, 0));
        Log.d(TAG, "Image base created.");

        Mat mx = new Mat(size, CvType.CV_32F);
        Mat my = new Mat(size, CvType.CV_32F);

        List<Point> target = new ArrayList<Point>(width * height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                target.add(new Point(i,j));
            }
        }
        Log.d(TAG, "Point list" + target.size());

        Mat src1 = Converters.vector_Point2d_to_Mat(target);
        Log.d(TAG, "src points size: " + src1.size().height + "x" + src1.size().width);
        Mat dst1 = new Mat(src1.size(), src1.type());

        Core.perspectiveTransform(src1, dst1, rotation);

        List<Mat> m = new ArrayList<Mat>(2);
        Core.split(dst1, m);

        Mat m0 = m.get(0).reshape(1, height);
        Mat m1 = m.get(1).reshape(1, height);

        m0.convertTo(mx, CvType.CV_32F);
        m1.convertTo(my, CvType.CV_32F);

        Imgproc.remap(src, dst, mx, my, Imgproc.INTER_LINEAR);
        return dst;
    }
}
