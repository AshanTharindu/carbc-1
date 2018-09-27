package core.smartContract;

public class TimeKeeper extends Thread{
    @Override
    public void run() {
        try {
            Thread.sleep(600000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
