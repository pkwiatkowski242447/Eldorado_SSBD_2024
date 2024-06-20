/// <reference types="cypress" />
/// <reference types="cypress-xpath" />

describe("MOK.8 Change own password", () => {
    beforeEach(() => {
        cy.visit("http://localhost:3000/login");

        // Enter login
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/div[1]/div/input")
            .type("tkarol");

        // Enter password
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/div[2]/div/input")
            .type("P@ssw0rd!");

        // Click log in button
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/button")
            .click();
    });

    it("Successful password change", () => {
        // Enter menu
        cy.xpath("/html/body/div/div/header/div/button")
            .click();

        // Choose settings tab
        cy.xpath("/html/body/div[2]/div/div[3]")
            .click();

        // Choose password change
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/nav/button[4]")
            .click();

        // Input current password
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div/div/div/div/form/div/div[1]/div/input")
            .type("P@ssw0rd!");

        // Input new password
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div/div/div/div/form/div/div[2]/div/input")
            .type("NewP@ssw0rd!");

        // Input repeated new password
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div/div/div/div/form/div/div[3]/div/input")
            .type("NewP@ssw0rd!");

        // Click change password button
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div/div/div/div/form/div/button")
            .click();

        // Confirm action
        cy.xpath("/html/body/div[3]/button[1]")
            .click();

        // Check if toast appeared
        cy.xpath("/html/body/div/div/div[2]/ol/li/div/div[1]")
            .should("contain", "Sukces!");

        cy.xpath("/html/body/div/div/div[2]/ol/li/div/div[2]")
            .should("contain", "Twoje hasło zostało pomyślnie zmienione.");

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
            .type("tkarol");

        // Enter password
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/div[2]/div/input")
            .type("NewP@ssw0rd!");

        // Click log in button
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/button")
            .click();

        // Verify whether user logged in.
        cy.xpath("/html/body/div/div/header/div/small")
            .should("contain", "Witaj, tkarol");
    });
});
