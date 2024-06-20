/// <reference types="cypress" />
/// <reference types="cypress-xpath" />

describe("MOK.13 Modify other user account", () => {
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

    it("Successful password change", () => {
        // Choose user list view
        cy.xpath("/html/body/div/div/header/nav/a[2]")
            .click();

        // Choose user settings button
        cy.xpath("/html/body/div/div/div[1]/div/div[3]/div[1]/table/tbody/tr[2]/td[8]/button")
            .click();

        // Choose management tab
        cy.xpath("/html/body/div[2]/div/div[1]")
            .click();

        // Choose personal data change
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/nav/button[3]")
            .click();

        // Input username
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div/div/div/div/form/div/div[1]/div/input")
            .type("Tony");

        // Input user lastname
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div/div/div/div/form/div/div[2]/div/input")
            .type("Baryłka");

        // Input phone number
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div/div/div/div/form/div/div[3]/div/div/input")
            .type("790890123");

        // Apply changes
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div/div/div/div/form/div/button")
            .click();

        // Confirm action
        cy.xpath("/html/body/div[3]/button[1]")
            .click();

        // Check if toast appeared
        cy.xpath("/html/body/div/div/div[2]/ol/li/div/div[1]")
            .should("contain", "Sukces!");

        cy.xpath("/html/body/div/div/div[2]/ol/li/div/div[2]")
            .should("contain", "Dane użytkownika zostały pomyślnie zmienione.");

        // Check new user data in account details
        // Choose personal data change
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/nav/button[1]")
            .click();

        // Check username nad lastname
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div/div/div/div/p[1]")
            .should("contain", "Tony Baryłka")

        // Check username nad lastname
        cy.xpath("/html/body/div/div/div[1]/div/main/div[2]/div/div/div/div/p[4]")
            .should("contain", "+48790890123")
    });
});
