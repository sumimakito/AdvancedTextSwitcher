package sumimakito.android.advtextswitcher;

import android.content.Context;
import android.os.Handler;

public class Switcher
{
	private Context mContext;
	private AdvTextSwitcher advTsView;
	private boolean isPaused;
	private int mDuration=1000;

	public Switcher(Context context)
	{
		this.mContext = context;
	}

	public Switcher(Context context, AdvTextSwitcher view, int duration)
	{
		this.mContext = context;
		this.advTsView = view;
		this.mDuration = duration;
	}

	public Switcher setDuration(int duration)
	{
		this.mDuration = duration;
		return this;
	}

	public Switcher attach(AdvTextSwitcher view)
	{
		this.pause();
		this.advTsView = view;
		return this;
	}

	public void start()
	{
		isPaused = false;
		if (this.advTsView != null)
		{
			hlUpdt.postDelayed(rbUpdt, mDuration);
		}
	}

	public void pause()
	{
		isPaused = true;
	}

	public Handler hlUpdt = new Handler();

	public Runnable rbUpdt = new Runnable(){
		@Override
		public void run()
		{
			if (!isPaused && advTsView != null)
			{
				advTsView.next();
			}
			if (!isPaused) hlUpdt.postDelayed(this, mDuration);
		}
	};
}
