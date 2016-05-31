package com.cupslicenew.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class ViewButton extends Button{

	public ViewButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ViewButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont();
	}
	public ViewButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFont();
	}

	private void setFont() {
		
		Typeface font = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/Lato-Light.ttf");
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

	public void setBold()
	{
		Typeface font = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/Lato-Bold.ttf");
		setTypeface(font, Typeface.NORMAL);
	}

	public void setItalic()
	{
		Typeface font = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/Lato-Italic.ttf");
		setTypeface(font, Typeface.NORMAL);
	}

	public void setMedium()
	{
		Typeface font = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/Lato-Medium.ttf");
		setTypeface(font, Typeface.NORMAL);
	}

}
