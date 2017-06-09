package com.example.user.layout.canvas;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.example.user.layout.sensors.Bluetooth;
import com.example.user.layout.R;
import com.example.user.layout.sensors.motionsensors;

public class Speedometer extends View implements SpeedChangeListener {
	private static final String TAG = Speedometer.class.getSimpleName();
	public static final float DEFAULT_MAX_SPEED = 200;
	Typeface type, type2;
	float maxSpeed = 0, avgSpeed = 60;


	// estado interno del Speedometer
	private float mMaxSpeed;
	private float mCurrentSpeed;
	private float currentBattery;
	private float mCurrentBatteryTemp;
	private float mCurrentMotorTemp;

	
	//drawing tools
	private Paint onMarkPaint;
	private Paint offMarkPaint;
	private Paint scalePaint;
	private Paint batPaint;
	private Paint readingPaint;
	private Paint smallScale;
	private Path onPath;
	private Path offPath;
	final RectF oval = new RectF();
	final RectF oval2 = new RectF();
	final RectF oval3 = new RectF();
	final RectF oval4 = new RectF();


	//colores
	private int ON_COLOR = Color.argb(255, 191, 187, 107);
	private int OFF_COLOR = Color.argb(255,0x3e,0x3e,0x3e);
	private int SCALE_COLOR = Color.argb(255, 255, 255, 255);
	private int BATTERY_COLOR = Color.argb(255, 160, 255, 158);
	private int VALUE_COLOR = Color.argb(255, 191, 187, 107);
	private int RED_COLOR = Color.argb(255, 166, 0, 0);
	private float SCALE_SIZE = 60f;
	private float READING_SIZE = 60f;
	
	//comfiguracion de escala
	private float centerX;
	private float centerY;
	private float radius;
	Matrix  mMatrix = new Matrix();
	Matrix  mMatrix2 = new Matrix();

