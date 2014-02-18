package houtbecke.rs.when.robo.act;

import android.content.Context;
import android.widget.Toast;

import houtbecke.rs.when.TypedAct;

public class MakeToast extends TypedAct {
    Context context;

    @javax.inject.Inject
    public MakeToast(Context context) {
        this.context = context;
    }

    public void act(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public void act(int text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
