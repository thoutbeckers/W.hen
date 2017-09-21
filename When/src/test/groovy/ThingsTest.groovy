import houtbecke.rs.when.BasePushCondition
import houtbecke.rs.when.Condition
import houtbecke.rs.when.DefaultConditionThing
import houtbecke.rs.when.PushCondition
import houtbecke.rs.when.PushConditionListener
import houtbecke.rs.when.DefaultConditionThings
import houtbecke.rs.when.Things
import houtbecke.rs.when.ThingsListener
import houtbecke.rs.when.TypedAct
import houtbecke.rs.when.W
import houtbecke.rs.when.act.AddTo
import houtbecke.rs.when.act.RemoveFrom
import houtbecke.rs.when.condition.Added
import houtbecke.rs.when.condition.Removed

class ThingsTest extends GroovyTestCase {

    @Override
    void setUp() {
        added = null;
        removed = null;
    }
    def added, removed;

    def thingsListener = new PushConditionListener() {

        @Override
        void push(Condition c, boolean conditionMet, boolean isSticky, Object... results) {

        }
    }

    def thingsCondition = new PushCondition() {

        @Override
        void addListener(PushConditionListener listener, Object thing) {
            added = thing;
        }

        @Override
        void removeListener(PushConditionListener listener, Object thing) {
            removed = thing
        }
    }

    void testThings() {
        def object = new Object()
        def object2 = new Object()

        def things = new DefaultConditionThings()
        things.addThing(object)
        assert things.things.contains(object)

        things.addPushCondition(thingsCondition, thingsListener)

        assert added == object
        assert removed == null
        added = null
        things.removeThing(object)
        assert added == null
        assert removed == object

        things.addThing(object2)
        assert added == object2
    }

    void testAddToRemoveFromSimple() {
        def things = new DefaultConditionThings()
        def add = new AddTo(things)
        def remove = new RemoveFrom(things);
        def object = new Object()

        add.act(object as Object)
        assert things.things.contains(object)
        remove.act(object)
        assert !things.things.contains(object)

    }

    interface A {}; class B implements A {}

    void testAddToRemoveFromTyped() {
        def things = new DefaultConditionThings()
        def object = new Object()
        def b = new B();
        def add = new AddTo(things, Integer.class, A.class)
        def remove = new RemoveFrom(things, Integer.class, A.class)
        def inti = 20;

        add.act(inti as int)
        assert things.things.contains(inti)
        add.act(object as Object, b as B)
        assert !things.things.contains(object)
        assert things.things.contains(b)

        remove.act(object as Object, b as B)
        assert things.things.contains(inti)
        assert !things.things.contains(object)
        assert !things.things.contains(b)


    }

    void testNotOneOf() {
        def object = new Object()

        def things = new DefaultConditionThings()
        def notOneOfCondition = things.notOneOf(Integer.class)

        assert notOneOfCondition.toString() != null

        assert !notOneOfCondition.isMet(object), "type that is not Integer should not lead to the condition being triggered"

        def integer = new Integer(1)

        assert notOneOfCondition.isMet(integer)
        things.addThing(integer);
        assert !notOneOfCondition.isMet(integer)
    }

    void testOneOf() {
        def object = new Object()

        def things = new DefaultConditionThings()
        def oneOfCondition = things.oneOf(String.class)

        assert oneOfCondition.toString() != null

        assert !oneOfCondition.isMet(object), "type that is not String should not lead to the condition being triggered"

        String merp = "merp"

        assert !oneOfCondition.isMet(merp)
        things.addThing("merp");
        things.addThing("derp");
        assert oneOfCondition.isMet("merp")
        assert !oneOfCondition.isMet("herp")
    }




    def emptyListener = new PushConditionListener() {

        @Override
        void push(Condition c, boolean conditionMet, boolean isSticky, Object... results) {
            empty = true
        }
    }
    def notEmptyListener = new PushConditionListener() {

        @Override
        void push(Condition c, boolean conditionMet, boolean isSticky, Object... results) {
            notEmpty = true
        }
    }


    def empty,notEmpty;

    void testIsEmpty() {

        def things = new DefaultConditionThings()
        BasePushCondition isEmpty = things.isEmpty();

        BasePushCondition isNotEmpty = things.isNotEmpty();

        isEmpty.addListener(emptyListener,this);
        isNotEmpty.addListener(notEmptyListener,this);

        empty = false;
        notEmpty = false;

        def object = new Object()
        things.addThing(object);
        assert notEmpty;
        assert !empty;
        empty = false;
        notEmpty = false;
        things.removeThing(object);
        assert empty;
        assert !notEmpty;


    }

    class MyThingsListener implements ThingsListener<String> {
        def added = 0
        def removed = 0
        def thingRemoved, thingAdded

        @Override
        void thingAdded(Things<String> things, String s) {
            added++
            thingAdded = s;
        }

        @Override
        void thingRemoved(Things<String> things, String s) {
            removed++
            thingRemoved = s
        }
    }

    void testObserve() {
        def things = new DefaultConditionThings<String>()

        def listener1 = new MyThingsListener()
        def listener2 = new MyThingsListener()
        things.observe(listener1)
        things.observe(listener2)

        things.addThing("hallo")
        assert listener1.removed == 0
        assert listener1.added == 1
        assert listener1.thingAdded == "hallo"

        things.addThing("hallo")
        assert listener1.added == 2 && listener1.removed == 1, "ensure duplicate items are added, since they are removed and added"
        assert listener1.thingRemoved == "hallo"

        things.addThing("how are you")
        assert listener1.added == 3
        assert listener1.thingAdded == "how are you"

        listener1.thingRemoved = null
        things.removeThing("hallo")
        assert listener1.removed == 2
        assert listener1.thingRemoved == "hallo"

        things.removeThing("hallo")
        assert listener1.removed == 2, "ensure duplicate removal does not happen"

        things.removeThing("merp")
        assert listener1.removed == 2, "ensure removal of non existing things has no effect"

        things.removeThing("how are you")
        assert listener1.removed == 3
        assert listener1.thingRemoved == "how are you"

        assert listener1.removed == listener2.removed && listener1.added == listener2.added, "ensure both listeners were called"
    }


    void testThing() {
        def thing = new DefaultConditionThing<String>("someThing");
        thing.setOrReplaceThing("merp");
        assert thing.getThings().toArray().size() == 1
        assert thing.getThings().toArray()[0] == "merp"

        thing.setOrReplaceThing("herp");
        assert thing.getThings().toArray().size() == 1
        assert thing.getThings().toArray()[0] == "herp"

        thing.clear()
        assert thing.getThings().toArray().size() == 0

        shouldFail { thing.addThing("merp") }


    }

    void testAddedRemoved() {
        def thing= new DefaultConditionThing<String>("someThing");

        assert thing.toString() != null

        def added = new Added(thing)
        def removed = new Removed(thing)

        assert added.toString() != null && removed.toString() != null

        def addedString = null;
        def removedString = null;

        W.hen(added).then(new TypedAct() {
            void act(String string) {
                addedString = string;
            }
        }).work()

        W.hen(removed).then(new TypedAct() {
            void act(String string) {
                removedString = string;
            }
        }).work()


        thing.setOrReplaceThing("merp")
        assert addedString == "merp" && removedString == null;

        thing.setOrReplaceThing("herp")
        assert addedString == "herp" && removedString == "merp"
    }
}