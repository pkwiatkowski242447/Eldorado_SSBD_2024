/// <reference types="cypress" />
/// <reference types="cypress-xpath" />

describe("MOK.2 Authenticate", () => {
    beforeEach(() => {
        cy.visit("http://localhost:3000/login", {
            onBeforeLoad(win: Cypress.AUTWindow) {
                Object.defineProperty(win.navigator, 'languages', {
                    value: ['pl'],
                });
            }
        })
    });

    it("Correct authentication", () => {
        // Enter login
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/div[1]/div/input")
            .type("jerzybem");

        // Enter password
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/div[2]/div/input")
            .type("P@ssw0rd!");

        // Click log in button
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/button")
            .click();

        // Verify whether user logged in.
        cy.xpath("/html/body/div/div/header/div/small")
            .should("contain", "Witaj, jerzybem");
    });

    it("Incorrect authentication", () => {
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/div[1]/div/input")
            .type("someInvalidLogin");

        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/div[2]/div/input")
            .type("P@ssw0rd!");

        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/button")
            .click();

        cy.xpath("/html/body/div/div/div[2]/ol/li/div/div")
            .should("contain", "Nieprawidłowa próba logowania");
    });
});
