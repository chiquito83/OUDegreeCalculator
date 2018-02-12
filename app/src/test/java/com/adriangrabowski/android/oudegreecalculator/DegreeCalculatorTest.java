package com.adriangrabowski.android.oudegreecalculator;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Adrian on 06/09/2017.
 */
public class DegreeCalculatorTest {

    DegreeCalculator dc;

    OUModule m250, m256, b201, b333, t100, t332;
    OUModule dummyModule1, dummyModule2, dummyModule3, dummyModule4;

    @Before
    public void setUp() throws Exception {

        dc = new DegreeCalculator();

        //

        m250 = new OUModule("m250", "oo with java", 30, 2, 1);
        m256 = new OUModule("m256", "s with java", 30, 2, 3);
        b201 = new OUModule("b201", "business something", 60, 2, 4);

        b333 = new OUModule("b333", "business lvl 3", 60, 3, 3);
        t100 = new OUModule("t100", "software engineering", 30, 3, 1);
        t332 = new OUModule("t332", "it project", 30, 3, 2);

        //

        dummyModule1 = new OUModule("dum1", "dummy module 1", 60, 2, 1);
        dummyModule2 = new OUModule("dum2", "dummy mod 2", 60, 2, 1);
        dummyModule3 = new OUModule("dum3", "dummy mod 3", 60, 3, 3);
        dummyModule4 = new OUModule("dum4", "dummy mod 4", 60, 3, 3);


    }

    private void addValidModules1() {

        dc.addModule(m250);
        dc.addModule(m256);
        dc.addModule(b201);
        dc.addModule(b333);
        dc.addModule(t100);
        dc.addModule(t332);

    }

    private void addValidModules2() {
        dc.addModule(dummyModule1);
        dc.addModule(dummyModule2);
        dc.addModule(dummyModule3);
        dc.addModule(dummyModule4);
    }

    @Test
    public void reset() throws Exception {
        addValidModules1();
        dc.reset();

        assertTrue(dc.getTotalWeightedCreditsForLevel2() == 0);
        assertTrue(dc.getTotalWeightedCreditsForLevel3() == 0);


    }

    @Test
    public void addModule() throws Exception {


    }

    @Test
    public void calculateClassOfHonours() throws Exception {

        addValidModules1();

        int coh = dc.calculateClassOfHonours();

        int total = dc.getTotalWeightedCreditsForLevel2() + dc.getTotalWeightedCreditsForLevel3();


        assertEquals(2, coh); // coh before qa

        assertEquals(900, total); // total points


        /////////

        dc.reset();

        addValidModules2();

        coh = 0;
        total = 0;

        coh = dc.calculateClassOfHonours();
        total = dc.getTotalWeightedCreditsForLevel2() + dc.getTotalWeightedCreditsForLevel3();

        assertEquals(2, coh);
        assertEquals(840, total);


    }

    @Test
    public void calculateQualityAssurance() throws Exception {

        addValidModules1();

        int qa = dc.calculateQualityAssurance();
        assertEquals(2, qa);

        ///
        dc.reset();

        addValidModules2();

        qa = 0;
        qa = dc.calculateQualityAssurance();
        assertEquals(3, qa);


    }

    @Test
    public void calculateFinalClassOfHonours() throws Exception {
        addValidModules1();

        int fin = dc.calculateFinalClassOfHonours();

        assertEquals(2, fin);

        //

        dc.reset();
        fin = 0;
        addValidModules2();
        fin = dc.calculateFinalClassOfHonours();

        assertEquals(3, fin);

    }

    @Test
    public void removeModule() throws Exception {

    }

}