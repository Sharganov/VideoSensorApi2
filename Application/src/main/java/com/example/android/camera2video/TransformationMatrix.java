package com.example.android.camera2video;

import android.util.Log;

public class TransformationMatrix {

    public static float[] getTransformationMatrix(float[] rotationMatrix, int width, int height, int focalLength) {

        float[] A = {
                1.0f, 0, 0, 0,
                0, 1.0f, 0, 0,
                -width / 2, -height / 2, 0, 1.0f,
                0, 0, 0, 0
        };

        float[] T = {
                1.0f, 0, 0, 0,
                0, 1.0f, 0, 0,
                0, 0, 1.0f, 0,
                0, 0, focalLength, 1.0f
        };

        float[] A1 = new float[16];
        android.opengl.Matrix.multiplyMM(A1, 0, T, 0, A, 0);

        Log.d("a1", Float.toString(A[0]) + "x"
                + Float.toString(A[1]) + "x"
                + Float.toString(A[2]) + "x"
                + Float.toString(A[3]) + "x"
                + Float.toString(A[4]) + "x"
                + Float.toString(A1[5]) + "x"
                + Float.toString(A1[6]) + "x"
                + Float.toString(A1[7]) + "x"
                + Float.toString(A1[8]) + "x"
                + Float.toString(A1[9]) + "x"
                + Float.toString(A1[10]) + "x"
                + Float.toString(A1[11]) + "x"
                + Float.toString(A1[12]) + "x"
                + Float.toString(A1[13]) + "x"
                + Float.toString(A1[14]) + "x"
                + Float.toString(A1[15]) + "x");

        float[] A2 = {
            focalLength, 0, 0, 0,
                0, focalLength, 0, 0,
                width / 2, height / 2, 1.0f, 0,
                0, 0, 0, 0
        };

        int angle = 10;
        float c = (float)Math.cos(angle * 3.14 / 180);
        float s = (float)Math.sin(angle * 3.14 / 180);

        float[] rotationMatrix1 = {
                1.0f, 0, 0, 0,
                0, c, s, 0,
                0, -s, c, 0,
                0, 0, 0, 1.0f
        };

        float[] A3 = new float[16];
        android.opengl.Matrix.multiplyMM(A3, 0, A2, 0, rotationMatrix1, 0);

        Log.d("a3", Float.toString(A3[0]) + "x"
                + Float.toString(A3[1]) + "x"
                + Float.toString(A3[2]) + "x"
                + Float.toString(A3[3]) + "x"
                + Float.toString(A3[4]) + "x"
                + Float.toString(A3[5]) + "x"
                + Float.toString(A3[6]) + "x"
                + Float.toString(A3[7]) + "x"
                + Float.toString(A3[8]) + "x"
                + Float.toString(A3[9]) + "x"
                + Float.toString(A3[10]) + "x"
                + Float.toString(A3[11]) + "x"
                + Float.toString(A3[12]) + "x"
                + Float.toString(A3[13]) + "x"
                + Float.toString(A3[14]) + "x"
                + Float.toString(A3[15]));

        float[] A4 = new float[16];
        android.opengl.Matrix.multiplyMM(A4, 0, A3, 0, A1, 0);

        Log.d("a4", Float.toString(A4[0]) + "x"
                + Float.toString(A4[1]) + "x"
                + Float.toString(A4[2]) + "x"
                + Float.toString(A4[3]) + "x"
                + Float.toString(A4[4]) + "x"
                + Float.toString(A4[5]) + "x"
                + Float.toString(A4[6]) + "x"
                + Float.toString(A4[7]) + "x"
                + Float.toString(A4[8]) + "x"
                + Float.toString(A4[9]) + "x"
                + Float.toString(A4[10]) + "x"
                + Float.toString(A4[11]) + "x"
                + Float.toString(A4[12]) + "x"
                + Float.toString(A4[13]) + "x"
                + Float.toString(A4[14]) + "x"
                + Float.toString(A4[15]));

        float[] transformationMatrix = new float[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                transformationMatrix[3 * i + j] = A4[4 * i + j];
            }
        }

        Log.d("floatdeb", Float.toString(width * width * focalLength / 2));

        return transformationMatrix;
    }
}