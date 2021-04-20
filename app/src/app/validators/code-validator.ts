import { AbstractControl } from '@angular/forms'

export function codeValidator(
    control: AbstractControl
): { [key: string]: any } | null {
    const valid = /^[a-zA-z]{4}-[0-9]{4}-[a-zA-z]{1}[0-9]{1}$/.test(control.value);

    return valid
        ? null
        : { invalidNumber: { valid: false, value: control.value } }
}