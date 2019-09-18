package ru.nemodev.project.quotes.ads;

public interface AdsBanner
{
    void show();
    void hide();

    interface OnAdsListener
    {
        void onClose();
    }
}
