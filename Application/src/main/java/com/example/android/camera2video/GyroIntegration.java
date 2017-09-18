package com.example.android.camera2video;

import android.hardware.SensorManager;
import android.util.Log;

public class GyroIntegration {

    private static final String TAG = "GyroIntegration";
    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];
    private long mLastTimestamp = -1;
    private float EPSILON = 0.000000001f;

    public GyroIntegration() {
        Log.d(TAG, "gyro integrator inizialization");
    }

    public void newData(float x, float y, float z, long timestamp) {
        Log.d(TAG, "New gyro");
        if (mLastTimestamp >= 0) {
            mLastTimestamp = timestamp;
            return;
        }

        float dt = (timestamp - mLastTimestamp) * NS2S;
        float omegaMagnitude = (float) Math.sqrt(x * x + y * y + z * z);

        if (omegaMagnitude > EPSILON) {
            x /= omegaMagnitude;
            y /= omegaMagnitude;
            z /= omegaMagnitude;
        }

        double thetaOverTwo = omegaMagnitude * dt / 2.0f;
        float sinThetaOverTwo = (float) Math.sin(thetaOverTwo);
        float cosThetaOverTwo = (float) Math.cos(thetaOverTwo);
        deltaRotationVector[0] = sinThetaOverTwo * x;
        deltaRotationVector[1] = sinThetaOverTwo * y;
        deltaRotationVector[2] = sinThetaOverTwo * z;
        deltaRotationVector[3] = cosThetaOverTwo;

        mLastTimestamp = timestamp;
    }

    public float[] getRotationMatrix() {
        float[] matrix = new float[9];
        SensorManager.getRotationMatrixFromVector(matrix, deltaRotationVector);
        return matrix;
    }
}
