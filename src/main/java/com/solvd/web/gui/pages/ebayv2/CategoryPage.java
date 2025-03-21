package com.solvd.web.gui.pages.ebayv2;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.decorator.PageOpeningStrategy;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * @author Vadym Spitsyn
 * @created 2025-03-21
 */
public class CategoryPage extends AbstractPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "/html/body/div[2]/div[2]/section[1]/h1")
    private ExtendedWebElement categoryPageTitle;

    public CategoryPage(WebDriver driver) {
        super(driver);
        setPageOpeningStrategy(PageOpeningStrategy.BY_ELEMENT);
        setUiLoadedMarker(categoryPageTitle);
    }

    public String getCategoryPageTitle(){
        return categoryPageTitle.getText();
    }

}
