package com.solvd.other;

import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.core.registrar.ownership.MethodOwner;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LearningTests implements IAbstractTest {

    @Test(dataProvider = "DP1")
    @MethodOwner(owner = "Laba")
    public void testMultiplyOperation(String TUID, String testRailColumn, int a, int b, int c) {
        setCases(testRailColumn.split(","));
        int actual = a * b;
        int expected = c;
        Assert.assertEquals(actual, expected, "Invalid sum result!");
    }

    @DataProvider(name = "DP1")
    public Object[][] dataprovider() {
        return new Object[][]{
                {"TUID: Data1", "111,112", 2, 3, 6},
                {"TUID: Data2", "114", 6, 6, 36},
                {"TUID: Data3", "113", 5, 8, 40}
        };
    }
}
