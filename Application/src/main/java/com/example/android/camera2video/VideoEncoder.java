package com.example.android.camera2video;

import android.media.Image;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Environment;
import android.util.Log;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.opencv.imgcodecs.Imgcodecs.imwrite;

public class VideoEncoder {

    private static final String TAG = "VideoEncoder";

    private int mWidth;
    private int mHeight;
    private int mBitRate;

    private static final int TIMEOUT_USEC = 10000;

    private static final String MIME_TYPE = "video/avc";
    private static final int FRAME_RATE = 30;
    // TODO: нормально оформить путь
    private static final String outputPath = "/test.mp4";

    // TODO: Разобраться c IFrame и inputSurface
    private static final int IFRAME_INTERVAL = 5;

    private MediaCodec mEncoder;
    private MediaMuxer mMuxer;
    private int mFrameCount;
    private int mVideoTrack = -1;
    private boolean mMuxerStarted = false;

    public VideoEncoder(int width, int height, int bitRate) {
        mWidth = width;
        mHeight = height;
        mBitRate = bitRate;
        mFrameCount = 0;
    }

    public void prepare() {
        MediaFormat mediaFormat = MediaFormat.createVideoFormat(MIME_TYPE, mWidth, mHeight);

        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible);
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, mBitRate);
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE);
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, IFRAME_INTERVAL);
        try {
            mEncoder = MediaCodec.createEncoderByType(MIME_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mEncoder.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        mEncoder.start();

        try {
            mMuxer = new MediaMuxer(Environment.getExternalStorageDirectory().getPath() + "/tmp.mp4", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMuxerStarted = false;
    }

    public void addImage(Image image, float[] rotationMatrix) {
        rotateImage(image, rotationMatrix);
        mFrameCount++;
//
//        int inputBufferId = mEncoder.dequeueInputBuffer(TIMEOUT_USEC);
//
//        if (inputBufferId >= 0) {
//            rotateImage(image, rotationMatrix);
////            ByteBuffer inputBuffer = mEncoder.getInputBuffer(inputBufferId);
////            int size = inputBuffer.remaining();
////            inputBuffer.put(bytes);
////            Log.d(TAG, Integer.toString(size));
//
////            Image inputImage = mEncoder.getInputImage(inputBufferId);
//
////            CodecUtils.copyFlexYUVImage(inputImage, image);
//
////            mEncoder.queueInputBuffer(inputBufferId, 0, size, mFrameCount * 2000000 / FRAME_RATE, 0);
//            mFrameCount++;
//        }
//        Log.d(TAG, "add" + Integer.toString(mFrameCount));
//        drainEncoder();
    }

    private void rotateImage(Image image, float[] rotationMatrix) {
        Log.d("rotmat", Float.toString(rotationMatrix[0]) + "x"
                + Float.toString(rotationMatrix[1]) + "x"
                + Float.toString(rotationMatrix[2]) + "x"
                + Float.toString(rotationMatrix[3]) + "x"
                + Float.toString(rotationMatrix[4]) + "x"
                + Float.toString(rotationMatrix[5]) + "x"
                + Float.toString(rotationMatrix[6]) + "x"
                + Float.toString(rotationMatrix[7]) + "x"
                + Float.toString(rotationMatrix[8]));
        Mat src = Imgcodecs.imread("/storage/emulated/0/colorgrid.jpg");
        Log.d("sz", Integer.toString(mWidth) + " " + Integer.toString(mHeight));
        float[] transformMatrix = TransformationMatrix.getTransformationMatrix(rotationMatrix, mWidth, mHeight, 800);
//        Mat srcYUV = ImageUtils.imageToMat(image);
//        Mat srcBRG = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
//        Imgproc.cvtColor(srcYUV, srcBRG, Imgproc.COLOR_YUV420sp2BGR);

        Mat rt = new Mat(3, 3, CvType.CV_32F);
        rt.put(0, 0, transformMatrix);
//
        Mat dst = MeshWarp.warp(src, rt);
//        Imgproc.cvtColor(dst, srcYUV, Imgproc.COLOR_BGR2YUV);
        imwrite("/storage/emulated/0/st" + mFrameCount + ".jpg", dst);
    }

    public void drainEncoder() {
        while (true) {
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int outputBufferId= mEncoder.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC);
            Log.d(TAG, "oid" + Integer.toString(outputBufferId));
            if (outputBufferId >= 0) {
                ByteBuffer outputBuffer = mEncoder.getOutputBuffer(outputBufferId);

                if (outputBuffer == null) {
                    Log.d(TAG, "output buffer was null");
                } else {
                    Log.d(TAG, Integer.toString(bufferInfo.size));
                }

                mMuxer.writeSampleData(mVideoTrack, outputBuffer, bufferInfo);
                mEncoder.releaseOutputBuffer(outputBufferId, false);
            } else if (outputBufferId == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                MediaFormat newFormat = mEncoder.getOutputFormat();
                mVideoTrack = mMuxer.addTrack(newFormat);
                mMuxer.start();
            } else {
                break;
            }
        }
    }

    public void release() {
        if (mEncoder != null) {
            mEncoder.stop();
            mEncoder.release();
            mEncoder = null;
        }
        if (mMuxer != null) {
            mMuxer.stop();
            mMuxer.release();
            mMuxer = null;
        }
        Log.d(TAG, "frames count: " + Integer.toString(mFrameCount));
    }
}
