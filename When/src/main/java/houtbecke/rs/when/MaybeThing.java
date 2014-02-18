package houtbecke.rs.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public abstract class MaybeThing<T> implements Things<T> {

    protected Random random = new Random();
    int percentageChance;
    public MaybeThing(int percentageChance) {
        this.percentageChance = percentageChance;

    }

    public abstract T getRandomThing();

    @Override
    public Collection<T> getThings() {
        if (random.nextInt(100) < percentageChance) {
            T t = getRandomThing();
            if (t!= null) {
                ArrayList<T> set = new ArrayList<T>(1);
                set.add(t);
                return set;
            }
        }
        return null;
    }
}
