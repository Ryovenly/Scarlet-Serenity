package com.akane.scarletserenity.model.matche

import kotlin.random.Random


class RandomPlayerMatche: PlayerMatche("Random Player") {

    fun RandomPickMatches(): Int {
        return Random.nextInt(1,3)
    }
}