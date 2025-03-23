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

import com.solvd.web.gui.pages.ebay.ComputerTabletsNetworkPage;
import com.solvd.web.gui.pages.ebay.EbayHomePage;
import com.solvd.web.gui.pages.ebay.ItemPage;
import com.solvd.web.gui.pages.ebay.LaptopsNetbooksPage;
import com.solvd.web.gui.pages.ebayv2.*;
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.core.registrar.ownership.MethodOwner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.invoke.MethodHandles;

/**
 * This sample shows how create Web test.
 *
 * @author qpsdemo
 */
public class WebSampleTest implements IAbstractTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Test
    @MethodOwner(owner = "Laba")
    public void CompareFirstLaptopOrTabletNameTest() {
        EbayHomePage ebayHomePage = new EbayHomePage(getDriver());
        ebayHomePage.open();
        Assert.assertTrue(ebayHomePage.isPageOpened(), "Home page is not opened!");

        ComputerTabletsNetworkPage computerTabletsNetworkPage = ebayHomePage.openComputerTabletsNetworkPage();
        LaptopsNetbooksPage laptopsNetbooksPage = computerTabletsNetworkPage.openLaptopsNetbooksPage();
        String firstItemText = laptopsNetbooksPage.getFirstItemTitleText();
        LOGGER.info("First item text: {}",firstItemText);

        ItemPage itemPage = laptopsNetbooksPage.openFirstItemPage();
        Assert.assertEquals(firstItemText, itemPage.getItemTitleText(), "Items titles are not equal");
    }

    @Test
    @MethodOwner(owner = "Laba")
    @Parameters({"category", "categoryPageTitle"})
    public void categoryPageTitleCompareTest(String category, String categoryPageTitle) {
        EbayHomePageV2 ebayHomePageV2 = new EbayHomePageV2(getDriver());
        ebayHomePageV2.open();

        CategoryPage categoryPage = ebayHomePageV2.selectCategory(category);
        Assert.assertEquals(categoryPageTitle,categoryPage.getCategoryPageTitle());
    }

    @Test
    @MethodOwner(owner = "Laba")
    @Parameters({"firstName", "lastName", "city", "email", "phone"})
    public void findAndCheckOutItemTest(String firstName, String lastName, String city, String email, String phone) {
        EbayHomePageV2 ebayHomePageV2 = new EbayHomePageV2(getDriver());
        ebayHomePageV2.open();

        SearchResultPage searchResultPage = ebayHomePageV2.searchForItem("book","Books");
        ItemPageV2 itemPage = searchResultPage.selectFirstResultItem();
        Assert.assertTrue(itemPage.isPageOpened(), "Item page is not opened!");
        CheckoutPage checkoutPage = itemPage.goToCheckOutPage();
        checkoutPage.checkOut(firstName,lastName,city,email,phone);
    }

}
