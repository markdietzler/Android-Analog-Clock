package mark.dietzler.mdietzlerlab5;

import android.animation.TimeAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Observable;

public class AnalogClock extends View implements TimeAnimator.TimeListener {

    TimeAnimator mTimer = new TimeAnimator();
    final static float overallWidth = 110.0f, overallHeight = 110.0f, handLength = 40.0f;
    final static float ASPECT_RATIO = 1f;
    final static float[] minuteHand = new float[]{-02.5f, 0, 0, 40.25f, 02.5f, 0, 0, -02.5f, -02.5f, 0};
    final static float[] hourHand = new float[]{-05f, 0, 0, 30.5f, 05f, 0, 0, -05f, -05f, 0};

    int mViewWidth, mViewHeight;
    Boolean hourFormat = Boolean.FALSE;
    private static final float mSECOND_DEGREES = 360/-60;

    public HourMinSec hourMinSec = new HourMinSec();

    public AnalogClock(Context context) {
        super(context);
    }

    public AnalogClock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Init();
    }

    public AnalogClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init();
    }

    private void Init(){
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        startTimer();
    }

    @Override
    public void onDraw(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();
        float scaleX = (width/overallWidth);
        float scaleY = (height/overallHeight);
        canvas.save();

        canvas.scale(scaleX , -scaleY);
        canvas.translate(overallWidth/2.0f, -overallHeight/2.0f);
        Paint paint = new Paint();

        //set up canvas to draw on
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1f);
        paint.setColor(Color.RED);
        canvas.save();

        //get time
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        int hour = gregorianCalendar.get(Calendar.HOUR);
        int minute = gregorianCalendar.get(Calendar.MINUTE);
        int second = gregorianCalendar.get(Calendar.SECOND);

        float hourTick = 30/60 * minute;

        //draw hour hand
        canvas.save();
        paint.setColor(Color.RED);
        Path hourPath = new Path();
        hourPath.moveTo(hourHand[0], hourHand[1]);
        hourPath.lineTo(hourHand[2], hourHand[3]);
        hourPath.lineTo(hourHand[4], hourHand[5]);
        hourPath.lineTo(hourHand[6], hourHand[7]);
        hourPath.lineTo(hourHand[8], hourHand[9]);
        hourPath.close();
        canvas.rotate((mSECOND_DEGREES * 5) * hour + hourTick);
        canvas.drawPath(hourPath, paint);
        canvas.restore();

        //draw minute hand
        canvas.save();
        paint.setColor(Color.BLACK);
        Path minutePath = new Path();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        minutePath.moveTo(minuteHand[0], minuteHand[1]);
        minutePath.lineTo(minuteHand[2],minuteHand[3]);
        minutePath.lineTo(minuteHand[4], minuteHand[5]);
        minutePath.lineTo(minuteHand[6], minuteHand[7]);
        minutePath.lineTo(minuteHand[8], minuteHand[9]);
        minutePath.close();
        canvas.rotate(mSECOND_DEGREES * minute);
        canvas.drawPath(minutePath, paint);
        canvas.restore();

        //draw seconds hand
        paint.setColor((Color.RED));
        canvas.rotate(second * mSECOND_DEGREES);
        canvas.drawLine(0, 0,0,handLength,paint);
        canvas.restore();

        //draw big circle ticks
        for(int circleTics = 0 ; circleTics <= 12 ; circleTics++){
            paint.setColor(Color.BLACK);
            canvas.save();
            canvas.rotate(circleTics*30);
            canvas.drawLine(0,40,0,50, paint);
            canvas.restore();
        }

        //draw little circle ticks
        paint.setStrokeWidth(0.5f);
        paint.setColor((Color.BLACK));
        for(int minuteTics = 0 ; minuteTics <= 60 ; minuteTics++){
            canvas.save();
            canvas.rotate(minuteTics*6);
            canvas.drawLine(0,45, 0, 50, paint);
            canvas.restore();
        }

        canvas.restore();

        if(hourFormat) {
            hourMinSec.setHourMinuteSeconds(hour, minute, second);
        }
        else{
            hourMinSec.setHourMinuteSeconds(gregorianCalendar.get(Calendar.HOUR),minute,second);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        float wSize = View.MeasureSpec.getSize(widthMeasureSpec);
        float hSize = View.MeasureSpec.getSize(heightMeasureSpec);
        float width, height;
        height = wSize / ASPECT_RATIO;
        width = hSize * ASPECT_RATIO;
        if(height>hSize){
            this.setMeasuredDimension((int)width, (int)hSize);
        }
        else {
            this.setMeasuredDimension((int) wSize, (int) height);
        }
    }

    @Override
    public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
        invalidate();
    }

    @Override
    protected void onSizeChanged(int incomingWidth, int incomingHeight, int oldWidth, int oldHeight){
        super.onSizeChanged(incomingWidth, incomingHeight, oldWidth, oldHeight);
        mViewHeight = incomingHeight;
        mViewWidth = incomingWidth;
    }

    public void startTimer() {
        mTimer.setTimeListener(new TimeAnimator.TimeListener() {
            @Override
            public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
                invalidate();
            }
        });
        mTimer.start();
    }

    class HourMinSec extends Observable {
        int mHour, mMin, mSec;

        public int getHour() {
            return mHour;
        }

        public int getMin() {
            return mMin;
        }

        public int getSec() {
            return mSec;
        }

        public void setHourMinuteSeconds(int newHour, int newMinute, int newSecond){
            mHour = newHour;
            mMin = newMinute;
            mSec = newSecond;
            setChanged();
            notifyObservers("Clock");
        }
    }
}
