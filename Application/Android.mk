LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
LOCAL_SRC_FILES := \
    src/main/java/com/example/android/camera2video/CodecImage.java \
	src/main/java/com/example/android/camera2video/CodecUtils.java \
include $(BUILD_STATIC_JAVA_LIBRARY)
include $(call all-makefiles-under,$(LOCAL_PATH))