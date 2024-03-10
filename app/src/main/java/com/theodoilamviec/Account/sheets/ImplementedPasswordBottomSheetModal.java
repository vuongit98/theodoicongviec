package com.theodoilamviec.Account.sheets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.theodoilamviec.theodoilamviec.R;
import com.theodoilamviec.theodoilamviec.models.Note;
import com.theodoilamviec.theodoilamviec.sharedPreferences.SharedPref;

public class ImplementedPasswordBottomSheetModal extends BottomSheetDialogFragment {

    public interface OnUnlockListener {
        void onUnlockListener(Note note);
    }

    private SharedPref sharedPref;

    private Note note;
    private EditText pinCode;

    OnUnlockListener onUnlockListener;

    public ImplementedPasswordBottomSheetModal() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onUnlockListener = (OnUnlockListener) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onUnlockListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.password_bottom_sheet_modal, container, false);

        // get note data
        if (getArguments() != null) {
            note = (Note) requireArguments().getSerializable("data");
        }

        // pin code
        pinCode = view.findViewById(R.id.pin_code);
        pinCode.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        pinCode.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
        pinCode.addTextChangedListener(pinCodeTextWatcher);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPref = new SharedPref(requireContext());
        super.onCreate(savedInstanceState);
    }

    /**
     * text watcher checks password
     */
    private final TextWatcher pinCodeTextWatcher = new TextWatcher() {
        @SuppressLint("SetTextI18n")
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(pinCode.getText().toString())) {
                if (sharedPref.loadNotePinCode() == Integer.parseInt(pinCode.getText().toString())) {
                    onUnlockListener.onUnlockListener(note);
                    dismiss();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
