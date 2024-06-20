/// <reference types="cypress" />
/// <reference types="cypress-xpath" />

describe("MOK.12 Modify own account", () => {
    beforeEach(() => {
        cy.visit("http://localhost:3000/login");

        // Enter login
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/div[1]/div/input")
            .type("piotrnowak");

        // Enter password
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/div[2]/div/input")
            .type("P@ssw0rd!");

        // Click log in button
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/button")
            .click();
    });

    it("Successful password change", () => {
        // Click account menu
        cy.xpath("/html/body/div/div/header/div/button")
            .click();

        // Choose settings tab
        cy.xpath("/html/body/div[2]/div/div[3]")
            .click();

        // Choose personal data change
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/nav/button[2]")
            .click();

        // Input username
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div/div/div/div/form/div/div[1]/div/input")
            .type("Kamil");

        // Input user lastname
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div/div/div/div/form/div/div[2]/div/input")
            .type("Chrząszcz");

        // Input phone number
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div/div/div/div/form/div/div[3]/div/div/input")
            .type("590890123");

        // Apply changes
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div/div/div/div/form/div/button")
            .click();

        // Confirm action
        cy.xpath("/html/body/div[3]/button[1]")
            .click();

        // Check if toast appeared
        cy.xpath("/html/body/div/div/div[2]/ol/li/div/div[1]")
            .should("contain", "Sukces!");

        cy.xpath("/html/body/div/div/div[2]/ol/li/div/div[2]")
            .should("contain", "Twoje dane osobowe zostały pomyślnie zmienione.");

        // Check new user data in account details
        // Choose personal data change
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/nav/button[1]")
            .click();

        // Check username nad lastname
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div/div/div/div/p[1]")
            .should("contain", "Kamil Chrząszcz")

        // Check username nad lastname
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div/div/div/div/p[4]")
            .should("contain", "+48590890123")
    });
});
