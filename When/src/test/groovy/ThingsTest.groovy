import houtbecke.rs.when.BasePushCondition
import houtbecke.rs.when.Condition
import houtbecke.rs.when.PushCondition
import houtbecke.rs.when.PushConditionListener
import houtbecke.rs.when.DefaultConditionThings
import houtbecke.rs.when.act.AddTo
import houtbecke.rs.when.act.RemoveFrom

class ThingsTest extends groovy.util.GroovyTestCase {

    @Override
    void setupBefore() {
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
        assert !notOneOfCondition.isMet(object)

        def integer = new Integer(1)

        assert notOneOfCondition.isMet(integer)
        things.addThing(integer);
        assert !notOneOfCondition.isMet(integer)
    }

    void testIsEmpty() {

        def things = new DefaultConditionThings()
        BasePushCondition isEmpty = things.IsEmpty();

        BasePushCondition isNotEmpty = things.IsNotEmpty();
        def object = new Object()
        things.addThing(object);

        things.removeThing(object);
    }

}

