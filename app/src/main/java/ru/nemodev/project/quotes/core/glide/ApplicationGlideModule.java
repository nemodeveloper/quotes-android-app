package ru.nemodev.project.quotes.core.glide;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;


@GlideModule
public class ApplicationGlideModule extends AppGlideModule
{
    @Override
    public void applyOptions(Context context, GlideBuilder builder)
    {
        builder.setDefaultRequestOptions(
                new RequestOptions()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .disallowHardwareConfig());
    }

    @Override
    public boolean isManifestParsingEnabled()
    {
        return false;
    }

}
