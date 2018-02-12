package com.adriangrabowski.android.oudegreecalculator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Adrian on 14/08/2017.
 */

public class DegreeCalculator implements Serializable {

    private Set<OUModule> level2modules;


    private Set<OUModule> level3modules;


    public DegreeCalculator() {
        level2modules = new HashSet<>();
        level3modules = new HashSet<>();
    }

    public DegreeCalculator(List<OUModule> ouModuleList) {
        this();

        for (OUModule module : ouModuleList) {

            if (module.getLevel() == 2) {
                level2modules.add(module);
            } else if (module.getLevel() == 3) {
                level3modules.add(module);
            }

        }
    }




    public void reset() {
        level2modules.clear();
        level3modules.clear();
    }

    public void addModule(OUModule module) {
        if (module.getLevel() == 2) {
            level2modules.add(module);
        } else if (module.getLevel() == 3) {
            level3modules.add(module);
        }
    }


    public void addModule(int level, String code, String name, int credits, int grade) {
        if (level == 2) {
            level2modules.add(new OUModule(code, name, credits, level, grade));
        } else if (level == 3) {
            level3modules.add(new OUModule(code, name, credits, level, grade));
        }
    }

    public int getTotalCreditsLevel2() {
        int total = 0;

        for (OUModule module : level2modules) {
            total += module.getNumberOfCredits();
        }

        return total;
    }

    public int getTotalCreditsLevel3() {
        int total = 0;

        for (OUModule module : level3modules) {
            total += module.getNumberOfCredits();
        }
        return total;
    }


    public int getTotalWeightedCreditsForLevel2() {
        return getTotalWeightedCredits(level2modules);
    }

    public int getTotalWeightedCreditsForLevel3() {
        return getTotalWeightedCredits(level3modules);
    }

    private int getTotalWeightedCredits(Set<OUModule> chosenLevel) {

        int total = 0;

        for (OUModule module : chosenLevel) {

            total += module.getWeightedGradeCredits();

        }

        return total;
    }

    public int calculateClassOfHonours() {

        int total = 0;
        int classOfHonours;

        total += getTotalWeightedCreditsForLevel2();
        total += getTotalWeightedCreditsForLevel3();

        if (total <= 630) {

            classOfHonours = 1;

        } else if (total <= 900) {

            classOfHonours = 2;
        } else if (total <= 1170) {

            classOfHonours = 3;
        } else if (total <= 1440) {

            classOfHonours = 4;
        } else {
            throw new IllegalStateException("Total weighted credits should not exceed 1440");
        }

        return classOfHonours;

    }

    public int calculateQualityAssurance() {


        ArrayList<OUModule> thirtyCreditModules = new ArrayList<>();
        ArrayList<OUModule> sixtyCreditModules = new ArrayList<>();

        Set<OUModule> best60 = new HashSet<>();

        for (OUModule module : level3modules) {

            if (module.getNumberOfCredits() == 60) {
                sixtyCreditModules.add(module);
            } else if (module.getNumberOfCredits() == 30) {
                thirtyCreditModules.add(module);
            }
        }


        Collections.sort(thirtyCreditModules);
        Collections.sort(sixtyCreditModules);

        int scoreOfBest60Modules = 99999;
        int scoreOfTwoBest30Modules = 9999;

        if (sixtyCreditModules.size() > 0) {

            scoreOfBest60Modules = sixtyCreditModules.get(0).getGradeOfPass() * sixtyCreditModules.get(0).getNumberOfCredits();

        }

        if (thirtyCreditModules.size() > 0) {

            scoreOfTwoBest30Modules = thirtyCreditModules.get(0).getGradeOfPass() * thirtyCreditModules.get(0).getNumberOfCredits() +
                    thirtyCreditModules.get(1).getGradeOfPass() * thirtyCreditModules.get(1).getNumberOfCredits();

        }


        if (scoreOfBest60Modules < scoreOfTwoBest30Modules) {
            best60.add(sixtyCreditModules.get(0));
        } else {
            best60.add(thirtyCreditModules.get(0));
            best60.add(thirtyCreditModules.get(1));
        }


        int qualityAssurancePoints = 0;


        for (OUModule module : best60) {
            qualityAssurancePoints += (module.getNumberOfCredits() * module.getGradeOfPass());
        }

        if (qualityAssurancePoints <= 60) {
            return 1;
        } else if (qualityAssurancePoints >= 61 && qualityAssurancePoints <= 120) {
            return 2;
        } else if (qualityAssurancePoints >= 121 && qualityAssurancePoints <= 180) {
            return 3;
        } else if (qualityAssurancePoints >= 181 && qualityAssurancePoints <= 240) {
            return 4;
        } else {
            throw new IllegalStateException("Something went wrong, quality assurance " +
                    "point should be between 60 and 240");
        }


    }

    public int calculateFinalClassOfHonours() {


        int coh = calculateClassOfHonours();
        int qa = calculateQualityAssurance();


        if (coh == qa) {
            return coh;
        } else if (coh > qa) {
            return coh;
        } else {
            return qa;
        }


    }

    public void removeModule(OUModule module) {

        level2modules.remove(module);
        level3modules.remove(module);
    }

    public Set<OUModule> getLevel2modules() {
        return level2modules;
    }

    public Set<OUModule> getLevel3modules() {
        return level3modules;
    }

    public List<OUModule> getAllModules() {

        Set<OUModule> moduleSet = new HashSet<>();

        moduleSet.addAll(getLevel2modules());
        moduleSet.addAll(getLevel3modules());

        return new ArrayList<>(moduleSet);
    }


}
