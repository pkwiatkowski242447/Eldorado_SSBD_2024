/// <reference types="cypress" />
/// <reference types="cypress-xpath" />

describe("MOK.6 Add user level to account", () => {
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

    it("Successful account user level add", () => {
        // Enter users page
        cy.xpath("/html/body/div/div/header/nav/a[2]")
            .click();

        cy.wait(2000);

        // Open aandrus menu
        cy.xpath("/html/body/div/div/div[1]/div/div[3]/div[1]/table/tbody/tr[1]/td[8]/button")
            .click();

        // Choose settings option
        cy.xpath("/html/body/div[2]/div/div[1]")
            .click();

        cy.wait(2000);

        // Choose user levels option
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/nav/button[2]")
            .click();

        cy.wait(2000);

        // Choose add user level option
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div/div/div/div/div/div/div[2]/div[2]/button")
            .click();

        cy.wait(2000);

        // Confirm user level addition
        cy.xpath("/html/body/div[3]/button[1]")
            .click();

        cy.wait(2000);

        // Check if toast appeared
        cy.xpath("/html/body/div/div/div[2]/ol/li/div")
            .should("contain", "Sukces!");

    });
});
