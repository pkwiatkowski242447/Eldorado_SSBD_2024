/// <reference types="cypress" />
/// <reference types="cypress-xpath" />

describe("MOK.1 Register", () => {
    beforeEach(() => {
        cy.visit("http://localhost:3000/register");
    });

    it("Correct regisration", () => {
        // Enter first name
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/div[1]/div[1]/div[1]/div/input")
            .type("Adam");

        // Enter last name
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/div[1]/div[1]/div[2]/div/input")
            .type("Małysz");

        // Enter phone number
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/div[1]/div[2]/div[1]/div/div/input")
            .type("722728950");

        // Enter email
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/div[1]/div[2]/div[2]/div/input")
            .type("exampleEmail@email.com");

        // Enter login
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/div[1]/div[3]/div/input")
            .type("adamM");

        // Enter first password
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/div[1]/div[4]/div[1]/input")
            .type("P@ssw0rd!");

        // Enter second password
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/div[1]/div[4]/div[2]/input")
            .type("P@ssw0rd!");

        // Submit button
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/div[1]/button")
            .click();

        cy.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/ol[1]/li[1]/div[1]/div[1]")
            .should("contain", "Prawie gotowe!");
    });
});
