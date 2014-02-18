import houtbecke.rs.when.TypedAct

class TypedActTest extends groovy.util.GroovyTestCase {

    class SimpleTypedAct extends TypedAct {

        def acted = false;
        void act(String s) {
            assert s == "s";
            acted = true;
        }
    }

    void testSimple() {
        def typedAct = (TypedAct)new SimpleTypedAct();
        typedAct.act("s")
        assert typedAct.acted
    }

    class DoubleTypedAct extends TypedAct {
        def acted = false
        void act(Integer i, String s) {
            assert i == 20
            assert s == "s"
            acted = true
        }

    }

    void testDouble() {
        def typedAct = new DoubleTypedAct()

        def params = new Object[2]
        params[0] = "s"
        params[1] = 20

        typedAct.act(params)
        assert typedAct.acted

        typedAct = new DoubleTypedAct()
        params = new Object[4]
        params[0] = "s"
        params[1] = null
        params[2] = 20L
        params[3] = 20

        typedAct.act(params)
        assert typedAct.acted

        typedAct = new DoubleTypedAct()
        typedAct.act(null as Object, 20 as Object);
        assert !typedAct.acted;

    }

    class VarargTypedAct extends TypedAct {
        def acted = false
        void act(Integer i, String... s) {
            acted = true
            assert i == 20
            assert s[0] == "s"
            assert s[1] == "t"

        }
    }

    class VarargTypedActNoString extends TypedAct {
        def acted = false
        void act(Integer i, String... s) {
            acted = true
            assert i == 20
            assert s.length == 0;

        }
    }


    void testVararg() {
        def typedAct = new VarargTypedAct()

        def params = new Object[3]
        params[0] = "s"
        params[1] = 20
        params[2] = "t"

        typedAct.act(params)

        assert typedAct.acted

        // once more for hitting the cache
        typedAct.acted = false
        typedAct.act(params)
        assert typedAct.acted

        // different order
        params = new Object[3]
        params[0] = 20
        params[1] = "s"
        params[2] = "t"

        typedAct.acted = false
        typedAct.act(params)
        assert typedAct.acted


        typedAct = new VarargTypedAct()

        params = new Object[5]
        params[0] = "s"
        params[1] = 20
        params[2] = null
        params[3] = 20L
        params[4] = "t"

        typedAct.act(params)

        assert typedAct.acted
        // once more for hitting the cache
        typedAct.acted = false
        typedAct.act(params)
        assert typedAct.acted

    }
    void testVarArgNoString() {
        TypedAct typedActNoString = new VarargTypedActNoString()

        typedActNoString.act(20 as Object);
        assert typedActNoString.acted

    }

    enum HIT {NONE, STRING, INTEGER, BOTH}
    class MultiAct extends TypedAct {

        HIT wasHit = HIT.NONE

        boolean hit(HIT test) {
            return test == wasHit
        }

        void act(String s) {
            wasHit = HIT.STRING
        }

        void act(Integer i) {
            wasHit = HIT.INTEGER
        }

        void act(String s, Integer i) {
            wasHit = HIT.BOTH
        }
    }

    void testMultiAct() {

        def multiAct = new MultiAct()

        multiAct.act(20 as Integer)
        assert multiAct.hit(HIT.INTEGER)

        multiAct.act("bla" as String)
        assert multiAct.hit(HIT.STRING)

        multiAct.act(20 as Integer, "bla" as String);
        assert multiAct.hit(HIT.BOTH)

    }

    interface A { }; class B implements A {  }

    class InterfaceTypedAct extends TypedAct {
        def acted = false
        void act(A a) {
            acted = true
        }
    }

    void testInterfaces() {
        TypedAct act = (TypedAct) new InterfaceTypedAct()
        act.act new B()
        assert act.acted
    }


    void testEmpty() {

        TypedAct act = new TypedAct();

        act.act(null)
        act.act("random")
        assert true : "no exceptions"

    }
}