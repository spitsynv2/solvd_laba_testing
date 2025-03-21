package com.solvd.web.gui.pages.ebayv2;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * @author Vadym Spitsyn
 * @created 2025-03-21
 */
public class CheckoutPage extends AbstractPage {

    @FindBy(xpath = "//*[@id=\"firstName\"]")
    private ExtendedWebElement firstNameField;

    @FindBy(xpath = "//*[@id=\"lastName\"]")
    private ExtendedWebElement lastNameField;

    @FindBy(xpath = "//*[@id=\"city\"]")
    private ExtendedWebElement cityField;

    @FindBy(xpath = "//*[@id=\"email\"]")
    private ExtendedWebElement emailField;

    @FindBy(xpath = "//*[@id=\"emailConfirm\"]")
    private ExtendedWebElement emailConfirmField;

    @FindBy(xpath = "//*[@id=\"phoneNumber\"]")
    private ExtendedWebElement phoneField;

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void checkOut(String firstName, String lastName, String city, String email, String phone){
        firstNameField.type(firstName);
        lastNameField.type(lastName);
        cityField.type(city);
        emailField.type(email);
        emailConfirmField.type(email);
        phoneField.type(phone);
    }

}
