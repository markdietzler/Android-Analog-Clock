package mark.dietzler.mdietzlerlab5;

import java.util.Observable;

public class TimeTransfer extends Observable {

    public String getTime() {
        return "";
    }

    public void setTime(String newTime) {
        setChanged();
        notifyObservers(newTime);
    }

}