LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := cupslice-library
LOCAL_SRC_FILES := cupslice-library.c

include $(BUILD_SHARED_LIBRARY)