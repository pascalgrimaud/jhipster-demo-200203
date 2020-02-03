import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { AlphaComponentsPage, AlphaDeleteDialog, AlphaUpdatePage } from './alpha.page-object';

const expect = chai.expect;

describe('Alpha e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let alphaComponentsPage: AlphaComponentsPage;
  let alphaUpdatePage: AlphaUpdatePage;
  let alphaDeleteDialog: AlphaDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Alphas', async () => {
    await navBarPage.goToEntity('alpha');
    alphaComponentsPage = new AlphaComponentsPage();
    await browser.wait(ec.visibilityOf(alphaComponentsPage.title), 5000);
    expect(await alphaComponentsPage.getTitle()).to.eq('totoApp.alpha.home.title');
  });

  it('should load create Alpha page', async () => {
    await alphaComponentsPage.clickOnCreateButton();
    alphaUpdatePage = new AlphaUpdatePage();
    expect(await alphaUpdatePage.getPageTitle()).to.eq('totoApp.alpha.home.createOrEditLabel');
    await alphaUpdatePage.cancel();
  });

  it('should create and save Alphas', async () => {
    const nbButtonsBeforeCreate = await alphaComponentsPage.countDeleteButtons();

    await alphaComponentsPage.clickOnCreateButton();
    await promise.all([
      alphaUpdatePage.setFirstnameInput('firstname'),
      alphaUpdatePage.setLastnameInput('lastname'),
      alphaUpdatePage.setBirthdayInput('2000-12-31')
    ]);
    expect(await alphaUpdatePage.getFirstnameInput()).to.eq('firstname', 'Expected Firstname value to be equals to firstname');
    expect(await alphaUpdatePage.getLastnameInput()).to.eq('lastname', 'Expected Lastname value to be equals to lastname');
    expect(await alphaUpdatePage.getBirthdayInput()).to.eq('2000-12-31', 'Expected birthday value to be equals to 2000-12-31');
    await alphaUpdatePage.save();
    expect(await alphaUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await alphaComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Alpha', async () => {
    const nbButtonsBeforeDelete = await alphaComponentsPage.countDeleteButtons();
    await alphaComponentsPage.clickOnLastDeleteButton();

    alphaDeleteDialog = new AlphaDeleteDialog();
    expect(await alphaDeleteDialog.getDialogTitle()).to.eq('totoApp.alpha.delete.question');
    await alphaDeleteDialog.clickOnConfirmButton();

    expect(await alphaComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
