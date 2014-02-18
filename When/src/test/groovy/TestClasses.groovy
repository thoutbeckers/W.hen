import houtbecke.rs.when.BasePushCondition
import houtbecke.rs.when.PullCondition
import houtbecke.rs.when.PushCondition
import houtbecke.rs.when.PushConditionListener
import houtbecke.rs.when.TypedAct
import houtbecke.rs.when.TypedPullCondition
import houtbecke.rs.when.SimpleAct

class TestThing extends Object {
    def saidMerp = false;
    void sayMerp() {
        println toString()+": merp"
        saidMerp = true;
    }

    void reset() {
        saidMerp = false
    }
}

class TriggerStickyTrue implements PushCondition {

    @Override
    void addListener(PushConditionListener listener, Object thing) {
        listener.push this, true, true, null
    }

    @Override
    void removeListener(PushConditionListener listener, Object thing) {}

    @Override
    String toString() {
        'stickyTrue'
    }

}

class PullFalse implements PullCondition {

    @Override
    boolean isMet(Object things) {
        return false
    }
}

class PullTrue implements PullCondition {

    @Override
    boolean isMet(Object things) {
        return true
    }
}

class OneTimeTrue implements PushCondition {

    PushConditionListener push;

    @Override
    void addListener(PushConditionListener listener, Object thing) {
        this.push = listener
    }

    @Override
    void removeListener(PushConditionListener listener, Object thing) {}


    void makeTrue() {
        push.push this, true, false, null
    }

    @Override
    String toString() {
        'oneTimeTrue'
    }
}

class PushStickyFalse extends BasePushCondition {
    void makeTrue() {
        stick true
    }

    void value(value) {
        stick value
    }

    void reset() {
        stick false
    }

    @Override
    String toString() {
        'stickyFalse'
    }

}


class SayMerpAct extends SimpleAct {

    @Override
    public void act(Object thing) {
        if (thing instanceof TestThing)
            thing.sayMerp()
    }


    @Override
    String toString() {
        'sayMerp'
    }
}


class NamedThing {
    def name;

    NamedThing(String name) {
        this.name = name
    }

    @Override
    String toString() {
        name
    }

    boolean equals(o) {
        if (getClass() != o.class) return false
        if (name != o.name) return false
        return true
    }

    int hashCode() {
        return name.hashCode()
    }
}

class NamedThingActivationPushCondition extends BasePushCondition {
    void activate(def name) {
        event new NamedThing(name)
    }

    String toString() {
        'activated'
    }

}

class NamedThingTypedAct extends TypedAct {

    def lastNameRemembered;

    void act(NamedThing thing) {
        lastNameRemembered = thing.name;
    }

    String toString() {
        'rememberName'
    }
}

class DifferentClassesPullCondition extends TypedPullCondition {

    boolean met(String s) {false};

    boolean met(StringBuilder b) {false};

    boolean met(Integer i) {true;}

    boolean met(Long l) {true;}



}
