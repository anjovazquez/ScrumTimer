package com.avv.scrumtimer.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.avv.scrumtimer.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by angelvazquez on 7/5/16.
 */
public class CountdownTimerView extends View implements MediaPlayer.OnCompletionListener, ControlsView {

    Logger logger = Logger.getLogger(getClass().getName());

    private static final int START_ANGLE_POINT = 270;
    private static final int DEFAULT_SIZE = 150;
    private static final int DEFAULT_RATE = 15;
    private static final int DEFAULT_PADDING = 20;

    private int SIZE = DEFAULT_SIZE;
    private int RATE = DEFAULT_RATE;

    private Paint paint;
    private Paint outer;
    private TextPaint text;
    String dateText;
    PointF textPoint;
    boolean isFinished = false;
    boolean blink = false;

    private float angle = 0;
    private float oldAngle = 0;
    private RectF rect;

    private MediaPlayer mAlarm;

    boolean isStarted = false;
    long initCountDown = 15000;
    long stepTimeMillis = 100;
    long mMillisUntilFinished = initCountDown;

    private CountDownTimer countDownTimer;
    private float overtime = 0;

    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

    public CountdownTimerView(Context context) {
        super(context);
    }

    public CountdownTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(context, attrs);
        /*setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onCounterAction();
            }
        });*/
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(getContext());
        String ringtone = prefs.getString("ringtone", "r2d2whistle");

        if (!"silence".equals(ringtone))
            mAlarm = loadSound(getContext().getResources().getIdentifier(ringtone, "raw", getContext().getPackageName()));
    }

    private void onCounterAction() {
        if (isStarted) {
            countDownTimer.cancel();
            isStarted = false;
        } else {
            updateCountDownTimer(mMillisUntilFinished);
            countDownTimer.start();
            isStarted = true;
        }
    }

    private void onRestart() {
        countDownTimer.cancel();

        loadPreferences();
        text.setColor(getResources().getColor(R.color.colorAccent));
        overtime = 0;
        isFinished = false;
        isStarted = false;
        mMillisUntilFinished = initCountDown;
        angle = 0;
        oldAngle = 0;
        stopSound(mAlarm);

        date.setTime(mMillisUntilFinished);
        dateText = simpleDateFormat.format(date);
        invalidate();


    }

    public CountdownTimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttributes(context, attrs);
    }

    private void setAttributes(Context context, AttributeSet attrs) {

        loadPreferences();
        initPainters();

        date.setTime(mMillisUntilFinished);
        dateText = simpleDateFormat.format(date);
    }

    private void loadPreferences() {
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(getContext());
        initCountDown = prefs.getLong("timeshift", 60 * 5 * 1000);
        mMillisUntilFinished = initCountDown;
    }

    private void initPainters() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.colorAccent));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);

        outer = new Paint();
        outer.setAntiAlias(true);
        outer.setStyle(Paint.Style.STROKE);
        outer.setColor(Color.LTGRAY);

        text = new TextPaint();
        text.setStyle(Paint.Style.FILL);
        text.setAntiAlias(true);
        text.setColor(getResources().getColor(R.color.colorAccent));
        text.setTextSize(150);
        text.setTypeface(Typeface.create("Arial", Typeface.BOLD));

        setLayerType(LAYER_TYPE_SOFTWARE, null);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(w, h);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (w > h) {
            SIZE = h;
        } else {
            SIZE = w;
        }

        initMeasurements();
    }

    private void initMeasurements() {

        SIZE -= DEFAULT_PADDING;

        final int strokeWidth = SIZE / RATE;

        rect = new RectF(strokeWidth / 2 + strokeWidth + DEFAULT_PADDING, strokeWidth / 2 + strokeWidth + DEFAULT_PADDING, SIZE - (strokeWidth / 2 + strokeWidth), SIZE - (strokeWidth / 2 + strokeWidth));

        paint.setStrokeWidth(strokeWidth);
        outer.setStrokeWidth(strokeWidth / 2);

        calculateTextPosition();
        updateCountDownTimer(mMillisUntilFinished);
    }

    private void calculateTextPosition() {
        //Text Size Calculations
        RectF bounds = new RectF(rect);
        // measure text width
        bounds.right = text.measureText("mm:ss", 0, "mm:ss".length());
        // measure text height
        bounds.bottom = text.descent() - text.ascent();
        bounds.left += (rect.width() - bounds.right) / 2.0f;
        bounds.top += (rect.height() - bounds.bottom) / 2.0f;

        //text
        textPoint = new PointF(bounds.left, bounds.top - text.ascent());
    }

    private void updateCountDownTimer(final long millis) {

        final float increment = ((float) 360 / ((float) initCountDown / 100));


        countDownTimer = new CountDownTimer(millis, stepTimeMillis) {

            public void onTick(long millisUntilFinished) {
                angle = (initCountDown / 100 - millisUntilFinished / stepTimeMillis) * increment;

                if (angle > oldAngle) {
                    oldAngle = angle;
                    date.setTime(millisUntilFinished);
                    dateText = simpleDateFormat.format(date);
                    mMillisUntilFinished = millisUntilFinished;
                    logger.log(Level.INFO, angle + "");
                    invalidate();
                }
            }

            public void onFinish() {
                isFinished = true;
                update();
                playSound(mAlarm);
                Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(new long[]{0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500}, -1);
            }
        };
    }

    /************************************
     * Overtime count time
     *****************************************/
    //Overtime count time
    private final RefreshHandler mRedrawHandler = new RefreshHandler();

    private void update() {
        blink = blink ? false : true;
        overtime = overtime + 0.5f;
        date.setTime((long) overtime * 1000);
        dateText = simpleDateFormat.format(date);
        text.setColor(Color.RED);
        this.mRedrawHandler.sleep(500);
    }

    class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (isFinished) {
                update();
                invalidate();
            }
        }

        public void sleep(long delay) {
            this.removeMessages(0);
            this.sendMessageDelayed(this.obtainMessage(0), delay);
        }
    }
    /************************************    Overtime count time    *****************************************/


    /************************************
     * Draw stuff
     *****************************************/
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(rect, 0, 360, false, outer);
        if (isFinished) {
            if (blink) {
                canvas.drawText(dateText, textPoint.x, textPoint.y, text);
            }
        } else {
            canvas.drawText(dateText, textPoint.x, textPoint.y, text);
        }

        canvas.drawArc(rect, START_ANGLE_POINT, angle, false, paint);
    }
    /************************************    Draw stuff    *****************************************/


    /************************************
     * Sounds
     *****************************************/
    private MediaPlayer loadSound(int rid) {
        MediaPlayer mp = MediaPlayer.create(this.getContext(), rid);
        mp.setOnCompletionListener(this);
        return mp;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.seekTo(0);
    }

    private void playSound(MediaPlayer mp) {
        if (mp != null)
            if (!mp.isPlaying()) {
                mp.setVolume(0.2f, 0.2f);
                mp.start();
            }
    }

    private void stopSound(MediaPlayer mp) {
        if (mp != null)
            if (mp.isPlaying()) {
                mp.stop();
            }
    }

    @Override
    public void play() {
        onCounterAction();
    }

    @Override
    public void pause() {
        onCounterAction();
    }

    @Override
    public long stop() {
        long totalTime = 0;
        if(initCountDown - mMillisUntilFinished>=0){
            totalTime = initCountDown - mMillisUntilFinished;
        }
        else{
            totalTime = initCountDown + (long) overtime * 1000;
        }
        onRestart();
        return totalTime;
    }

    @Override
    public void next() {

    }
}
