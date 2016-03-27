#pragma version(1)
#pragma rs java_package_name(com.dtodorov.darkroomnegative.ImageProcessing.filters)

#define MSG_TAG "GrayScaleFromRenderScript"
#define GS_RED 0.299
#define GS_GREEN 0.587
#define  GS_BLUE 0.114


void root(const uchar4 *v_in, uchar4 *v_out) {
	float4 f4 = rsUnpackColor8888(*v_in);

	float average = (f4.r * GS_RED + f4.g * GS_GREEN + f4.b * GS_BLUE);
	float3 output = {average, average, average};
	*v_out = rsPackColorTo8888(output);
}

void init(){
	rsDebug("Called init", rsUptimeMillis());
}