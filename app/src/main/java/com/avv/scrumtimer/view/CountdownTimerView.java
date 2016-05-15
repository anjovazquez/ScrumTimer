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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by angelvazquez on 7/5/16.
 */
public class CountdownTimerView extends View implements MediaPlayer.OnCompletionListener{


    public CountdownTimerView(Context context) {
        super(context);
    }

    public CountdownTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(context, attrs);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setColor(Color.BLUE);
                overtime = 0;
                if (!isFinished) {
                    if (isStarted) {
                        countDownTimer.cancel();
                        isStarted = false;
                    } else {
                        updateCountDownTimer(mMillisUntilFinished);
                        countDownTimer.start();
                        isStarted = true;
                    }
                } else {
                    isFinished = false;
                    mMillisUntilFinished = initCountDown;
                    angle = 0;
                    oldAngle = 0;
                    stopSound(mAlarm);
                }
            }
        });
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(getContext());
        String ringtone = prefs.getString("ringtone", "r2d2whistle");

        mAlarm = loadSound(getContext().getResources().getIdentifier(ringtone, "raw", getContext().getPackageName()));
    }

    private MediaPlayer mAlarm;

    public CountdownTimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttributes(context, attrs);

    }

    private static final int START_ANGLE_POINT = 270;

    private float angle = 0;
    private float oldAngle = 0;
    private Paint paint;
    private Paint outer;
    private TextPaint text;

    private void setAttributes(Context context, AttributeSet attrs) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);

        outer = new Paint();
        outer.setAntiAlias(true);
        outer.setStyle(Paint.Style.STROKE);
        outer.setColor(Color.LTGRAY);

        text = new TextPaint();
        text.setStyle(Paint.Style.FILL);
        text.setAntiAlias(true);
        text.setColor(Color.BLUE);
        text.setTextSize(150);
        /*Typeface font = Typeface.createFromAsset(
                getContext().getAssets(),
                "fonts/TENOCLOCK-Regular.ttf");
        text.setTypeface(font);*/
        text.setTypeface(Typeface.create("Arial", Typeface.BOLD));

        setLayerType(LAYER_TYPE_SOFTWARE, null);

        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(getContext());
        initCountDown = prefs.getLong("timeshift", 60*5*1000);
        mMillisUntilFinished = initCountDown;

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        date.setTime(mMillisUntilFinished);
        dateText = simpleDateFormat.format(date);
    }

    private static final int DEFAULT_SIZE = 150;
    private static final int DEFAULT_RATE = 15;
    private static final int DEFAULT_PADDING = 20;

    private int SIZE = DEFAULT_SIZE;
    private int RATE = DEFAULT_RATE;

    private RectF rect;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(w, h);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //make sure square
        if (w > h) {
            SIZE = h;
        } else {
            SIZE = w;
        }

        initMeasurements();
    }

    boolean isStarted = false;
    long initCountDown = 15000;
    long stepTimeMillis = 100;
    long mMillisUntilFinished = initCountDown;

    private void initMeasurements() {

        SIZE -= DEFAULT_PADDING;

        final int strokeWidth = SIZE / RATE;

        //animation
        rect = new RectF(strokeWidth / 2 + strokeWidth + DEFAULT_PADDING, strokeWidth / 2 + strokeWidth + DEFAULT_PADDING, SIZE - (strokeWidth / 2 + strokeWidth), SIZE - (strokeWidth / 2 + strokeWidth));

        paint.setStrokeWidth(strokeWidth);
        outer.setStrokeWidth(strokeWidth / 2);
        //text.setTextSize(TEXT_SIZE);

        calculateTextPosition();
        updateCountDownTimer(mMillisUntilFinished);
    }


    private CountDownTimer countDownTimer;
    private float overtime = 0;

    private void updateCountDownTimer(final long millis) {

        final float increment = ((float) 360 / ((float) initCountDown / 100));


        countDownTimer = new CountDownTimer(millis, stepTimeMillis) {



            public void onTick(long millisUntilFinished) {
                //angle = (60 - (float)millisUntilFinished/(float)1000)*((float)360/(float)60);
                //angle = (600 - millisUntilFinished/100)*((float)360/(float)600);
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

    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

    private void update() {
        blink = blink ? false : true;
        overtime = overtime + 0.5f;
        date.setTime((long)overtime*1000);
        dateText = simpleDateFormat.format(date);
        text.setColor(Color.RED);
        this.mRedrawHandler.sleep(500);
    }

    class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if(isFinished) {
                update();
                invalidate();
            }
        }

        public void sleep(long delay) {
            this.removeMessages(0);
            this.sendMessageDelayed(this.obtainMessage(0), delay);
        }
    }

    private final RefreshHandler mRedrawHandler = new RefreshHandler();

    Logger logger = Logger.getLogger(getClass().getName());

    String dateText;
    PointF textPoint;
    boolean isFinished = false;
    boolean blink = false;

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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(rect, 0, 360, false, outer);
        //calculateTextPosition();

        if (isFinished) {
            if (blink) {
                canvas.drawText(dateText, textPoint.x, textPoint.y, text);
            }
        } else {
            canvas.drawText(dateText, textPoint.x, textPoint.y, text);
        }

        canvas.drawArc(rect, START_ANGLE_POINT, angle, false, paint);
        //canvas.drawArc(rect2, 0, 360, false, inner);

        /*String secs = mMillisUntilFinished / 1000 + "";
        if (secs.length() != previousLengthOfText) {
            calculateTextPosition();
            previousLengthOfText = secs.length();
        }*/

        //canvas.drawText(dateText, rect.centerX(), rect.centerX(), text);
    }


    /**
     * Se cargan los sonidos a utilizar
     *
     * @param rid
     * @return
     */
    private MediaPlayer loadSound(int rid) {
        MediaPlayer mp = MediaPlayer.create(this.getContext(), rid);
        mp.setOnCompletionListener(this);
        return mp;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.seekTo(0);
    }

    /**
     * Se reproduce el sonido elegido
     *
     * @param mp
     */
    private void playSound(MediaPlayer mp) {

        if (!mp.isPlaying()) {
            mp.setVolume(0.2f, 0.2f);
            mp.start();
        }
    }

    private void stopSound(MediaPlayer mp) {

        if (mp.isPlaying()) {
            mp.stop();
        }
    }
}
