package com.example.android.camera2video;

import org.jblas.DoubleMatrix;

public class TransformationMatrix {

    public static double[] getTransformationMatrix(float[] rotationMatrix, int width, int height, int focalLength) {

        DoubleMatrix A = new DoubleMatrix(4, 3,
                1.0, 0, -width / 2,
                0, 1.0, -height / 2,
                0, 0, 0,
                0, 0, 1.0);

        DoubleMatrix T = new DoubleMatrix(4, 4,
                1.0, 0, 0, 0,
                0, 1.0, 0, 0,
                0, 0, 1.0, focalLength,
                0, 0, 0, 1.0);

        DoubleMatrix A1 = A.mul(T);

        DoubleMatrix A2 = new DoubleMatrix(3, 4,
                focalLength, 0, width / 2, 0,
                0, focalLength, height / 2, 0,
                0, 0, 1.0, 0);

        DoubleMatrix R = new DoubleMatrix(4, 4);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                R.put(i, j, (double) rotationMatrix[4 * j + i]);
            }
        }

        DoubleMatrix transform = A2.mul(R).mul(A1);

        double[] transformationMatrix = new double[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                transformationMatrix[3 * j + i] = transform.get(i, j);
            }
        }

        return transformationMatrix;
    }
}