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
        act(SHORT, text);
    }

    public void act(Integer text) {
        act(SHORT, text);
    }


    public void act(Length length,String text) {
        Toast.makeText(context, text, length.length).show();
    }

    public void act(Length length, Integer text) {
        Toast.makeText(context, text, length.length).show();
    }

    public static Length SHORT = Length.SHORT;
    public static Length LONG = Length.LONG;

    public static enum Length {
        SHORT(Toast.LENGTH_SHORT),
        LONG(Toast.LENGTH_LONG);

        int length;
        Length(int length) {
            this.length = length;

        }
    }

}
