import houtbecke.rs.when.W
import spock.lang.Specification

class WhenBuilderTest extends groovy.util.GroovyTestCase {

    void testWhen_BaseCondition_TypedAct() {
        def merpie = new NamedThing("merpie")
        def rememberName  = new NamedThingTypedAct()
        def activated = new NamedThingActivationPushCondition()
        // syntax one: explicit with
        W.hen(merpie).is(activated).then().with(merpie).act(rememberName).work()
        assert rememberName.lastNameRemembered != "merpie" : "merpie should not be activated yet"
        activated.activate("merpie")
        assert rememberName.lastNameRemembered.equals("merpie") : "merpie should be activated"

        rememberName.lastNameRemembered = ""
        // syntax 2, no with
        W.hen(merpie).is(activated).then(rememberName).work()
        activated.activate("merpie")
        assert rememberName.lastNameRemembered.equals("merpie") : "merpie should be activated"

        rememberName.lastNameRemembered = ""
        // syntax 2, shorthand
        W.hen merpie, activated, rememberName
        activated.activate("merpie")
        assert rememberName.lastNameRemembered.equals("merpie") : "merpie should be activated"
    }



    void testInitTestObjects() {
        def thing = new TestThing()
        assert !thing.saidMerp : "thing not acted upon yet"
        def sayTest = new SayMerpAct()
        sayTest.act(thing)
        assert thing.saidMerp: "thing should not be acted upon"
    }


    void testWhen_Fixed() {

        def thing = new TestThing()
        def alwaysTrue = new TriggerStickyTrue()
        def sayTest = new SayMerpAct()

        def w = W.hen(alwaysTrue).then(thing, sayTest)
        assert !thing.saidMerp : "false before startingNow"
        w.work()
        assert thing.saidMerp : "true after evaluation was started"
    }

    void testWhen_OneTime() {
        def thing = new TestThing()
        def oneTimeTrue = new OneTimeTrue()
        def sayTest = new SayMerpAct()

        W.hen(oneTimeTrue).then(thing, sayTest).work()
        assert !thing.saidMerp : "false before triggering the condition"
        oneTimeTrue.makeTrue()
        assert thing.saidMerp : "true after a one time trigger"

    }

    void testWhen_OneTime_And_FixedCondition() {
        def thing = new TestThing()
        def oneTimeTrue = new OneTimeTrue()
        def alwaysTrue = new TriggerStickyTrue()
        def sayTest = new SayMerpAct()

        W.hen(oneTimeTrue, alwaysTrue).then(thing, sayTest).work()
        assert !thing.saidMerp : "false before triggering the condition"
        oneTimeTrue.makeTrue()
        assert thing.saidMerp : "true after a one time trigger combined with an always true trigger"
        thing.saidMerp = false;
        oneTimeTrue.makeTrue()
        assert thing.saidMerp : "again true after a one time trigger combined with an always true trigger"
    }

    void testWhen_OneTime_Or_OneTime_AndAlwaysTrue() {
        def thing = new TestThing()
        def oneTimeTrue1 = new OneTimeTrue(), oneTimeTrue2 = new OneTimeTrue()
        def alwaysTrue = new TriggerStickyTrue()
        def sayTest = new SayMerpAct()

        W.hen(oneTimeTrue1).or(oneTimeTrue2).and(alwaysTrue).then(thing, sayTest).work()
        assert !thing.saidMerp : "All conditions are false"
        oneTimeTrue1.makeTrue()
        assert thing.saidMerp : "First of two conditions is met"
        thing.saidMerp = false;
        oneTimeTrue2.makeTrue()
        assert thing.saidMerp : "Second of two conditions is met"
    }

    static def st = [].withEagerDefault { new TriggerStickyTrue()}
    static def sf = [].withEagerDefault { new PushStickyFalse()}

    static def pt = [].withEagerDefault { new PullTrue()}
    static def pf = [].withEagerDefault { new PullFalse()}


    static class WhenConditions extends Specification {

        def "conditions"() {
            expect:
                when.work()
                println when
                result == when.evaluateConditions()

            where:
                when | result

                W.hen(st[1]) | true
                W.hen(sf[1]) | false

                W.hen(pt[1]) | true
                W.hen(pf[1]) | false

                W.hen(st[1]).and().not(sf[1]) | true
                W.hen(st[1]).and().not(st[1]) | false

                W.hen(pt[1]).and().not(pf[1]) | true
                W.hen(pt[1]).and().not(pt[1]) | false

                W.hen(pt[1]).and().not(sf[1]) | true
                W.hen(st[1]).and().not(pf[1]) | true

                W.hen(st[1]).and().when(st[1]) | true
                W.hen(st[1]).and().when(sf[1]) | false
                W.hen(sf[1]).and().when(st[1]) | false
                W.hen(sf[1]).and().when(sf[1]) | false

                W.hen(st[1]).and().not().when(st[1]) | false
                W.hen(st[1]).and().not().when(sf[1]) | true
                W.hen(sf[1]).and().not().when(st[1]) | false
                W.hen(sf[1]).and().not().when(sf[1]) | false

                W.hen(sf[1]).or().when(st[1]) | true
                W.hen(st[1]).or().when(sf[1]) | true
                W.hen(sf[1]).or().not().when(st[1]) | false
                W.hen(sf[1]).or().not().when(sf[1]) | true
        }
    }

    static class WhenToString extends Specification {

        def "ToString"() {
            given:
            expect:
                toString == when.toString()
            where:
                when | toString
                W.hen(st[1]).and(sf[1]) | "when is stickyTrue AND is stickyFalse"
                W.hen(sf[1]).and().not().when(st[1]) | "when is stickyFalse AND_NOT when is stickyTrue"

                W.hen(new NamedThing("merpie"), new NamedThingActivationPushCondition(), new NamedThingTypedAct()) |
                    "when a merpie is activated then rememberName"

        }
    }

    static class WhenInWhen extends Specification {

        def "or and (or)"() {
            given: "An embedded W.hen inside a W.hen"
                def conditions = [].withEagerDefault {new PushStickyFalse()}
                def when = W.hen(conditions[1]).or(conditions[2]).and(W.hen(conditions[3]).or(conditions[4]))

            expect:
                println("$value1, $value2, $value3, $value4")

                conditions[1].value value1
                conditions[2].value value2
                conditions[3].value value3
                conditions[4].value value4
                result == when.evaluateConditions()

            where:
            //        or       and(     or       )
                value1 | value2 | value3 | value4 | result
//                false  | false  | false  | false  | false
                true   | true   | true   | true   | true

//                true   | false  | false  | false  | false
//                false  | true   | false  | false  | false
//                false  | false  | true   | false  | false
//                false  | false  | false  | true   | false
//
//                true   | true   | false  | false  | false
//                true   | false  | true   | false  | true
//                true   | false  | false  | true   | true
//
//                false  | false  | true   | false  | false
//                false  | false  | true   | true   | false

        }
    }
}
