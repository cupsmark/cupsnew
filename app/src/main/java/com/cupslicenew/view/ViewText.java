package com.cupslicenew.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.cupslicenew.R;


public class ViewText extends TextView{

	Context mContext;
	boolean isAdjustSetting = false;
	int colorNormal = 0xFFFFFFFF, colorHover = 0xFF000000;

	public ViewText(Context context) {
		super(context);
		this.mContext = context;
	}

	public ViewText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		setFont();
	}
	public ViewText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		setFont();
	}

	public void setColorNormal(int color)
	{
		this.colorNormal = color;
	}

	public void setColorHover(int color)
	{
		this.colorHover = color;
	}

	public void setAdjustSetting(boolean adjust, int color)
	{
		this.isAdjustSetting = adjust;
		if(adjust)
		{
			setTextColor(colorNormal);
		}
	}

	public void setOnHover()
	{
		if(this.isAdjustSetting)
		{
			setTextColor(colorHover);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(isAdjustSetting)
				{
					setTextColor(colorHover);
				}
				break;

			case MotionEvent.ACTION_UP:
				setTextColor(colorNormal);
				break;
		}
		return false;
	}

	private void setFont() {
		
		Typeface font = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/Lato-Light.ttf");
		setTypeface(font, Typeface.NORMAL);
	}
	
	public void setBold()
	{
		Typeface font = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/Lato-Bold.ttf");
		setTypeface(font, Typeface.NORMAL);
	}
	
	public void setRegular()
	{
		Typeface font = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/Lato-Regular.ttf");
		setTypeface(font, Typeface.NORMAL);
	}
	
	public void setSemiBold()
	{
		Typeface font = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/Lato-Semibold.ttf");
		setTypeface(font, Typeface.NORMAL);
	}
	
}
