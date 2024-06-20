/// <reference types="cypress" />
/// <reference types="cypress-xpath" />

describe("MOK.3 Reset password", () => {
    beforeEach(() => {
        cy.visit("http://localhost:3000/forgot-password", {
            onBeforeLoad(win: Cypress.AUTWindow) {
                Object.defineProperty(win.navigator, 'languages', {
                    value: ['pl'],
                });
            }
        })
    });

    it("Successful password reset", () => {
        // Enter account's email address
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/div/div/div/input")
            .type("jerzybem@spoko.pl");

        // Click send reset link button
        cy.xpath("/html/body/div/div/div[1]/div/div/div[2]/form/div/button")
            .click();

        // Check if toast has the correct title
        cy.xpath("/html/body/div/div/div[2]/ol/li/div/div[1]")
            .should('contain', 'Prawie gotowe!')

        // Check if toast has the correct content
        cy.xpath("/html/body/div/div/div[2]/ol/li/div/div[2]")
            .should('contain', 'Wysłaliśmy Ci e-mail z linkiem do zmiany hasła. Sprawdź skrzynkę odbiorczą i kliknij link, aby dokończyć proces.')
    });
})