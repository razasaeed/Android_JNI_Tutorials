#include <jni.h>
#include <string>
#include <android/log.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_irhammuch_android_jni_1project_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jint JNICALL
Java_com_irhammuch_android_jni_1project_MainActivity_sumFromJni(
        JNIEnv* env,
        jobject /* this */,
        jint a,
        jint b) {
    return a+b;
}

extern "C" JNIEXPORT jintArray JNICALL
Java_com_irhammuch_android_jni_1project_MainActivity_convertToGrayscale(
        JNIEnv* env,
        jobject /* this */,
        jintArray pixels,
        jint width,
        jint height) {

    jint *pixelsData = env->GetIntArrayElements(pixels, NULL);

    for(int i=0; i<width * height; i++) {
        jint alpha = (pixelsData[i] & 0xFF000000);
        jint red = (pixelsData[i] & 0x00FF0000) >> 16;
        jint green = (pixelsData[i] & 0x0000FF00) >> 8;
        jint blue = (pixelsData[i] & 0x000000FF);

        // Convert to grayscale using luminosity method
        jint gray = (red * 0.21 + green * 0.72 + blue * 0.07);
        gray = alpha | (gray << 16) | (gray << 8) | gray;

        pixelsData[i] = gray;
    }

    jintArray results = env->NewIntArray(width * height);
    env->SetIntArrayRegion(results, 0, width * height, pixelsData);

    env->ReleaseIntArrayElements(pixels, pixelsData, 0);
    return results;

}