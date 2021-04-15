package com.catasoft.ip_finder.ui.helpers;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;

import com.catasoft.ip_finder.R;
import com.catasoft.ip_finder.databinding.LoadingDialogLayoutBinding;

public class LoadingDialog extends DialogFragment {

    private final MutableLiveData<String> liveMessage;

    public LoadingDialog(String message) {
        this.liveMessage = new MutableLiveData<>(message);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // set data binding
        LoadingDialogLayoutBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.loading_dialog_layout,null, false);
        dialogBinding.setLifecycleOwner(this);

        // link data binding with view model
        dialogBinding.setLiveMessage(liveMessage);

        // build dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        // set dialog characteristics
        builder.setView(dialogBinding.getRoot())
                .setCancelable(false);

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}
