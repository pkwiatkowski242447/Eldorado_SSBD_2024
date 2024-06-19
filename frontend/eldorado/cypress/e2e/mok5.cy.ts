/// <reference types="cypress" />
/// <reference types="cypress-xpath" />

describe("MOK.5 Unblock user account", () => {
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

    it("Successful account unblock", () => {
        // Enter users page
        cy.xpath("/html/body/div/div/header/nav/a[2]")
            .click();

        cy.wait(2000);

        // Go to the second page
        cy.xpath("/html/body/div/div/div[1]/div/div[3]/div[2]/nav/ul/li[3]/a")
            .click();

        cy.wait(2000);

        // Open juleswinfield menu
        cy.xpath("/html/body/div/div/div[1]/div/div[3]/div[1]/table/tbody/tr[3]/td[8]/button")
            .click();

        // Choose unblock option
        cy.xpath("/html/body/div[2]/div/div[2]")
            .click();

        // Confirm block
        cy.xpath("/html/body/div[3]/button[1]")
            .click();

        // Check if toast appeared
        cy.xpath("/html/body/div/div/div[2]/ol/li/div/div[1]")
            .should("contain", "Sukces");

        cy.xpath("/html/body/div/div/div[2]/ol/li/div/div[2]")
            .should("contain", "Użytkownik został odblokowany");
    });
});
