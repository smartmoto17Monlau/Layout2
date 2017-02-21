package com.example.user.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class Speedometer extends View implements SpeedChangeListener {
	private static final String TAG = Speedometer.class.getSimpleName();
	public static final float DEFAULT_MAX_SPEED = 200; // Assuming this is km/h and you drive a super-car
	Typeface type;


	// Speedometer internal state
	private float mMaxSpeed;
	private float mCurrentSpeed;
	private float currentBattery;
	
	// Scale drawing tools
	private Paint onMarkPaint;
	private Paint offMarkPaint;
	private Paint scalePaint;
	private Paint batPaint;
	private Paint readingPaint;
	private Path onPath;
	private Path offPath;
	final RectF oval = new RectF();
	final RectF oval2 = new RectF();
	final RectF oval3 = new RectF();
	final RectF oval4 = new RectF();

	
	// Drawing colors

	//private int ON_COLOR = Color.argb(255, 0xff, 0xA5, 0x00);
	private int ON_COLOR = Color.argb(255, 191, 187, 107);
	private int OFF_COLOR = Color.argb(255,0x3e,0x3e,0x3e);
	private int SCALE_COLOR = Color.argb(255, 255, 255, 255);
	private int BATTERY_COLOR = Color.argb(255, 160, 255, 158);
	private int VALUE_COLOR = Color.argb(255, 191, 187, 107);
	private int RED_COLOR = Color.argb(255, 166, 0, 0);
	private float SCALE_SIZE = 60f;
	private float READING_SIZE = 60f;
	
	// Scale configuration
	private float centerX;
	private float centerY;
	private float radius;

	public Speedometer(Context context){
		super(context);
	}
	
	public Speedometer(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, 
				R.styleable.Speedometer, 
				0, 0);
		try{
			mMaxSpeed = a.getFloat(R.styleable.Speedometer_maxSpeed, DEFAULT_MAX_SPEED);
			mCurrentSpeed = a.getFloat(R.styleable.Speedometer_currentSpeed, 0);
			currentBattery = a.getFloat(R.styleable.Speedometer_currentSpeed, 0);
			ON_COLOR = a.getColor(R.styleable.Speedometer_onColor, ON_COLOR);
			OFF_COLOR = a.getColor(R.styleable.Speedometer_offColor, OFF_COLOR);
			SCALE_COLOR = a.getColor(R.styleable.Speedometer_scaleColor, SCALE_COLOR);
			SCALE_SIZE = a.getDimension(R.styleable.Speedometer_scaleTextSize, SCALE_SIZE);
			READING_SIZE = a.getDimension(R.styleable.Speedometer_readingTextSize, READING_SIZE);
		} finally{
			a.recycle();
		}
		initDrawingTools();
		type = Typeface.createFromAsset(context.getAssets(),"fonts/DS-DIGI.TTF");
	}
	
	private void initDrawingTools(){
		onMarkPaint = new Paint();
		onMarkPaint.setStyle(Paint.Style.STROKE);
		onMarkPaint.setColor(ON_COLOR);
		onMarkPaint.setStrokeWidth(35f);
		onMarkPaint.setShadowLayer(5f, 0f, 0f, ON_COLOR);
		onMarkPaint.setAntiAlias(true);
		
		offMarkPaint = new Paint(onMarkPaint);
		offMarkPaint.setColor(OFF_COLOR);
		offMarkPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		offMarkPaint.setShadowLayer(0f, 0f, 0f, OFF_COLOR);

		batPaint = new Paint(onMarkPaint);
		batPaint.setColor(BATTERY_COLOR);
		batPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		batPaint.setShadowLayer(0f, 0f, 0f, OFF_COLOR);
		
		scalePaint = new Paint(offMarkPaint);
		scalePaint.setStrokeWidth(2f);
		scalePaint.setTextSize(SCALE_SIZE);
		scalePaint.setShadowLayer(5f, 0f, 0f, Color.RED);
		scalePaint.setColor(SCALE_COLOR);
		
		readingPaint = new Paint(scalePaint);
		readingPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		offMarkPaint.setShadowLayer(3f, 0f, 0f, Color.WHITE);
		readingPaint.setTextSize(65f);
		readingPaint.setTypeface(Typeface.SANS_SERIF);
		readingPaint.setColor(Color.WHITE);
		
		onPath = new Path();
		offPath = new Path();
	}
	
	public float getCurrentSpeed() {
		return mCurrentSpeed;
	}

	public void setCurrentSpeed(float mCurrentSpeed) {
		if(mCurrentSpeed > this.mMaxSpeed)
			this.mCurrentSpeed = mMaxSpeed;
		else if(mCurrentSpeed < 0)
			this.mCurrentSpeed = 0;
		else
			this.mCurrentSpeed = mCurrentSpeed;
	}

	public float getCurrentBattery() {
		return currentBattery;
	}

	public void setCurrentBattery(float mCurrentBattery) {
		if(mCurrentBattery > this.mMaxSpeed)
			this.currentBattery = mMaxSpeed;
		else if(mCurrentBattery < 0)
			this.currentBattery = 0;
		else
			this.currentBattery = mCurrentBattery;
	}
	
	@Override
	protected void onSizeChanged(int width, int height, int oldw, int oldh) {
		
		// Setting up the oval area in which the arc will be drawn
		if (width > height){
			radius = height/3;
		}else{
			radius = width/3;
		}
		oval.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
		oval2.set(30, 30, 460, 460);
		oval3.set(1220, 20, 1520, 320);
		oval4.set(1530, 300, 1830, 600);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		Log.d(TAG, "Width spec: " + MeasureSpec.toString(widthMeasureSpec));
//		Log.d(TAG, "Height spec: " + MeasureSpec.toString(heightMeasureSpec));
		
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		int chosenWidth = chooseDimension(widthMode, widthSize);
		int chosenHeight = chooseDimension(heightMode, heightSize);
		
		int chosenDimension = Math.min(chosenWidth, chosenHeight);
		centerX = chosenDimension / 2;
		centerY = chosenDimension / 2;
		setMeasuredDimension(chosenDimension, chosenDimension);
	}
	
	private int chooseDimension(int mode, int size) {
		if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
			return size;
		} else { // (mode == MeasureSpec.UNSPECIFIED)
			return getPreferredSize();
		} 
	}
	
	// in case there is no size specified
	private int getPreferredSize() {
		return 300;
	}
	
	@Override
	public void onDraw(Canvas canvas){
		//canvas.drawColor(Color.BLUE);
		//velocity
		drawScaleBackground(canvas);
		drawScale(canvas);
		drawLegend(canvas);
		drawReading(canvas);
		//battery
		drawScaleBackground2(canvas);
		drawScale2(canvas);
		drawReading2(canvas);
		//motor temp
		drawScaleBackground3(canvas);
		drawScale3(canvas);
		//drawReading2(canvas);
		//battery temp
		drawScaleBackground4(canvas);
		drawScale4(canvas);
		//drawReading2(canvas);
	}

	/**
	 * Draws the segments in their OFF state
	 * @param canvas
	 */
	private void drawScaleBackground(Canvas canvas){
		offPath.reset();
		for(int i = -180; i < 0; i+=4){
			offPath.addArc(oval, i, 2f);
		}
		if(getCurrentSpeed() >= 140){
			offMarkPaint.setColor(RED_COLOR);
			canvas.drawPath(offPath, offMarkPaint);
		}else{
			offMarkPaint.setColor(OFF_COLOR);
			canvas.drawPath(offPath, offMarkPaint);
		}

	}
	
	private void drawScale(Canvas canvas){
		onPath.reset();
		for(int i = -180; i <= (mCurrentSpeed/mMaxSpeed)*180 - 180; i+=4){
			onPath.addArc(oval, i, 2f);
		}
		canvas.drawPath(onPath, onMarkPaint);
	}
	
	private void drawLegend(Canvas canvas){
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.rotate(-180, centerX,centerY);
		Path circle = new Path();
		double halfCircumference = radius * Math.PI;
		double increments = 20;
		scalePaint.setTypeface(type);
		for(int i = 0; i < this.mMaxSpeed; i += increments){
			circle.addCircle(centerX, centerY, radius, Path.Direction.CW);
			canvas.drawTextOnPath(String.format("%d", i), 
								circle, 
								(float) (i*halfCircumference/this.mMaxSpeed), 
								-30f,
								scalePaint);}
		canvas.restore();
	}
	
	private void drawReading(Canvas canvas){
		String message = String.format("%d", (int)this.mCurrentSpeed);
		readingPaint.setTextSize(300);
		readingPaint.setTypeface(type);
		readingPaint.setColor(VALUE_COLOR);
		canvas.drawText(message, 610, 920, readingPaint);
		readingPaint.setTextSize(150);
		canvas.drawText("km/h", 1000, 920, readingPaint);
	}

	@Override
	public void onSpeedChanged(float newSpeedValue) {
		this.setCurrentSpeed(newSpeedValue);
		this.invalidate();
	}
	@Override
	public void onBatteryChanged(float newBatValue) {
		this.setCurrentBattery(newBatValue);
		this.invalidate();
	}



	private void drawScaleBackground2(Canvas canvas){
		offPath.reset();
		for(int i = -360; i < 0; i+=4){
			offPath.addArc(oval2, i, 2f);
		}
		offMarkPaint.setColor(OFF_COLOR);
		canvas.drawPath(offPath, offMarkPaint);
	}

	private void drawScale2(Canvas canvas){
		onPath.reset();
		for(int i = -360; i <= (currentBattery/mMaxSpeed)*360 - 360; i+=4){
			onPath.addArc(oval2, i, 2f);
		}

		if(getCurrentBattery() <= 60 && getCurrentBattery() > 30){
			offMarkPaint.setColor(Color.YELLOW);
			canvas.drawPath(onPath, offMarkPaint);
		}else if(getCurrentBattery() <= 35){
			offMarkPaint.setColor(Color.RED);
			canvas.drawPath(onPath, offMarkPaint);
		}else{
			offMarkPaint.setColor(OFF_COLOR);
			canvas.drawPath(onPath, batPaint);
		}
	}
	private void drawReading2(Canvas canvas){


		String message = String.format("%d", (int)this.currentBattery / 2);
		readingPaint.setTextSize(150);
		readingPaint.setTypeface(type);
		readingPaint.setColor(BATTERY_COLOR);
		canvas.drawText(message+"%", 140, 295, readingPaint);
	}

	private void drawScaleBackground3(Canvas canvas){
		offPath.reset();
		for(int i = -360; i < 0; i+=4){
			offPath.addArc(oval3, i, 2f);
		}
		offMarkPaint.setColor(OFF_COLOR);
		canvas.drawPath(offPath, offMarkPaint);
	}

	private void drawScale3(Canvas canvas){
		onPath.reset();
		for(int i = -360; i <= (currentBattery/mMaxSpeed)*360 - 360; i+=4){
			onPath.addArc(oval3, i, 2f);
		}

		if(getCurrentBattery() <= 60 && getCurrentBattery() > 30){
			offMarkPaint.setColor(Color.YELLOW);
			canvas.drawPath(onPath, offMarkPaint);
		}else if(getCurrentBattery() <= 35){
			offMarkPaint.setColor(Color.RED);
			canvas.drawPath(onPath, offMarkPaint);
		}else{
			offMarkPaint.setColor(OFF_COLOR);
			canvas.drawPath(onPath, batPaint);
		}
	}
	private void drawReading3(Canvas canvas){


		String message = String.format("%d", (int)this.currentBattery / 2);
		readingPaint.setTextSize(150);
		readingPaint.setTypeface(type);
		readingPaint.setColor(BATTERY_COLOR);
		canvas.drawText(message+"%", 140, 295, readingPaint);
	}

	private void drawScaleBackground4(Canvas canvas){
		offPath.reset();
		for(int i = -360; i < 0; i+=4){
			offPath.addArc(oval4, i, 2f);
		}
		offMarkPaint.setColor(OFF_COLOR);
		canvas.drawPath(offPath, offMarkPaint);
	}

	private void drawScale4(Canvas canvas){
		onPath.reset();
		for(int i = -360; i <= (currentBattery/mMaxSpeed)*360 - 360; i+=4){
			onPath.addArc(oval4, i, 2f);
		}

		if(getCurrentBattery() <= 60 && getCurrentBattery() > 30){
			offMarkPaint.setColor(Color.YELLOW);
			canvas.drawPath(onPath, offMarkPaint);
		}else if(getCurrentBattery() <= 35){
			offMarkPaint.setColor(Color.RED);
			canvas.drawPath(onPath, offMarkPaint);
		}else{
			offMarkPaint.setColor(OFF_COLOR);
			canvas.drawPath(onPath, batPaint);
		}
	}
	private void drawReading4(Canvas canvas){


		String message = String.format("%d", (int)this.currentBattery / 2);
		readingPaint.setTextSize(150);
		readingPaint.setTypeface(type);
		readingPaint.setColor(BATTERY_COLOR);
		canvas.drawText(message+"%", 140, 295, readingPaint);
	}
}
