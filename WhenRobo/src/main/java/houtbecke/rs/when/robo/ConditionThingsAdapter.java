package houtbecke.rs.when.robo;

import android.widget.BaseAdapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import houtbecke.rs.when.Condition;
import houtbecke.rs.when.ConditionThings;
import houtbecke.rs.when.PushCondition;
import houtbecke.rs.when.PushConditionListener;
import houtbecke.rs.when.Things;
import houtbecke.rs.when.ThingsListener;
import houtbecke.rs.when.W;

public abstract class ConditionThingsAdapter<T> extends BaseAdapter implements ThingsListener<T> {

    final Map<T, Things<T>> items = Collections.synchronizedMap(new LinkedHashMap<T, Things<T>>());
    volatile Object[] itemArray = null;

    @SuppressWarnings("unchecked")
    public ConditionThingsAdapter(ConditionThings<T>... thingies) {
        for (ConditionThings<T> anotherThing : thingies)
            addThings(anotherThing);
    }

    void addThings(ConditionThings<T> things) {
        for (T thing : things.getThings())
            items.put(thing, things);
        things.observe(this);

    }

    @Override
    public void thingAdded(Things<T> things, T thing) {
        itemArray = null;
        items.put(thing, things);
        notifyDataSetChanged();
    }

    @Override
    public void thingRemoved(Things<T> things, T thing) {
        itemArray = null;
        items.remove(thing);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.keySet().size();
    }


    public T getItemForPosition(int position) {
        //noinspection unchecked
        return (T) getItem(position);
    }

    public Things<T> getThingsForPosition(int position) {
        return items.get(getItemForPosition(position));
    }

    @Override
    public Object getItem(int position) {

        Object[] lastItems = itemArray;
        if (lastItems == null) {
            lastItems = items.keySet().toArray();
            itemArray = lastItems;
        }
        return lastItems[position];
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }
}
