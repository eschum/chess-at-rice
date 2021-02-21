package edu.rice.comp610.model;


/**
 * Dispatch Adapter Test
 * Unit Tests for Template Ball World.
 */
//public class DispatchAdapterTest extends TestCase {
//   //Instantiate mock and spy ball objects.
//    private DispatchAdapter da;
//    private PropertyChangeSupport pcs;
//    private Point loc = new Point(100,100);
//    private Point vel = new Point(10,-10);
//    IUpdateStrategy strat = DiagonalStrategy.getInstance();
//    @Spy
//    private Ball switchBallSpy = new Ball(loc, 6, vel, "green", true, strat);
//    @Spy
//    private Ball nonSwitchBallSpy = new Ball(loc, 6, vel, "green", false, strat);
//    @Mock
//    private Ball nonSwitchBallMock;
//    @Spy
//    private Fish switchFishSpy = new Fish(loc, 6, vel, "green", true, strat);
//    @Spy
//    private Fish nonSwitchFishSpy = new Fish(loc, 6, vel, "green", false, strat);
//    @Mock
//    private Fish nonSwitchFishMock;
//
//    //Additional spy individual strategies
//    @Spy
//    private FrictionBounceStrategy frictionStrategy;
//    @Spy
//    private RandomReflectionStrategy reflectionStrategy;
//    @Spy
//    private ZigZagStrategy zagStrategy;
//    @Spy
//    private RotateStrategy rotStrategy;
//    @Spy
//    private SizeChangeStrategy sizeStrategy;
//    @Spy
//    private HopScotchStrategy hopScotchStrategy;
//    @Spy
//    private DeadStrategy deadStrategy;
//    @Spy
//    private ColorChangeStrategy colorChangeStrategy;
//    @Spy
//    private RoboticStrategy roboticStrategy;
//
//    //Spy Collision Strategies (that can be instantiated without DA).
//    @Spy
//    private BounceCollision bounceCollision = BounceCollision.getInstance();
//    @Spy
//    private ChaseCollision chaseCollision = ChaseCollision.getInstance();
//    @Spy
//    private ColorChangeCollision colorChangeCollision = ColorChangeCollision.getInstance();
//    @Spy
//    private DodgeCollision dodgeCollision = DodgeCollision.getInstance();
//    @Spy
//    private ExcitationCollision exciteCollision = ExcitationCollision.getInstance();
//    //Instantiate ExplodeCollision, IncinerateCollision, and AbsorbCollision in testCollisionStrategy()
//
//    //Instantiate additional ball and fish objects to test collisions
//    @Spy
//    private Ball ballSpy2 = new Ball(loc, 6, vel, "green", true, strat);
//    @Spy
//    private Fish fishSpy2 = new Fish(loc, 6, vel, "green", true, strat);
//
//    /**
//     * Method: testDA - Test the Methods of the Dispatch Adapter
//     */
//    public void testDA() {
//        //Initialize Mocks and Spies from Annotations
//        MockitoAnnotations.initMocks(this);
//
//        //Instantiate / test static methods
//        da = new DispatchAdapter();
//        pcs = DispatchAdapter.getPCS();
//        DispatchAdapter.setCanvasDims(new Point(800, 800));
//
//        //Test loadObj
//        PaintObject SBall = da.loadObj("diagonal", "ball", "true");
//        PaintObject SFish = da.loadObj("diagonal", "fish", "true");
//        PaintObject NBall = da.loadObj("diagonal", "ball", "false");
//        PaintObject NFish = da.loadObj("diagonal", "fish", "false");
//        PaintObject Ball3 = da.loadObj("diagonal", "ball", "false");
//        PaintObject Ball4 = da.loadObj("size-change", "ball", "true");
//        PaintObject Ball5 = da.loadObj("random-reflection", "ball", "true");
//        PaintObject Ball6 = da.loadObj("random-reflection", "ball", "true");
//        PaintObject Ball7 = da.loadObj("friction-bounce", "ball", "true");
//        PaintObject Ball8 = da.loadObj("zig-zag", "ball", "true");
//        PaintObject Ball9 =da.loadObj("rotate", "ball", "true");
//        PaintObject Ball10 =da.loadObj("hopscotch", "ball", "true");
//        PaintObject Ball11 =da.loadObj("dead / float", "ball", "true");
//        PaintObject Ball12 =da.loadObj("color-change", "ball", "true");
//        PaintObject Ball13 =da.loadObj("do-the-robot", "ball", "true");
//
//        //Test switchStrategy
//        ArrayList<String> sel = new ArrayList<String>();
//        sel.add("Ball-0");
//        sel.add("Ball-1");
//        sel.add("Fish-0");
//        sel.add("Fish-1");
//        da.switchStrategy("size-change", sel, "chase", "chase", "chase");
//        assertEquals("switchable ball switches", "SizeChange", SBall.getStrategy().getName());
//        assertEquals("switchable fish switches", "SizeChange", SFish.getStrategy().getName());
//        assertEquals("non-switchable ball no switch", "Diagonal", NBall.getStrategy().getName());
//        assertEquals("non-switchable fish no switch", "Diagonal", NFish.getStrategy().getName());
//
//        //Test AddListener, UpdateBallWorld. Use our mocks and spys.
//        da.addListener(switchBallSpy);
//        da.addListener(switchFishSpy);
//        nonSwitchBallSpy.setName("Ball-To-Delete");
//        da.addListener(nonSwitchBallSpy);
//        da.updateBallWorld();
//        //Use sendCollisionCommand as a proxy for invocation of the propertyChange method.
//        verify(switchBallSpy, atMost(1)).sendCollisionCommand();
//        verify(switchFishSpy, atMost(1)).sendCollisionCommand();
//        verify(nonSwitchBallSpy, atMost(1)).sendCollisionCommand();
//        verify(nonSwitchFishSpy, never()).sendCollisionCommand();
//
//        //Test Object Removal Methods.
//
//        //da.addToNameIndex(nonSwitchBallSpy);
//        ArrayList<String> rem = new ArrayList<String>();
//        rem.add("Ball-To-Delete");
//
//        da.removeSubsetListeners(rem);
//        da.updateBallWorld();
//        //Make sure that the object that was updated before (that was then removed) is not updated again.
//        verify(nonSwitchBallSpy, atMost(1)).sendCollisionCommand();
//        //Check that the object updated before and not removed is updated again.
//        verify(switchBallSpy, atLeast(2)).sendCollisionCommand();
//
//        //Check that the listener that was still listening after indie delete is now cleared after deleting all.
//        //Ie, it is not called again. It should only have been called twice in total.
//        da.removeListeners();
//        verify(switchBallSpy, atMost(2)).sendCollisionCommand();
//    }
//
//
//    public void testIndividualStrategy() {
//        //Initialize Mocks and Spies from Annotations
//        MockitoAnnotations.initMocks(this);
//
//        //Instantiate / test static methods
//        da = new DispatchAdapter();
//        pcs = DispatchAdapter.getPCS();
//        DispatchAdapter.setCanvasDims(new Point(800, 800));
//
//        //Test Wall collision, Diagonal Strategy
//        switchBallSpy.setStrategy(DiagonalStrategy.getInstance());
//        //ball properly changes direction when hitting a wall
//        da.addListener(switchBallSpy);
//        Point collisionPoint = new Point (800, 400);   //Define a position that the fish crashes into the right wall.
//        Point preCollisionVel = new Point(20, 10);    //Define a velocity that the fish is swimming up and right.
//        switchBallSpy.setLocation(collisionPoint);
//        switchBallSpy.setVelocity(preCollisionVel);
//        da.updateBallWorld();   //Update the world - should call all event listeners and the fish should update state.
//        Mockito.verify(switchBallSpy).detectCollision();   //Detect that we checked for collision
//        Mockito.verify(switchBallSpy).setVelocity(new Point(-20, 10));   //Detect that we tried to change the velocity correctly
//        //Confirm that the velocity is correct.
//        assertEquals("Correct updated velocity", new Point(-20, 10), switchBallSpy.getVelocity());
//
//        //Test Fish wall collision and angle update. (Diagonal Strategy)
//        SwitchStrategyCommand com = new SwitchStrategyCommand("diagonal");
//        com.execute(switchFishSpy);
//        da.addListener(switchFishSpy);
//        switchFishSpy.setLocation(collisionPoint);
//        switchFishSpy.setVelocity(preCollisionVel);
//        da.updateBallWorld();
//        Mockito.verify(switchFishSpy).updateImageAngle();
//        double theta = Math.atan(((double) 10) / -20) + Math.PI;
//        assertEquals("Fish angle updated correctly", theta, switchFishSpy.getRotate());
//
//        //Test Friction Bounce Strategy
//        //Ball
//        com = new SwitchStrategyCommand("friction-bounce");
//        com.execute(switchBallSpy);
//        da.updateBallWorld();
//        assertEquals("command changed strategy to friction", FrictionBounceStrategy.getInstance().getName(),
//                switchBallSpy.getStrategy().getName());
//        //Fish
//        switchFishSpy.setStrategy(frictionStrategy);
//        da.updateBallWorld();
//        Mockito.verify(frictionStrategy).computeFrictionVelocity(switchFishSpy);
//
//
//        //Test RandomReflectionStrategy
//        com = new SwitchStrategyCommand("random-reflection");
//        com.execute(switchBallSpy);
//        switchFishSpy.setStrategy(reflectionStrategy);
//        da.updateBallWorld();
//        //Ball
//        assertEquals("command changed strategy to random-reflection", RandomReflectionStrategy.getInstance().getName(),
//                switchBallSpy.getStrategy().getName());
//        //Fish
//        Mockito.verify(reflectionStrategy).updateState(switchFishSpy);
//
//        //Zig Zag Strategy
//        com = new SwitchStrategyCommand("zig-zag");
//        com.execute(switchBallSpy);
//        switchFishSpy.setStrategy(zagStrategy);
//        da.updateBallWorld();
//        //Ball
//        assertEquals("command changed strategy to zigzag", ZigZagStrategy.getInstance().getName(),
//                switchBallSpy.getStrategy().getName());
//        //Fish
//        Mockito.verify(zagStrategy).updateState(switchFishSpy);
//
//        //Size Change Strategy
//        com = new SwitchStrategyCommand("size-change");
//        com.execute(switchBallSpy);
//        switchFishSpy.setStrategy(sizeStrategy);
//        da.updateBallWorld();
//        //ball
//        assertEquals("command changed strategy to size-change", SizeChangeStrategy.getInstance().getName(),
//                switchBallSpy.getStrategy().getName());
//        //Fish
//        Mockito.verify(sizeStrategy).updateState(switchFishSpy);
//
//        //Rotate Strategy
//        com = new SwitchStrategyCommand("rotate");
//        com.execute(switchBallSpy);
//        switchFishSpy.setStrategy(rotStrategy);
//        da.updateBallWorld();
//        //ball
//        assertEquals("command changed strategy to rotate", RotateStrategy.getInstance().getName(),
//                switchBallSpy.getStrategy().getName());
//        //Fish
//        Mockito.verify(rotStrategy).updateState(switchFishSpy);
//
//        //Hopscotch
//        com = new SwitchStrategyCommand("hopscotch");
//        com.execute(switchBallSpy);
//        switchFishSpy.setStrategy(hopScotchStrategy);
//        da.updateBallWorld();
//        //ball
//        assertEquals("command changed strategy to hopscotch", HopScotchStrategy.getInstance().getName(),
//                switchBallSpy.getStrategy().getName());
//        //Fish
//        Mockito.verify(hopScotchStrategy).updateState(switchFishSpy);
//
//        //Dead
//        com = new SwitchStrategyCommand("dead / float");
//        com.execute(switchBallSpy);
//        switchFishSpy.setStrategy(deadStrategy);
//        da.updateBallWorld();
//        //ball
//        assertEquals("command changed strategy to dead / float", DeadStrategy.getInstance().getName(),
//                switchBallSpy.getStrategy().getName());
//        //Fish
//        Mockito.verify(deadStrategy).updateState(switchFishSpy);
//
//        //Color Change
//        com = new SwitchStrategyCommand("color-change");
//        com.execute(switchBallSpy);
//        switchFishSpy.setStrategy(colorChangeStrategy);
//        da.updateBallWorld();
//        //ball
//        assertEquals("command changed strategy to color change", ColorChangeStrategy.getInstance().getName(),
//                switchBallSpy.getStrategy().getName());
//        //Fish
//        Mockito.verify(colorChangeStrategy).updateState(switchFishSpy);
//
//        //Do-the-robot
//        com = new SwitchStrategyCommand("do-the-robot");
//        com.execute(switchBallSpy);
//        switchFishSpy.setStrategy(roboticStrategy);
//        da.updateBallWorld();
//        //ball
//        assertEquals("command changed strategy to robot", RoboticStrategy.getInstance().getName(),
//                switchBallSpy.getStrategy().getName());
//        //Fish
//        Mockito.verify(roboticStrategy).updateState(switchFishSpy);
//    }
//
//    //TODO Test Collision Strategies
//    public void testCollisionStrategy() {
//        //Initialize Mocks and Spies from Annotations
//        MockitoAnnotations.initMocks(this);
//
//        //Instantiate / test static methods
//        da = new DispatchAdapter();
//        pcs = DispatchAdapter.getPCS();
//        DispatchAdapter.setCanvasDims(new Point(800, 800));
//
//
//        //Finish instantiation of spy strategies requiring DA.
//        ExplodeCollision expCollision = ExplodeCollision.getInstance(da);
//        ExplodeCollision explodeCollision = Mockito.spy(expCollision);
//        IncinerateCollision incCollision = IncinerateCollision.getInstance(da);
//        IncinerateCollision incinerateCollision = Mockito.spy(incCollision);
//        AbsorbCollision absCollision = AbsorbCollision.getInstance(da);
//        AbsorbCollision absorbCollision = Mockito.spy(absCollision);
//
//        //Add our strategy listeners.
//        da.addListener((switchBallSpy));
//        da.addListener((switchFishSpy));
//        da.addListener(ballSpy2);
//        da.addListener(fishSpy2);
//
//        //Check bounce strategy
//        resetSpyLocVel();
//        assertEquals("Ball Collision = Bounce", BounceCollision.getInstance().getName(),
//                da.getBallColStrategy().getName());
//        assertEquals("Fish Collision = Bounce", BounceCollision.getInstance().getName(),
//                da.getFishColStrategy().getName());
//        assertEquals("Ball Fish Collision = Bounce", BounceCollision.getInstance().getName(),
//                da.getBallFishColStrategy().getName());
//        da.updateBallWorld();
//        Mockito.verify(bounceCollision, atMost(1)).action(switchBallSpy, switchFishSpy);
//        Mockito.verify(bounceCollision, atMost(1)).action(switchFishSpy, switchBallSpy);
//        Mockito.verify(bounceCollision, atMost(1)).action(switchBallSpy, ballSpy2);
//        Mockito.verify(bounceCollision, atMost(1)).action(switchFishSpy, fishSpy2);
//
//        //Switch collision strat - size change, but also check to see that switchStrategy can handle a null input for selections.
//        da.switchStrategy("size-change", null, "chase", "chase", "chase");
//        //Check the Chase Collision Strategy functioning.
//        resetSpyLocVel();
//        da.updateBallWorld();
//        assertEquals("Collision switched to Chase", "Chase", da.getBallColStrategy().getName());
//        Mockito.verify(chaseCollision, atMost(1)).action(switchBallSpy, switchFishSpy);
//        Mockito.verify(chaseCollision, atMost(1)).action(switchFishSpy, switchBallSpy);
//        Mockito.verify(chaseCollision, atMost(1)).action(switchBallSpy, ballSpy2);
//        Mockito.verify(chaseCollision, atMost(1)).action(switchFishSpy, fishSpy2);
//
//        //Color change collision
//        da.switchStrategy("size-change", null, "color-change", "color-change",
//                "color-change");
//        //Check the Chase Collision Strategy functioning.
//        resetSpyLocVel();
//        da.updateBallWorld();
//        assertEquals("Collision switched to Color Change", "ColorChange", da.getBallColStrategy().getName());
//        Mockito.verify(colorChangeCollision, atMost(1)).action(switchBallSpy, switchFishSpy);
//        Mockito.verify(colorChangeCollision, atMost(1)).action(switchFishSpy, switchBallSpy);
//        Mockito.verify(colorChangeCollision, atMost(1)).action(switchBallSpy, ballSpy2);
//        Mockito.verify(colorChangeCollision, atMost(1)).action(switchFishSpy, fishSpy2);
//
//        //Dodge
//        da.switchStrategy("size-change", null, "scared-dodge", "scared-dodge",
//                "scared-dodge");
//        //Check the Chase Collision Strategy functioning.
//        resetSpyLocVel();
//        da.updateBallWorld();
//        assertEquals("Collision switched to Dodge", "Dodge", da.getBallColStrategy().getName());
//        Mockito.verify(dodgeCollision, atMost(1)).action(switchBallSpy, switchFishSpy);
//        Mockito.verify(dodgeCollision, atMost(1)).action(switchFishSpy, switchBallSpy);
//        Mockito.verify(dodgeCollision, atMost(1)).action(switchBallSpy, ballSpy2);
//        Mockito.verify(dodgeCollision, atMost(1)).action(switchFishSpy, fishSpy2);
//
//        //Excite
//        da.switchStrategy("size-change", null, "excitation", "excitation",
//                "excitation");
//        //Check the Chase Collision Strategy functioning.
//        resetSpyLocVel();
//        da.updateBallWorld();
//        assertEquals("Collision switched to Excitation", "Excitation", da.getBallColStrategy().getName());
//        Mockito.verify(exciteCollision, atMost(1)).action(switchBallSpy, switchFishSpy);
//        Mockito.verify(exciteCollision, atMost(1)).action(switchFishSpy, switchBallSpy);
//        Mockito.verify(exciteCollision, atMost(1)).action(switchBallSpy, ballSpy2);
//        Mockito.verify(exciteCollision, atMost(1)).action(switchFishSpy, fishSpy2);
//
//        //Explode
//        da.switchStrategy("size-change", null, "explode", "explode",
//                "explode");
//        //Check the Chase Collision Strategy functioning.
//        resetSpyLocVel();
//        da.updateBallWorld();
//        assertEquals("Collision switched to Explode", "Explode", da.getBallColStrategy().getName());
//        Mockito.verify(explodeCollision, atMost(1)).action(switchBallSpy, switchFishSpy);
//        Mockito.verify(explodeCollision, atMost(1)).action(switchFishSpy, switchBallSpy);
//        Mockito.verify(explodeCollision, atMost(1)).action(switchBallSpy, ballSpy2);
//        Mockito.verify(explodeCollision, atMost(1)).action(switchFishSpy, fishSpy2);
//        //After a collision that may remove listeners, reset the listeners.
//        resetListeners(da);
//        resetSpyLocVel();
//
//        //Incinerate
//        da.switchStrategy("size-change", null, "incinerate", "incinerate",
//                "incinerate");
//        //Check the Chase Collision Strategy functioning.
//        resetSpyLocVel();
//        da.updateBallWorld();
//        assertEquals("Collision switched to Incinerate", "Incinerate", da.getBallColStrategy().getName());
//        Mockito.verify(incinerateCollision, atMost(1)).action(switchBallSpy, switchFishSpy);
//        Mockito.verify(incinerateCollision, atMost(1)).action(switchFishSpy, switchBallSpy);
//        Mockito.verify(incinerateCollision, atMost(1)).action(switchBallSpy, ballSpy2);
//        Mockito.verify(incinerateCollision, atMost(1)).action(switchFishSpy, fishSpy2);
//        //After a collision that may remove listeners, reset the listeners.
//        resetListeners(da);
//        resetSpyLocVel();
//
//        //Absorb
//        da.switchStrategy("size-change", null, "eat-absorb", "eat-absorb",
//                "eat-absorb");
//        //Check the Chase Collision Strategy functioning.
//        resetSpyLocVel();
//        da.updateBallWorld();
//        assertEquals("Collision switched to Absorb", "Absorb", da.getBallColStrategy().getName());
//        Mockito.verify(absorbCollision, atMost(1)).updateState(switchBallSpy, switchFishSpy);
//        Mockito.verify(absorbCollision, atMost(1)).updateState(switchFishSpy, switchBallSpy);
//        Mockito.verify(absorbCollision, atMost(1)).updateState(switchBallSpy, ballSpy2);
//        Mockito.verify(absorbCollision, atMost(1)).updateState(switchFishSpy, fishSpy2);
//        //After a collision that may remove listeners, reset the listeners.
//        resetListeners(da);
//        resetSpyLocVel();
//
//
//
//
//        //TODO: Add additional objects (one more ball, one more fish).
//
//        //TODO: Place the locations at the same place to force a collision.
//    }
//
//    /**
//     * Helper method to reset the location and velocity of the objects to induce a collision and check all code paths.
//     */
//    private void resetSpyLocVel() {
//        switchBallSpy.setLocation(loc);
//        switchBallSpy.setVelocity(vel);
//        switchFishSpy.setLocation(loc);
//        switchFishSpy.setVelocity(vel);
//        ballSpy2.setLocation(loc);
//        ballSpy2.setVelocity(vel);
//        fishSpy2.setLocation(loc);
//        fishSpy2.setVelocity(vel);
//    }
//
//    /**
//     * Reset Listeners - Helper method to reset listeners after a listener may have been removed.
//     * @param da
//     */
//    private void resetListeners(DispatchAdapter da) {
//        da.removeListeners();
//        //Re-add relevant spy object instance variables.
//        da.addListener((switchBallSpy));
//        da.addListener((switchFishSpy));
//        da.addListener(ballSpy2);
//        da.addListener(fishSpy2);
//    }
//}