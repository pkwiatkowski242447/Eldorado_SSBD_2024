/// <reference types="cypress" />
/// <reference types="cypress-xpath" />

describe("MOK.24 Resend e-mail confirmation e-mail", () => {
    beforeEach(() => {
        cy.visit("http://localhost:3000/login",{
            onBeforeLoad(win: Cypress.AUTWindow) {
                Object.defineProperty(win.navigator, 'languages', {
                    value: ['pl'],
                });
            }
        })
    });

    it("Resend e-mail confirmation e-mail", () => {
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

        // Open dropdown menu
        cy.xpath("//*[@id=\"radix-:rb:\"]")
            .click();

        // Open options menu
        cy.xpath("/html/body/div[2]/div/div[3]")
            .click();

        cy.wait(2000);

        // Open e-mail screen
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/nav/button[3]")
            .click();

        cy.wait(2000);

        // Enter new e-mail
        cy.xpath("//*[@id=\":rg:-form-item\"]")
            .type("newemail@example.com");

        // Change new e-mail
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div/div/div/div[1]/form/div/button")
            .click();

        // Confirm e-mail change
        cy.xpath("/html/body/div[3]/button[1]")
            .click();

        // Check if e-mail was sent
        cy.xpath("/html/body/div/div/div[2]/ol/li/div/div[1]")
            .should("contain", "Prawie gotowe!");

        // Resend e-mail confirmation e-mail
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div/div/div/div[2]/button")
            .click();

        // Check if new e-mail was sent
        cy.xpath("/html/body/div/div/div[2]/ol/li/div/div[1]")
            .should("contain", "Sukces!");
    });
});
