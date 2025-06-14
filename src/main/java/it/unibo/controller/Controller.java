package it.unibo.controller;

public interface Controller {

    void userRequestedInitialPage();

    void userClickedReloadPreviews();

    void userClickedPreview(ProductPreview productPreview);

    void userClickedBack();

    void loadInitialPage();

}
