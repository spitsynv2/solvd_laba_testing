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
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.core.registrar.ownership.MethodOwner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
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
    public void test() {
        EbayHomePage ebayHomePage = new EbayHomePage(getDriver());
        ebayHomePage.open();
        Assert.assertTrue(ebayHomePage.isPageOpened(), "Home page is not opened!");

        ComputerTabletsNetworkPage computerTabletsNetworkPage = ebayHomePage.openComputerTabletsNetworkPage();
        Assert.assertTrue(computerTabletsNetworkPage.isPageOpened(), "Computer and Tablets page is not opened!");

        LaptopsNetbooksPage laptopsNetbooksPage = computerTabletsNetworkPage.openLaptopsNetbooksPage();
        Assert.assertTrue(laptopsNetbooksPage.isPageOpened(), "Laptops and Netbooks page is not opened!");
        String firstItemText = laptopsNetbooksPage.getFirstItemTitleText();
        LOGGER.info("First item text: {}",firstItemText);

        ItemPage itemPage = laptopsNetbooksPage.openFirstItemPage();
        Assert.assertTrue(itemPage.isPageOpened(), "First item page is not opened!");
        Assert.assertEquals(firstItemText, itemPage.getItemTitleText(), "Items titles are not equal");
    }

}
