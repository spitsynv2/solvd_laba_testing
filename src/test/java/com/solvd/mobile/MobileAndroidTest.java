package com.solvd.mobile;

import com.solvd.mobile.android.ClockAlarmPage;
import com.solvd.mobile.android.ClockHomePage;
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.utils.mobile.IMobileUtils;
import org.testng.annotations.Test;

public class MobileAndroidTest implements IAbstractTest, IMobileUtils {

    @Test()
    public void testUIElementsCreate() {
        ClockHomePage clockHomePage = new ClockHomePage(getDriver());
        ClockAlarmPage clockAlarmPage = clockHomePage.goToAlarmPage();

        clockAlarmPage.createAlarm();
    }

    @Test()
    public void testUIElementsDelete() {
        ClockHomePage clockHomePage = new ClockHomePage(getDriver());
        ClockAlarmPage clockAlarmPage = clockHomePage.goToAlarmPage();

        clockAlarmPage.deleteFirstAlarm();
    }

}
