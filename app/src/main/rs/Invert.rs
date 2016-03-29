#pragma version(1)
#pragma rs java_package_name(com.dtodorov.darkroomnegative.ImageProcessing.filters)

#define MSG_TAG "InvertFromRenderScript"
#define GS_RED 0.299
#define GS_GREEN 0.587
#define  GS_BLUE 0.114


void root(const uchar4 *v_in, uchar4 *v_out) {
	float4 f4 = rsUnpackColor8888(*v_in);
	float3 output = {1.0f - f4.r, 1.0f - f4.g, 1.0f - f4.b};
	*v_out = rsPackColorTo8888(output);
}

void init(){
	rsDebug("Called init", rsUptimeMillis());
}