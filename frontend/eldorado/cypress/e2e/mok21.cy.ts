/// <reference types="cypress" />
/// <reference types="cypress-xpath" />

describe("MOK.21 Create user by admin", () => {
    let random = Math.floor(Math.random() * 100);

    beforeEach(() => {
        cy.visit("http://localhost:3000/login",{
            onBeforeLoad(win: Cypress.AUTWindow) {
                Object.defineProperty(win.navigator, 'languages', {
                    value: ['pl'],
                });
            }
        })
    });

    it("Create client by admin", () => {

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
        cy.xpath("/html/body/div/div/header/nav/a[3]")
            .click();

        // Enter name
        cy.xpath("//*[@id=\":rd:-form-item\"]")
            .type("Andrzej");

        // Enter lastname
        cy.xpath("//*[@id=\":re:-form-item\"]")
            .type("Nazwiskowski");

        // Enter phone number
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div[2]/form/div/div[2]/div[1]/div/div/input")
            .type("123145179");

        // Enter email
        cy.xpath("//*[@id=\":rh:-form-item\"]")
            .type(`supermail${random}@email.com`);

        // Enter login
        cy.xpath("//*[@id=\":ri:-form-item\"]")
            .type(`zzandrzej${random}`);

        // Enter password
        cy.xpath("//*[@id=\":rj:-form-item\"]")
            .type("P@ssw0rd!");

        // Enter confirm password
        cy.xpath("//*[@id=\":rk:-form-item\"]")
            .type("P@ssw0rd!");

        // Create account
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div[2]/form/div/button")
            .click();

        // Confirm creating account
        cy.xpath("/html/body/div[3]/button[1]")
            .click();

        // Check toast
        cy.xpath("/html/body/div/div/div[2]/ol")
            .should("contain", "Konto zostało pomyślnie utworzone.");

        random+=1;
    });

    it("Create staff by admin", () => {

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
        cy.xpath("/html/body/div/div/header/nav/a[3]")
            .click();

        // Enter name
        cy.xpath("//*[@id=\":rd:-form-item\"]")
            .type("Andrzej");

        // Enter lastname
        cy.xpath("//*[@id=\":re:-form-item\"]")
            .type("Nazwiskowski");

        // Enter phone number
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div[2]/form/div/div[2]/div[1]/div/div/input")
            .type("123145179");

        // Enter email
        cy.xpath("//*[@id=\":rh:-form-item\"]")
            .type(`supermail${random}@email.com`);

        // Enter login
        cy.xpath("//*[@id=\":ri:-form-item\"]")
            .type(`zzandrzej${random}`);

        // Enter password
        cy.xpath("//*[@id=\":rj:-form-item\"]")
            .type("P@ssw0rd!");

        // Enter confirm password
        cy.xpath("//*[@id=\":rk:-form-item\"]")
            .type("P@ssw0rd!");

        // Change to staff user level
        cy.xpath("//*[@id=\":ro:-form-item\"]")
            .click();

        // Create account
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div[2]/form/div/button")
            .click();

        // Confirm creating account
        cy.xpath("/html/body/div[3]/button[1]")
            .click();

        // Check toast
        cy.xpath("/html/body/div/div/div[2]/ol")
            .should("contain", "Konto zostało pomyślnie utworzone.");

        random+=1;
    });

    it("Create admin by admin", () => {

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
        cy.xpath("/html/body/div/div/header/nav/a[3]")
            .click();

        // Enter name
        cy.xpath("//*[@id=\":rd:-form-item\"]")
            .type("Andrzej");

        // Enter lastname
        cy.xpath("//*[@id=\":re:-form-item\"]")
            .type("Nazwiskowski");

        // Enter phone number
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div[2]/form/div/div[2]/div[1]/div/div/input")
            .type("123145179");

        // Enter email
        cy.xpath("//*[@id=\":rh:-form-item\"]")
            .type(`supermail${random}@email.com`);

        // Enter login
        cy.xpath("//*[@id=\":ri:-form-item\"]")
            .type(`zzandrzej${random}`);

        // Enter password
        cy.xpath("//*[@id=\":rj:-form-item\"]")
            .type("P@ssw0rd!");

        // Enter confirm password
        cy.xpath("//*[@id=\":rk:-form-item\"]")
            .type("P@ssw0rd!");

        // Change to admin user level
        cy.xpath("//*[@id=\":rq:-form-item\"]")
            .click();

        // Create account
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div[2]/form/div/button")
            .click();

        // Confirm creating account
        cy.xpath("/html/body/div[3]/button[1]")
            .click();

        // Check toast
        cy.xpath("/html/body/div/div/div[2]/ol")
            .should("contain", "Konto zostało pomyślnie utworzone.");

    });
});
