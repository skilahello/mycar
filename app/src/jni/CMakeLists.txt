
cmake_minimum_required(VERSION 3.4.1)



set(ncnn_DIR ${CMAKE_SOURCE_DIR}/${ANDROID_ABI}/lib/cmake/ncnn)
find_package(ncnn REQUIRED)

set(OpenCV_DIR ${CMAKE_SOURCE_DIR}/opencv-mobile-4.5.4-android/sdk/native/jni)
find_package(OpenCV REQUIRED core imgproc)


add_library(yolov5ncnn SHARED paddleocr_ncnn.cpp native-lib.cpp yolov5ncnn_jni.cpp common.cpp clipper.cpp pingmujiequ.cpp tuxing.cpp)

target_link_libraries(
        yolov5ncnn
        ncnn
        ${OpenCV_LIBS}
        jnigraphics
)