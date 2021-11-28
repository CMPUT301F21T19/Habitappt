package com.CMPUT301F21T19.habitappt.Utils;

import android.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class CustomTextWatcher implements TextWatcher{

    private Integer minLength;
    private Integer maxLength;
    private EditText editText;
    private Button button;
    private Boolean goodText = false;

    public CustomTextWatcher (EditText editText , Button button, Integer minLength, Integer maxLength) {

        this.minLength = minLength;
        this.maxLength = maxLength;
        this.editText = editText;
        this.button = button;

    }


    public void checkInput(){
        if(editText.length() < minLength){
            editText.setError("Too short");
        }
        //too long
        if(editText.length() > maxLength){
            editText.setError("Too long");
        }
    }

    public Boolean getGoodText() {
        return goodText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        //if habit title changed
        if (editable == editText.getEditableText()) {
            //if good length
            if (editable.length() >= minLength && editable.length() <= maxLength) {
                button.setEnabled(true);
                goodText = true;
            } else {
                button.setEnabled(false);
                //if false, update error reason
                goodText = false;
                checkInput();
            }
        }
    }
}
