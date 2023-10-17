package com.chamika.newsapptest.data.models

import java.io.Serializable

data class HotNews(
    val articles: List<ArticleX>
) : Serializable