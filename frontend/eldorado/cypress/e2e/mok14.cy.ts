/// <reference types="cypress" />
/// <reference types="cypress-xpath" />

describe("MOK.14 Log out", () => {
    beforeEach(() => {
        cy.visit("http://localhost:3000/login");
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
    it("Log out", () => {

        cy.xpath("/html/body/div/div/header/div/button")
            .click();

        cy.xpath("/html/body/div[2]/div/div[7]")
            .click();

        cy.xpath("/html/body/div[1]/div/div[1]/div[2]/div/div[1]/h3")
            .should("contain", "Logowanie");
    });

});