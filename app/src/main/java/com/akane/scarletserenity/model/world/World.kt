package com.akane.scarletserenity.model.world

class World(
    var id: Long,
    var name: String,
    var img: String
) {
    constructor() : this( 0,"",""
    )
}