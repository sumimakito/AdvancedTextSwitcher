package sumimakito.android.advtextswitcher;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;

public class AdvTextSwitcher extends TextSwitcher
{
	private Context mContext;
	private String[] mTexts = {};
	private int currentPos;
	private AdvTextSwitcher.Callback mCallback = new Callback(){
		@Override
		public void onItemClick(int position){}
	};

	public AdvTextSwitcher(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.mContext = context;
		int[] attrsArray = new int[] {
			android.R.attr.textColor,
			R.attr.textSize,
			android.R.attr.inAnimation,
			android.R.attr.outAnimation,
			R.attr.gravity
		};
		TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);
		final int textColor = ta.getColor(0, Color.BLACK);
		final float textSize = ta.getDimensionPixelSize(1, 20);
		final int animInRes = ta.getResourceId(2, R.anim.fade_in_slide_in);
		final int animOutRes = ta.getResourceId(3, R.anim.fade_out_slide_out);
		boolean right = (ta.getInt(4, 0) & 0x01) == 0x01;
		boolean left = (ta.getInt(4, 0) & 0x02) == 0x02;
		boolean center = (ta.getInt(4, 0) & 0x03) == 0x03;
		final int gravity = center?Gravity.CENTER:
			(right?Gravity.RIGHT|Gravity.CENTER_VERTICAL:
					(left?(Gravity.LEFT|Gravity.CENTER_VERTICAL)
						:Gravity.NO_GRAVITY)
			);
		ta.recycle();
		this.setFactory(new ViewFactory() {
				public View makeView()
				{
					TextView innerText = new TextView(mContext);
					innerText.setGravity(gravity);
					innerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
					innerText.setTextColor(textColor);
					innerText.setOnClickListener(new OnClickListener(){
							@Override
							public void onClick(View p1)
							{
								AdvTextSwitcher.this.onClick();
							}
						});
					return innerText;
				}
			});

		Animation animIn = AnimationUtils.loadAnimation(mContext, animInRes);
		Animation animOut = AnimationUtils.loadAnimation(mContext, animOutRes);

		this.setInAnimation(animIn);
		this.setOutAnimation(animOut);
	}
	
	public void overrideText(String text){
		this.setText(text);
	}
	
	public void setAnim(int animInRes, int animOutRes){
		Animation animIn = AnimationUtils.loadAnimation(mContext, animInRes);
		Animation animOut = AnimationUtils.loadAnimation(mContext, animOutRes);
		this.setInAnimation(animIn);
		this.setOutAnimation(animOut);
	}
	
	public void setAnim(Animation animIn, Animation animOut){
		this.setInAnimation(animIn);
		this.setOutAnimation(animOut);
	}

	public void setTexts(String[] texts)
	{
		if (texts.length > 0)
		{
			this.mTexts = texts;
			this.currentPos = 0;
		}
		updateDisp();
	}
	
	public void setCallback(Callback callback){
		this.mCallback = callback;
	}

	public void next()
	{
		if (mTexts.length > 0)
		{
			if (currentPos < mTexts.length - 1)
			{
				currentPos++;
			}
			else
			{
				currentPos = 0;
			}
			updateDisp();
		}
	}

	public void previous()
	{
		if (mTexts.length > 0)
		{
			if (currentPos > 0)
			{
				currentPos--;
			}
			else
			{
				currentPos = mTexts.length - 1;
			}
			updateDisp();
		}
	}
	
	public interface Callback{
		public void onItemClick(int position);
	}

	private void updateDisp()
	{
		this.setText(mTexts[ currentPos ]);
	}
	
	private void onClick(){
		mCallback.onItemClick(currentPos);
	}
}