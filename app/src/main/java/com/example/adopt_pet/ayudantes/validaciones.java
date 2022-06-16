package com.example.adopt_pet.ayudantes;

import android.content.Context;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.example.adopt_pet.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class validaciones {

    Context mContext;

    public validaciones(Context mContext) {
        this.mContext = mContext;
    }
    public boolean handleEmailValidation(@NonNull TextInputLayout email) {

        String value = Objects.requireNonNull(email.getEditText()).getText().toString().trim().replace(" ", "");

        if (isEmpty(value)) {

            email.setError(null);
            email.setErrorEnabled(false);

            if (isEmail(value)) {
                email.setError(mContext.getString(R.string.invalid_email));
                return false;

            } else {
                email.setError(null);
                email.setErrorEnabled(false);
                return true;
            }
        }

        email.setError(mContext.getString(R.string.val_empty));

        return false;
    }

    public boolean handlePasswordValidation(@NonNull TextInputLayout password) {

        String value = Objects.requireNonNull(password.getEditText()).getText().toString().trim().replace(" ", "");

        if (isEmpty(value)) {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }

        password.setError(mContext.getString(R.string.val_empty));

        return false;
    }

    boolean isEmpty(@NonNull String value) {
        return !value.isEmpty();
    }
    public boolean handleValidate(@NonNull TextInputLayout value) {

        String data = Objects.requireNonNull(value.getEditText()).getText().toString().trim();
        if (isEmpty(data)) {
            value.setError(null);
            value.setErrorEnabled(false);
            return true;
        }

        value.setError(mContext.getString(R.string.val_empty));
        return false;
    }

    public boolean isEmail(@NonNull String value) {
        return !Patterns.EMAIL_ADDRESS.matcher(value).matches();
    }
}
