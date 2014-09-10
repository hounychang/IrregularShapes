package com.houny.irregularshapes.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.houny.irregularshapes.R;

/**
 * 这是第一篇文章，这篇文章的是用遮罩从新绘制图片的方法，把两张图重新生成为一张效果图。
 * 作者也提到了，这样做是不合理的，有个非常严重的问题就就是内存
 * 如果两张图都非常的大，非常容易造成OOM
 * 但方法值得学习，怎么使用在下面的注释里
 * Created by Houny on 2014/9/10.
 */
public class PartOneFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_part_one,container,false);
        ImageView imageView = (ImageView)rootView.findViewById(R.id.part_one_imageView);
        Bitmap bitmap = BitmapFactory.decodeResource(container.getResources(), R.drawable.betty);
        Bitmap mask = BitmapFactory.decodeResource(container.getResources(), R.drawable.mask);
        imageView.setImageBitmap(combineImages(mask, bitmap));
        bitmap.recycle();
        mask.recycle();
        return rootView;
    }

    /**
     * 两张图片生成一张图片
     * @param showImage 要显示的图片  也就是要显示为圆角的照片
     * @param maskImage 遮罩  这里是一个圆角处是透明的图片
     * @return
     */
    public Bitmap combineImages(Bitmap showImage, Bitmap maskImage) {
        Bitmap bmp;


        int width = showImage.getWidth() > maskImage.getWidth() ? showImage.getWidth() : maskImage.getWidth();
        int height = showImage.getHeight() > maskImage.getHeight() ? showImage.getHeight() : maskImage.getHeight();
        //通过对比两张图片的大小，以最大的那张的尺寸，重新生成一张图片
        bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        //关于这个Mode的解释 参阅 http://blog.csdn.net/q445697127/article/details/7867529
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        Canvas canvas = new Canvas(bmp);
        canvas.drawBitmap(showImage, 0, 0, null);
        canvas.drawBitmap(maskImage, 0, 0, paint);
        //这里是把要显示的图片绘制在底层，遮罩绘制在上层，使用PorterDuff.Mode.SRC_ATOP后，生成的图片是，用底层的和上层的交集的部分（betty这条狗）加上上层的和底层不没有交集的部分（四个透明的脚）
        //这里就要求遮罩，也就是四角是透明的那张图片，中间有像素的区域正好和要实现的图像也就是那条狗一样大
        //这种方式的运用还有这个文章 http://lonesane.iteye.com/blog/791267
        return bmp;
    }
}
