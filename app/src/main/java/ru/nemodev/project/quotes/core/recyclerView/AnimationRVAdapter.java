package ru.nemodev.project.quotes.core.recyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AnimationUtils;

import java.util.List;

import ru.nemodev.project.quotes.R;

public abstract class AnimationRVAdapter<T, VH extends RecyclerView.ViewHolder> extends SimpleRVAdapter<T, VH>
{
    private int prevItemPos;
    private boolean animationEnable;

    protected AnimationRVAdapter(Context context, List<T> data)
    {
        super(context, data);
        this.prevItemPos = -1;
        this.animationEnable = true;
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i)
    {
        doOnBindViewHolder(vh, i);
        doAnimationView(vh, i);
    }

    protected abstract void doOnBindViewHolder(@NonNull VH vh, int position);

    protected void doAnimationView(@NonNull VH vh, int position)
    {
        if (!animationEnable)
            return;

        if (prevItemPos < position)
        {
            vh.itemView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.item_slide_from_bottom));
        }
        else
        {
            vh.itemView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.item_slide_to_bottom));
        }

        prevItemPos = position;
    }

    public void setAnimationEnable(boolean animationEnable)
    {
        this.animationEnable = animationEnable;
    }
}
