package jason.github.aotomtv1.ndkdemo2.utis2.utils;

import android.graphics.Bitmap;

/**
 * Created by LostJason on 2017/2/22.
 */

public class BitmapUtils {
    public static Bitmap convertToBlackWhite(Bitmap bmp) {  //转换成黑白图片
        int width = bmp.getWidth(); // 获取位图的宽
        int height = bmp.getHeight(); // 获取位图的高
        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);

/*        Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(newBmp, 380, 460);*/
        return newBmp;
    }
}