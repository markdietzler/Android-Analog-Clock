package mark.dietzler.mdietzlerlab5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer{

    private String mTime;
    TimeTransfer timeTransfer = new TimeTransfer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeTransfer.addObserver(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here
        int id = item.getItemId();
        if(id == R.id.action_about) {
            Toast.makeText(this, "Lab 5, Winter 2019, Mark Dietzler", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(Observable arg0, Object newTime) {
        mTime = (String) newTime;
        TextView digitalClock = findViewById(R.id.digitalClock);
        digitalClock.setText(mTime);
    }
}
