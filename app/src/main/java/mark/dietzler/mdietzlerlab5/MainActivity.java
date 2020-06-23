package mark.dietzler.mdietzlerlab5;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer{

    AnalogClock clock;
    TextView digitalTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clock = (AnalogClock) findViewById(R.id.analog_clock);
        digitalTime = (TextView)findViewById(R.id.digital_clock);
        clock.hourMinSec.addObserver(this);
    }

    public void onAbout(MenuItem item){
        Toast.makeText(this, "Lab 5, Spring 2020, Mark Dietzler", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void update(Observable observableInput, Object observableTimeTransfer) {
        String digitalClock_Time = new String();
        if(observableInput != null) {
            int hour = clock.hourMinSec.getHour();
            int minute = clock.hourMinSec.getMin();
            int second = clock.hourMinSec.getSec();
            digitalClock_Time = String.format("%02d:%02d:%02d",hour,minute,second);
            digitalTime.setText(digitalClock_Time);
        } else {
            digitalTime.setText("XX:XX:XX");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
