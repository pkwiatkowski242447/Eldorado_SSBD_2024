describe('MOK.2 Authenticate', () => {
  beforeEach(() => {
    cy.visit('https://team-3.proj-sum.it.p.lodz.pl/login');
  })

  it('Correct authentication', () => {
    cy.get('#\\:r6\\:-form-item').type('jerzybem');
    cy.get('#\\:r7\\:-form-item').type('P@ssw0rd!');
    cy.get('.inline-flex').click();
    cy.get('.h-16 > .w-full').contains('.leading-none');
  })
})