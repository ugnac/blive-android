package com.learn.wandroid.ui.home

import com.learn.wandroid.ui.home.bean.ArticleList
import com.learn.wandroid.ui.home.bean.Banner

interface HomeContract {
    interface View {
        fun bindHomeData(data: ArticleList?)
        fun loadHomeDataError()
        fun noMoreData()
        fun bindBannerData(data: List<Banner?>?)
    }
}