	//bitmap de las imagenes de rotacion
	Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bikepos);
	Bitmap mBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.bikeposf);

	//constructor vacio
	public Speedometer(Context context){
		super(context);
	}

	//constructor
	public Speedometer(Context context, AttributeSet attrs) {
		super(context, attrs);
		//cramos typed array a base de el stylable que hemos declarado en attrs
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.Speedometer, 
				0, 0);
		//importamos las opciones del stylable correspondiente a este cnavas
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
		//inicializamos lo necesario para dibujar
		initDrawingTools();
		//inicializamos las fuentes
		type = Typeface.createFromAsset(context.getAssets(),"fonts/DS-DIGI.TTF");
		type2 = Typeface.createFromAsset(context.getAssets(),"fonts/GeosansLight.ttf");
	}
	//metodo que inicializa lo necesario para dibujar con cada pintura correspondiente
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

		smallScale = new Paint(scalePaint);
		smallScale.setStyle(Paint.Style.FILL_AND_STROKE);
		smallScale.setShadowLayer(0f, 0f, 0f, Color.YELLOW);
		smallScale.setTextSize(40);
		smallScale.setTypeface(type2);
		smallScale.setColor(Color.parseColor("#FF979797"));

		onPath = new Path();
		offPath = new Path();
	}

	//metodo que devuelve la velocidad actual
	public float getCurrentSpeed() {
		return mCurrentSpeed;
	}


	//metodo que se encarga de guardar la velocidad actual
	public void setCurrentSpeed(float mCurrentSpeed) {
		if(mCurrentSpeed > this.mMaxSpeed)
			this.mCurrentSpeed = mMaxSpeed;
		else if(mCurrentSpeed < 0)
			this.mCurrentSpeed = 0;
		else
			this.mCurrentSpeed = mCurrentSpeed;
	}

	//metodo que devuelve la bateria actual
	public float getCurrentBattery() {
		return currentBattery;
	}

	//metodo que se encarga de guardar la bateria actual
	public void setCurrentBattery(float mCurrentBattery) {
		if(mCurrentBattery > this.mMaxSpeed)
			this.currentBattery = mMaxSpeed;
		else if(mCurrentBattery < 0)
			this.currentBattery = 0;
		else
			this.currentBattery = mCurrentBattery;
	}
	//metodo que se encarga de guardar la temp de la bateria actual
	public void setCurrentBatteryTemp(float mCurrentBatteryTemp) {
		this.mCurrentBatteryTemp = mCurrentBatteryTemp;
	}
	//metodo que se encarga de guardar la temp del motor actual
	public void setCurrentMotorTemp(float mCurrentMotorTemp) {
		this.mCurrentMotorTemp = mCurrentMotorTemp;
	}

	//metodo que crear el ovalo del arean en la que el arco se va a dibujar
	@Override
	protected void onSizeChanged(int width, int height, int oldw, int oldh) {
		//ajustamos radio
		if (width > height){
			radius = height/3;
		}else{
			radius = width/3;
		}

		//limetes y medidas de los ovalos
		oval.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
		oval2.set(30, 30, 460, 460);
		oval3.set(1220, 20, 1520, 320);
		oval4.set(1530, 240, 1830, 540);
	}

	//metodo que crea y encuentra el centro del canvas donde va la circumferencia dibujada
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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
	//metodo que elege que dimensiones se van a usar
	private int chooseDimension(int mode, int size) {
		if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
			return size;
		} else {
			return getPreferredSize();
		} 
	}
	//si no existe ningun tamaño por defectop
	private int getPreferredSize() {
		return 300;
	}

	//metodo que dibuja el canvas cada frame
	@Override
	public void onDraw(Canvas canvas){
		MeasureVel();
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
		drawReading3(canvas);
		//battery temp
		drawScaleBackground4(canvas);
		drawScale4(canvas);
		drawReading4(canvas);
		//info
		drawInfo(canvas);

		//dibujamos los indicadores de inclinacion frontal y lateral
		mMatrix.setRotate (motionsensors.pitch+37, mBitmap.getWidth()/2, mBitmap.getHeight()/2);
		mMatrix.postTranslate(1650, 760);
		canvas.drawBitmap(mBitmap, mMatrix, null);

		mMatrix2.setRotate ((motionsensors.azimuth-88)*-1, mBitmap2.getWidth()/2, mBitmap2.getHeight()/2);
		mMatrix2.postTranslate(1650, 595);
		canvas.drawBitmap(mBitmap2, mMatrix2, null);


	}

	/**
	 * Draws the segments in their OFF state
	 * @param canvas
	 */
	//dibujar el fondo de el medidor de velocidad
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
	//dibujar la escala del medidor de velocidad
	private void drawScale(Canvas canvas){
		onPath.reset();
		for(int i = -180; i <= (mCurrentSpeed/mMaxSpeed)*180 - 180; i+=4){
			onPath.addArc(oval, i, 2f);
		}
		canvas.drawPath(onPath, onMarkPaint);
	}
	//dibujar la leyenda del medidor de velocidad
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
	//dibujar el texto del medidor de velocidad
	private void drawReading(Canvas canvas){
		String message = String.format("%d", (int)this.mCurrentSpeed);
		readingPaint.setTextSize(300);
		readingPaint.setTypeface(type);
		readingPaint.setColor(VALUE_COLOR);
		canvas.drawText(message, 770, 920, readingPaint);
		readingPaint.setTextSize(150);
		canvas.drawText("km/h", 1170, 920, readingPaint);
		//maxSpeed
		readingPaint.setTextSize(100);
		canvas.drawText("Max: "+Math.round(maxSpeed), 420, 920, readingPaint);
		canvas.drawText("AVG: "+Math.round(avgSpeed), 420, 820, readingPaint);
	}

	//metodos que actualizan los valores a mostrar
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
	@Override
	public void onMotorTempChanged(float newMotorTempValue) {
		this.setCurrentMotorTemp(newMotorTempValue);
		this.invalidate();
	}
	@Override
	public void onBatteryTempChanged(float newBatteryTempValue) {
		this.setCurrentBatteryTemp(newBatteryTempValue);
		this.invalidate();
	}

	//dibujar el fondo de el medidor de la bateria
	private void drawScaleBackground2(Canvas canvas){
		offPath.reset();
		for(int i = -360; i < 0; i+=4){
			offPath.addArc(oval2, i, 2f);
		}
		offMarkPaint.setColor(OFF_COLOR);
		canvas.drawPath(offPath, offMarkPaint);
	}
	//dibujar la escala del medidor de la bateria
	private void drawScale2(Canvas canvas){
		onPath.reset();
		for(int i = -360; i <= ((currentBattery*2)/mMaxSpeed)*360 - 360; i+=4){
			onPath.addArc(oval2, i, 2f);
		}

		if(getCurrentBattery() <= 30 && getCurrentBattery() > 15){
			offMarkPaint.setColor(Color.YELLOW);
			canvas.drawPath(onPath, offMarkPaint);
		}else if(getCurrentBattery() <= 15){
			offMarkPaint.setColor(Color.RED);
			canvas.drawPath(onPath, offMarkPaint);
		}else{
			offMarkPaint.setColor(OFF_COLOR);
			canvas.drawPath(onPath, batPaint);
		}
	}
	//dibujar el texto del medidor de la bateria
	private void drawReading2(Canvas canvas) {
		String message = String.format("%d", (int) this.currentBattery);
		readingPaint.setTextSize(150);
		readingPaint.setTypeface(type);
		readingPaint.setColor(BATTERY_COLOR);
		canvas.drawText(message + "%", 140, 310, readingPaint);
		readingPaint.setTextSize(50);
		readingPaint.setColor(VALUE_COLOR);
		canvas.drawText("Bike Battery", 110, 180, readingPaint);

	}
	//dibujar la info adicional
	private void drawInfo(Canvas canvas){
		smallScale.setTextSize(40);
		smallScale.setTypeface(type2);
		canvas.drawText("TEMP: "+ Bluetooth.s0+"ºC", 0, 750, smallScale);
		canvas.drawText("HUM: "+Bluetooth.s1+"%", 0, 800, smallScale);
		canvas.drawText("HGT: "+Bluetooth.s4+"m", 0, 850, smallScale);
		canvas.drawText("PA: "+Bluetooth.s3+"Hpa", 0, 900, smallScale);
	}

	//dibujar el fondo de temp de motor
	private void drawScaleBackground3(Canvas canvas){
		offPath.reset();
		for(int i = -360; i < 0; i+=4){
			offPath.addArc(oval3, i, 2f);
		}
		offMarkPaint.setColor(OFF_COLOR);
		canvas.drawPath(offPath, offMarkPaint);
	}
	//dibujar la escala de temp de motor
	private void drawScale3(Canvas canvas){
		onPath.reset();
		for(int i = -360; i <= ((mCurrentBatteryTemp*2)/mMaxSpeed)*360 - 360; i+=4){
			onPath.addArc(oval3, i, 2f);
		}

		if(mCurrentBatteryTemp <= 60 && mCurrentBatteryTemp > 30){
			offMarkPaint.setColor(Color.YELLOW);
			canvas.drawPath(onPath, offMarkPaint);
		}else if(mCurrentBatteryTemp <= 35){
			offMarkPaint.setColor(Color.RED);
			canvas.drawPath(onPath, offMarkPaint);
		}else{
			offMarkPaint.setColor(OFF_COLOR);
			canvas.drawPath(onPath, batPaint);
		}
	}
	//dibujar el texto de temp de motor
	private void drawReading3(Canvas canvas){
		String message = String.format("%d", (int)this.mCurrentMotorTemp);
		readingPaint.setTextSize(100);
		readingPaint.setTypeface(type);
		readingPaint.setColor(VALUE_COLOR);
		canvas.drawText(message+"ºC", 1600, 450, readingPaint);
		readingPaint.setTextSize(30);
		canvas.drawText("Motor Temp", 1610, 340, readingPaint);
	}
	//dibujar el fondo de temp de bateria
	private void drawScaleBackground4(Canvas canvas){
		offPath.reset();
		for(int i = -360; i < 0; i+=4){
			offPath.addArc(oval4, i, 2f);
		}
		offMarkPaint.setColor(OFF_COLOR);
		canvas.drawPath(offPath, offMarkPaint);
	}
	//dibujar la escala de temp de bateria
	private void drawScale4(Canvas canvas){
		onPath.reset();
		for(int i = -360; i <= ((mCurrentMotorTemp)*2/mMaxSpeed)*360 - 360; i+=4){
			onPath.addArc(oval4, i, 2f);
		}

		if(mCurrentMotorTemp <= 60 && mCurrentMotorTemp > 30){
			offMarkPaint.setColor(Color.YELLOW);
			canvas.drawPath(onPath, offMarkPaint);
		}else if(mCurrentMotorTemp <= 35){
			offMarkPaint.setColor(Color.RED);
			canvas.drawPath(onPath, offMarkPaint);
		}else{
			offMarkPaint.setColor(OFF_COLOR);
			canvas.drawPath(onPath, batPaint);
		}
	}
	//dibujar el texto de temp de bateria
	private void drawReading4(Canvas canvas){
		String message = String.format("%d", (int)this.mCurrentBatteryTemp);
		readingPaint.setTextSize(100);
		readingPaint.setTypeface(type);
		readingPaint.setColor(VALUE_COLOR);
		canvas.drawText(message+"ºC", 1290, 220, readingPaint);
		readingPaint.setTextSize(30);
		canvas.drawText("Battery temp", 1290, 120, readingPaint);
	}
	//mesuramos velocidad
	private void MeasureVel(){
		if(maxSpeed < mCurrentSpeed){
			maxSpeed = mCurrentSpeed;
			if(Bluetooth.max > maxSpeed){
				maxSpeed = Bluetooth.max;
			}else{
				Bluetooth.max = maxSpeed;
			}
		}
	}
}
