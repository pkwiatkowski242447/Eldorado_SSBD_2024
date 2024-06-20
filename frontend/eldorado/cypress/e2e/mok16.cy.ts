/// <reference types="cypress" />
/// <reference types="cypress-xpath" />

describe("MOK.16 Show account info", () => {
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
    it("", () => {

        cy.xpath("/html/body/div/div/header/div/button")
            .click();

        cy.xpath("/html/body/div[2]/div/div[3]")
            .click();

        cy.xpath("/html/body/div[1]/div/div[1]/div/main/div[2]/div/div/div/h3")
            .should("contain", "Informacje o koncie");

        cy.xpath("/html/body/div[1]/div/div[1]/div/main/div[2]/div/div/div/div/p[1]")
            .should("contain", "Imię");

        cy.xpath("/html/body/div[1]/div/div[1]/div/main/div[2]/div/div/div/div/p[2]")
            .should("contain", "E-mail");

        cy.xpath("/html/body/div[1]/div/div[1]/div/main/div[2]/div/div/div/div/p[3]")
            .should("contain", "Login");

        cy.xpath("/html/body/div[1]/div/div[1]/div/main/div[2]/div/div/div/div/p[4]")
            .should("contain", "Telefon");

        cy.xpath("/html/body/div[1]/div/div[1]/div/main/div[2]/div/div/div/div/p[5]")
            .should("contain", "Język konta");

    });

});