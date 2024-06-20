/// <reference types="cypress" />
/// <reference types="cypress-xpath" />

describe("MOK.18 Show user's account info", () => {
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

        cy.xpath("/html/body/div[1]/div/header/nav/a[2]")
            .click();

        cy.xpath("/html/body/div[1]/div/div[1]/div/div[3]/div[1]/table/tbody/tr[1]/td[8]/button")
            .click();

        cy.xpath("/html/body/div[2]/div/div[1]")
            .click();

        cy.xpath("/html/body/div[1]/div/div[1]/div/main/div[2]/div/div/div/h3")
            .should("contain", "Informacje o koncie");
    });

});