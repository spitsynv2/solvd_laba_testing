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

import com.solvd.web.gui.forms.CheckoutForm;
import com.solvd.web.gui.pages.common.ebay.*;
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.core.registrar.ownership.MethodOwner;
import com.zebrunner.carina.dataprovider.IAbstractDataProvider;
import com.zebrunner.carina.dataprovider.annotations.XlsDataSourceParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.invoke.MethodHandles;
import java.util.Map;

/**
 * This sample shows how create Web test.
 *
 * @author Laba
 */
public class EbayWebDesktopTests implements IAbstractTest, IAbstractDataProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Test(dataProvider = "DataProvider")
    @MethodOwner(owner = "Laba")
    @XlsDataSourceParameters(path = "data_source/testData.xlsx", sheet = "Page1", dsUid = "TUID")
    public void checkoutItemWithEncryptedDataTest(Map<String, String> args) {
        EbayHomePageBase ebayHomePage = initPage(getDriver(),EbayHomePageBase.class);
        ebayHomePage.open();

        SearchResultPageBase searchResultPage = ebayHomePage.searchForItem(args.get("searchText"),args.get("category"));
        ItemPageBase itemPage = searchResultPage.selectFirstResultItem();
        CheckoutPageBase checkoutPage = itemPage.goToCheckOutPage();
        CheckoutForm checkoutForm = new CheckoutForm(
                args.get("country"),
                args.get("firstName"),
                args.get("lastName"),
                args.get("city"),
                args.get("email"),
                args.get("countryCode"),
                args.get("phone"));
        checkoutPage.checkout(checkoutForm);
    }

    @Test(dataProvider = "DP1")
    @MethodOwner(owner = "Laba")
    public void itemTitleEqualsTest(String TUID, int position) {
        EbayHomePageBase ebayHomePage = initPage(getDriver(),EbayHomePageBase.class);
        ebayHomePage.open();

        CategoryPageBase electronicsPage = ebayHomePage.selectCategory("Electronics");

        ComputersTabletsNetworkPageBase computersTabletsNetworkPage = electronicsPage.openComputersTabletsNetworkPage();
        String limitedTimeDealItemName = computersTabletsNetworkPage.getLimitedTimeDealsItemName(position);

        ItemPageBase itemPageBase = computersTabletsNetworkPage.selectLimitedTimeDealsItem(position);
        String expectedItemName = itemPageBase.getItemName();

        Assert.assertEquals(limitedTimeDealItemName,expectedItemName);
    }

    @DataProvider(name = "DP1")
    public Object[][] dataprovider() {
        return new Object[][]{
                {"TUID: Test position0",0},
                {"TUID: Test position1",1},
                {"TUID: Test position2",2}
        };
    }
}
