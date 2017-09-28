LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
OPENCV_CAMERA_MODULES:=on
OPENCV_INSTALL_MODULES:=on
LOCAL_MODULE    := codec-utils-jni
LOCAL_LDLIBS    := -llog
LOCAL_SRC_FILES := codec-utils-jni.cpp
include $(BUILD_SHARED_LIBRARY)