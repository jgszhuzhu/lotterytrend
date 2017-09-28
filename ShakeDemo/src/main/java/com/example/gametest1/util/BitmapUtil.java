package com.example.gametest1.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapUtil {
	
	
	/**
	 ****************************************************** 
	 * 
	 * @param bitmap
	 * @param w
	 * @param h
	 * @return
	 ****************************************************** 
	 */
	public static Bitmap resizeBitmap(Bitmap bitmap, int w) {
		try {
			if (bitmap != null) {
				int width = bitmap.getWidth();
				int height = bitmap.getHeight();
				int newWidth = w;
//				int newHeight = h;
				float scaleWidth = ((float) newWidth/3) / width;
				float scaleHeight = ((float) newWidth/3) / height;
				Matrix matrix = new Matrix();
				matrix.postScale(scaleWidth, scaleHeight);
				Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
						height, matrix, true);
				return resizedBitmap;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bitmap;
	}

}
