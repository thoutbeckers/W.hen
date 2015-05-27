package houtbecke.rs.when.robo.act;

import android.os.AsyncTask;

import javax.inject.Singleton;

import houtbecke.rs.when.TypedAct;

@Singleton
public class CancelTask extends TypedAct {

    public void act(AsyncTask task) {
        task.cancel(true);
    }
}
