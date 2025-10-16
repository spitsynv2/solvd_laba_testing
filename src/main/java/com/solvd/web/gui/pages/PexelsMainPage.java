package com.solvd.web.gui.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;

public class PexelsMainPage extends AbstractPage {
    private static final Logger LOGGER = LogManager.getLogger(PexelsMainPage.class);

    private static final String URL = "https://www.pexels.com/";

    @FindBy(xpath = "(//div[contains(@class, 'MediaCard_')])[1]")
    private ExtendedWebElement firstElement;

    @FindBy(xpath = "//button[contains(@id, 'onetrust-accept')]")
    private ExtendedWebElement consentButton;

    @FindBy(xpath = "//button[@class='Modal_close__Pf0IY Modal_rightTopBtn__5MXyp']")
    private ExtendedWebElement closeButton;

    @FindBy(xpath = "//span[contains(@class, 'DownloadButton')][1]")
    private ExtendedWebElement downloadButton;

    public PexelsMainPage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL(URL);
    }

    public void openPage() {
        open();
        pause(3);
    }

    public void acceptCookiesIfPresent() {
        consentButton.clickIfPresent(3);
    }

    public void clickDownload() {
        firstElement.scrollTo();
        firstElement.hover();
        downloadButton.clickIfPresent(5);
        LOGGER.info("Clicked download button.");
        closeButton.click();
    }

}
