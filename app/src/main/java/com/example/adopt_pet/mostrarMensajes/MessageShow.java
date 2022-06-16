package com.example.adopt_pet.mostrarMensajes;

import androidx.fragment.app.FragmentManager;

public class MessageShow {
    showProgressBar progressBar;
    ShowMessage showMessage;
    FragmentManager manager;

    public MessageShow(FragmentManager manager) {
        this.manager = manager;
    }

    public void init() {
        progressBar = showProgressBar.newInstance();
    }
    public void showProgress() {
        progressBar.setCancelable(false);
        progressBar.show(manager, "show_progress");
    }
    public void dismissProgress() {
        progressBar.dismiss();
    }

    public void showMessageV(String msg) {
        showMessage = ShowMessage.newInstance("Error", msg);
        showMessage.setCancelable(false);
        showMessage.show(manager, "show_message");
    }
}
