/// <reference types="cypress" />
/// <reference types="cypress-xpath" />

describe("MOK.9 Change other user password", () => {
    beforeEach(() => {
        cy.visit("http://localhost:3000/login");

        // Enter login
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/div[1]/div/input")
            .type("jerzybem");

        // Enter password
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/div[2]/div/input")
            .type("P@ssw0rd!");

        // Click log in button
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/button")
            .click();

        // Click account menu
        cy.xpath("/html/body/div/div/header/div/button")
            .click();

        // Change access level menu
        cy.xpath("/html/body/div[2]/div/div[5]")
            .click();

        // Click admin access level
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div/form/div/div/div[1]/button")
            .click();

        // Confirm access level change
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div/form/button")
            .click();
    });

    it("Successful password change", () => {
        // Choose user list view
        cy.xpath("/html/body/div/div/header/nav/a[2]")
            .click();

        // Choose user settings button
        cy.xpath("/html/body/div/div/div[1]/div/div[3]/div[1]/table/tbody/tr[1]/td[8]/button")
            .click();

        // Choose management tab
        cy.xpath("/html/body/div[2]/div/div[1]")
            .click();

        // Select password change
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/nav/button[5]")
            .click();

        // Click reset password button
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div/div/div/div/button")
            .click();

        // Confirm password rest
        cy.xpath("/html/body/div[3]/button[1]")
            .click();

        // Check if toast appeared
        cy.xpath("/html/body/div/div/div[2]/ol/li/div/div[1]")
            .should("contain", "Sukces!");

        cy.xpath("/html/body/div/div/div[2]/ol/li/div/div[2]")
            .should("contain", "Mail do zmiany hasła został wysłany na adres E-Mail przypisany do konta użytkownika.");

        // Try to authenticate with new password
        // Logout
        // Enter menu
        cy.xpath("/html/body/div/div/header/div/button")
            .click();

        // Choose logout action
        cy.xpath("/html/body/div[2]/div/div[7]")
            .click();

        // Authenticate
        // Enter login
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/div[1]/div/input")
            .type("aandrus");

        // Enter password
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/div[2]/div/input")
            .type("P@ssw0rd!");

        // Click log in button
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/button")
            .click();

        // Verify whether user logged in.
        cy.xpath("/html/body/div/div/div[2]/ol/li/div/div")
            .should("contain", "Reset hasła został zainicjowany przez administratora. Wejdź w link wysłany na Twój adres e-mail, aby kontynuować.");
    });
});
