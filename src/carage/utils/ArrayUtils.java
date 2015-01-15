package carage.utils;

import java.util.ArrayList;

// TODO comment all methods
public class ArrayUtils {

	
	public static float[] arrayListToFloatArray(ArrayList<Float> list) {
		return arrayListToFloatArray(list, -1);
	}
	
	public static float[] arrayListToFloatArray(ArrayList<Float> list, float nullValue) {
		float[] array = new float[list.size()];
		for (int i=0; i<list.size(); ++i) {
			Float fv = list.get(i);
			array[i] = (fv == null) ? nullValue : (float) fv;
		}
		return array;
	}
	
	public static int[] arrayListToIntArray(ArrayList<Integer> list) {
		return arrayListToIntArray(list, -1);
	}
	
	public static int[] arrayListToIntArray(ArrayList<Integer> list, int nullValue) {
		int[] array = new int[list.size()];
		for (int i=0; i<list.size(); ++i) {
			Integer iv = list.get(i);
			array[i] = (iv == null) ? nullValue : (int) iv;
		}
		return array;
	}

}
