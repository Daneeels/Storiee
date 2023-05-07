package com.example.storiee

import com.example.storiee.data.response.ListStoryItem


object DataDummy {
    fun generateDummyStories(): List<ListStoryItem> {
        val listStory = ArrayList<ListStoryItem>()
        for (i in 1..20) {
            val story = ListStoryItem(
                createdAt = "2023-01-22T22:22:22Z",
                description = "Desc $i",
                id = "id_$i",
                lat = i.toDouble() * 10,
                lon = i.toDouble() * 10,
                name = "Nama $i",
                photoUrl = "https://d1vbn70lmn1nqe.cloudfront.net/prod/wp-content/uploads/2021/10/28064854/12.-Tips-Merawat-Anak-Kucing-Munchkin.jpg"
            )
            listStory.add(story)
        }

        return listStory
    }
}