/*******************************************************************************
 * Copyright 2020-2023 Zebrunner Inc (https://www.zebrunner.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.solvd.web;

import com.solvd.web.gui.pages.common.yahoo.*;
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.dataprovider.IAbstractDataProvider;
import com.zebrunner.carina.utils.resources.L10N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.List;

public class YahooWebDesktopLocaleTests implements IAbstractTest, IAbstractDataProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Test
    public void localeTest() {
        YahooHomePageBase yahooHomePageBase = initPage(getDriver(),YahooHomePageBase.class);
        yahooHomePageBase.open();

        YahooHomePageLocalBase yahooHomePageLocalBase = yahooHomePageBase.goToLocalPage();

        SoftAssert softAssert = new SoftAssert();
        String actualTrendingTitleText = yahooHomePageLocalBase.getTrendingTitleText();
        String expectedTrendingTitleText = L10N.getText("YahooHomePageLocal.trendingTitle");
        softAssert.assertEquals(actualTrendingTitleText,expectedTrendingTitleText, "Text not the same");
        softAssert.assertAll();
    }
}
