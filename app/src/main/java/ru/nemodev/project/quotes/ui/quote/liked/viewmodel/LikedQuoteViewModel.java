package ru.nemodev.project.quotes.ui.quote.liked.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import ru.nemodev.project.quotes.entity.quote.QuoteInfo;
import ru.nemodev.project.quotes.repository.db.room.AppDataBase;


public class LikedQuoteViewModel extends ViewModel {

    public final LiveData<PagedList<QuoteInfo>> likedQuoteList;

    public LikedQuoteViewModel() {

        likedQuoteList = new LivePagedListBuilder<>(
                AppDataBase.getInstance().getQuoteRepository().getLiked(),
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(100)
                        .setPrefetchDistance(25)
                        .build())
                .build();
    }
}
