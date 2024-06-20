/// <reference types="cypress" />
/// <reference types="cypress-xpath" />

describe("MOK.11 Change e-mail user", () => {
    beforeEach(() => {
        cy.visit("http://localhost:3000/login",{
            onBeforeLoad(win: Cypress.AUTWindow) {
                Object.defineProperty(win.navigator, 'languages', {
                    value: ['pl'],
                });
            }
        })
    });

    it("Change e-mail user", () => {
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

        // Open manage users page
        cy.xpath("/html/body/div/div/header/nav/a[2]")
            .click();

        // Open dropdown for user
        cy.xpath("//*[@id=\"radix-:rv:\"]")
            .click();

        // Open user page
        cy.xpath("/html/body/div[2]/div/div[1]")
            .click();

        // Email screen
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/nav/button[4]")
            .click();

        // Enter new email
        cy.xpath("//*[@id=\":r15:-form-item\"]")
            .type("newemail@email.com");

        // Change e-mail
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div/div/div/div/form/div/button")
            .click();

        // Confirm change
        cy.xpath("/html/body/div[3]/button[1]")
            .click();

        // Check if e-mail was sent
        cy.xpath("/html/body/div/div/div[2]/ol")
            .should("contain", "Sukces!");

    });
});
