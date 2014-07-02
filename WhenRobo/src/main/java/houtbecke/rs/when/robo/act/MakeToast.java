package houtbecke.rs.when.robo.act;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import houtbecke.rs.when.TypedAct;
import houtbecke.rs.when.robo.OnUiThread;

public class MakeToast extends AndroidTypedAct {
    Context context;
    Toast toast = null;

    @javax.inject.Inject
    public MakeToast(Context context) {
        this(context, null);
    }

    Class customClass;
    public MakeToast(Context context, Class customClass) {
        this.customClass = customClass;
        this.context = context;
    }

    @OnUiThread
    public void act(String text) {
        act(SHORT, text);
    }

    @OnUiThread
    public void act(Integer text) {
        act(SHORT, text);
    }


    @OnUiThread
    public void act(Length length,String text) {
        if (length.dismissPrevious && toast != null)
            toast.cancel();

        toast = Toast.makeText(context, text, length.length);
        toast.show();
        Log.i("TOAST", "with text: " + text);
    }

    @OnUiThread
    public void act(Length length, Integer text) {
        act(text+"", length.length);
    }

    @Override
    public void defaultActOnUi(Object... things) {
        if (customClass == null)
            return;

        Length l = SHORT;
        String text = "";
        for (Object thing: things)
            if (thing instanceof Length)
                l = (Length) thing;
            else if (customClass.isInstance(thing))
                text+=thing.toString()+'\n';
        if (text.length() > 0)
            act(l, text.substring(0, text.length() - 1));
    }

    public static Length SHORT = Length.SHORT;
    public static Length LONG = Length.LONG;

    public static enum Length {
        SHORT(Toast.LENGTH_SHORT, false),
        LONG(Toast.LENGTH_LONG, false),
        SHORT_DISMISS_PREVIOUS(Toast.LENGTH_SHORT, true),
        LONG_DISMISS_PREVIOUS(Toast.LENGTH_LONG, true);


        int length;
        boolean dismissPrevious;
        Length(int length, boolean dismissPrevious) {
            this.length = length;
            this.dismissPrevious = dismissPrevious;

        }
    }

}
