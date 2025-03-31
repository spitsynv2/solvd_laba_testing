package com.solvd.mobile;

import com.solvd.mobile.common.LoginPageBase;
import com.solvd.mobile.common.WelcomePageBase;
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.utils.mobile.IMobileUtils;
import org.testng.annotations.Test;

public class MobileAndroidTest implements IAbstractTest, IMobileUtils {

    @Test()
    public void testUIElements() {
        WelcomePageBase welcomePage = initPage(getDriver(), WelcomePageBase.class);
        LoginPageBase loginPage = welcomePage.clickNextBtn();
    }

}
