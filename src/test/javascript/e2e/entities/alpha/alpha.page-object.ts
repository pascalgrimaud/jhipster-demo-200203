import { element, by, ElementFinder } from 'protractor';

export class AlphaComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-alpha div table .btn-danger'));
  title = element.all(by.css('jhi-alpha div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class AlphaUpdatePage {
  pageTitle = element(by.id('jhi-alpha-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  firstnameInput = element(by.id('field_firstname'));
  lastnameInput = element(by.id('field_lastname'));
  birthdayInput = element(by.id('field_birthday'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setFirstnameInput(firstname: string): Promise<void> {
    await this.firstnameInput.sendKeys(firstname);
  }

  async getFirstnameInput(): Promise<string> {
    return await this.firstnameInput.getAttribute('value');
  }

  async setLastnameInput(lastname: string): Promise<void> {
    await this.lastnameInput.sendKeys(lastname);
  }

  async getLastnameInput(): Promise<string> {
    return await this.lastnameInput.getAttribute('value');
  }

  async setBirthdayInput(birthday: string): Promise<void> {
    await this.birthdayInput.sendKeys(birthday);
  }

  async getBirthdayInput(): Promise<string> {
    return await this.birthdayInput.getAttribute('value');
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class AlphaDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-alpha-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-alpha'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
