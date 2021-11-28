package com.CMPUT301F21T19.habitappt.Utils;

import android.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class DualCustomTextWatcher implements TextWatcher {

    Integer minLength1;
    Integer maxLength1;
    Integer minLength2;
    Integer maxLength2;
    private EditText editText1;
    private EditText editText2;
    Button button;
    Boolean goodInput = false;

    public DualCustomTextWatcher(EditText editText1 , EditText editText2, Button button, Integer minLength1, Integer maxLength1, Integer minLength2, Integer maxLength2) {

        this.minLength1 = minLength1;
        this.maxLength1 = maxLength1;
        this.minLength2 = minLength2;
        this.maxLength2 = maxLength2;
        this.editText1 = editText1;
        this.editText2 = editText2;
        this.button = button;

    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable == editText1.getEditableText()) {
            //if good length
            if (editable.length() >= minLength1 && editable.length() <= maxLength1 && editText2.length() >= minLength2 && editText2.length() <= maxLength2) {
                button.setEnabled(true);

            } else {
                button.setEnabled(false);
                checkInput();
            }
        }
        if (editable == editText2.getEditableText()) {
            //if good length
            if (editable.length() >= minLength2 && editable.length() <= maxLength2 && editText1.length() >= minLength1 && editText1.length() <= maxLength1) {
                button.setEnabled(true);

            } else {
                button.setEnabled(false);
                checkInput();
            }
        }
    }

    public void checkInput(){
        if(editText1.length() < minLength1){
            editText1.setError("Cannot be empty");
        }
        //too long
        if(editText1.length() > maxLength1){
            editText1.setError("Maximum Length 0f 20: Please reduce");
        }
        if(editText2.length() < minLength2){
            editText2.setError("Cannot be empty");
        }
        //too long
        if(editText2.length() > maxLength2){
            editText2.setError("\"Maximum Length 0f 30: Please reduce\"");
        }
    }

}
