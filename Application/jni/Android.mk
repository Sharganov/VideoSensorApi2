LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE    := codec-utils-jni
LOCAL_LDLIBS    := -llog
LOCAL_SRC_FILES := codec-utils-jni.cpp
include $(BUILD_SHARED_LIBRARY)