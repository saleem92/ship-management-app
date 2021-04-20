import { Component, Inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Optional } from 'ag-grid-community';
import { Ship } from 'src/app/models/Ship';
import { codeValidator } from 'src/app/validators/code-validator';

@Component({
  selector: 'app-new-ship-dialog',
  templateUrl: './new-ship-dialog.component.html',
  styleUrls: ['./new-ship-dialog.component.scss']
})
export class NewShipDialogComponent {

  shipForm: FormGroup;

  isUpdate = false;

  constructor(
    @Optional() @Inject(MAT_DIALOG_DATA) data: Ship,
    @Optional() private readonly dialogRef: MatDialogRef<NewShipDialogComponent>) {

    this.shipForm = new FormGroup({
      id: new FormControl(),
      name: new FormControl('', [Validators.required]),
      code: new FormControl('', [Validators.required, codeValidator]),
      width: new FormControl(null, [Validators.required, Validators.max(10000), Validators.pattern(/^\d/)]),
      length: new FormControl(null, [Validators.required, Validators.max(10000), Validators.pattern(/^\d/)])
    });

    if (Object.keys(data).length) {
      this.isUpdate = true;
      this.setData(data);
    }
  }

  setData(data: Ship): void {
    const { id, name, code, length, width } = data;

    this.shipForm.setValue({
      id,
      name,
      code,
      length,
      width
    });
  }

  validateDoubleOnKeyPress(event: any, key: string) {
    let charCode = (event.query) ? event.query : event.keyCode;

    if (charCode == 46 && this.shipForm.controls[key].value) {
      if (this.shipForm.controls[key].value.indexOf('.') > -1) {
        return false;
      }
    }

    if (charCode != 46 && charCode > 31
      && (charCode < 48 || charCode > 57))
      return false;

    return true;
  }

  submit() {
    this.dialogRef.close(this.shipForm.value);
  }

  close() {
    this.dialogRef.close();
  }
}
