package com.learn.wandroid.ui.home.bean

import com.learn.wandroid.ui.home.bean.Article

/**
 * {
 * "curPage": 2,
 * "datas": [
 * {
 * "adminAdd": false,
 * "apkLink": "",
 * "audit": 1,
 * "author": "",
 * "canEdit": false,
 * "chapterId": 502,
 * "chapterName": "自助",
 * "collect": false,
 * "courseId": 13,
 * "desc": "",
 * "descMd": "",
 * "envelopePic": "",
 * "fresh": false,
 * "host": "",
 * "id": 26737,
 * "isAdminAdd": false,
 * "link": "https://juejin.cn/post/7247302955493081144",
 * "niceDate": "2023-06-25 21:27",
 * "niceShareDate": "2023-06-25 21:26",
 * "origin": "",
 * "prefix": "",
 * "projectLink": "",
 * "publishTime": 1687699671000,
 * "realSuperChapterId": 493,
 * "selfVisible": 0,
 * "shareDate": 1687699583000,
 * "shareUser": "鸿洋",
 * "superChapterId": 494,
 * "superChapterName": "广场Tab",
 * "tags": [],
 * "title": "Android小技巧：在通知RemoteViews中显示动画",
 * "type": 0,
 * "userId": 2,
 * "visible": 1,
 * "zan": 0
 * }
 * ],
 * "offset": 20,
 * "over": false,
 * "pageCount": 718,
 * "size": 20,
 * "total": 14353
 * }
 */
data class ArticleList(
    val curPage: Int,
    val datas: List<Article>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)