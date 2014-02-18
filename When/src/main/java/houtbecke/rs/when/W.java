package houtbecke.rs.when;

import java.util.LinkedList;
import java.util.List;

public class W {

    public static void ork() {
        List<WhenBuilder> buildersList = builders.get();
        if (buildersList == null)
            return;

        for (WhenBuilder builder: buildersList)
            if (!builder.isWorking())
                builder.work();
    }

    private static ThreadLocal<List<WhenBuilder>> builders = new ThreadLocal<List<WhenBuilder>>();

    private static WhenBuilder checkForStart(WhenBuilder builder) {
        if (builder.isWorking())
            return builder;
        List buildersList = builders.get();
        if (buildersList == null) {
            buildersList = new LinkedList<WhenBuilder>();
            builders.set(buildersList);
        }
        buildersList.add(builder);
        return builder;

    }

    public static WhenBuilder hen() {
        return checkForStart(new WhenBuilder());
    }

    public static WhenBuilder hen(Object object) {
        return checkForStart(new WhenBuilder(object));
    }
    public static WhenBuilder hen(Object thing, Object... moreThings) {
        return checkForStart(new WhenBuilder(thing, moreThings));
    }

    public static WhenBuilder hen(Object thing, Condition condition) {
        return checkForStart(new WhenBuilder(thing, condition));
    }
    public static WhenBuilder hen(Object thing, Condition condition, Condition moreConditions) {
        return checkForStart(new WhenBuilder(thing, condition, moreConditions));
    }

    public static WhenBuilder hen(Object thing, Condition condition, Act act) {
        return checkForStart(new WhenBuilder(thing, condition, act));
    }

    public static WhenBuilder hen(Object thing, Condition condition, Object withThing, Act act) {
        return checkForStart(new WhenBuilder(thing, condition, withThing, act));
    }

    public static WhenBuilder hen(Condition condition) {
        return checkForStart(new WhenBuilder(condition));
    }
    public static WhenBuilder hen(Condition condition, Condition condition2) {
        return checkForStart(new WhenBuilder(condition, condition2));
    }
    public static WhenBuilder hen(Condition condition, Condition... moreConditions) {
        return checkForStart(new WhenBuilder(condition, moreConditions));
    }

    public static WhenBuilder hen(Condition condition, Act act) {
        return checkForStart(new WhenBuilder(condition, act));
    }
    public static WhenBuilder hen(Condition condition, Act act, Act... moreActs) {
        return checkForStart(new WhenBuilder(condition, act, moreActs));
    }


}
