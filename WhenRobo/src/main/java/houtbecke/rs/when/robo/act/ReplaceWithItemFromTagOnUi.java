package houtbecke.rs.when.robo.act;

import android.view.View;

import houtbecke.rs.when.DefaultConditionThing;

public class ReplaceWithItemFromTagOnUi extends ReplaceOnUI<View> {

    int[] tags = null;

    public ReplaceWithItemFromTagOnUi(DefaultConditionThing thing) {
        super(thing, View.class);
    }

    public ReplaceWithItemFromTagOnUi(DefaultConditionThing thing, int... tags) {
        super(thing, View.class);
        this.tags = tags;
    }

    protected void setOrReplaceThing(Object thing) {
        View v = (View) thing;
        if (tags == null)
            super.setOrReplaceThing(v.getTag());
        else
            for (int tag: tags) {
                Object o = v.getTag(tag);
                if (o != null) {
                    super.setOrReplaceThing(v.getTag(tag));
                    break;
                }

            }
    }
}
