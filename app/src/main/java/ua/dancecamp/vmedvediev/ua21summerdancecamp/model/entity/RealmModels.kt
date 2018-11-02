package ua.dancecamp.vmedvediev.ua21summerdancecamp.model.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RealmEvent(
        @PrimaryKey var id: String = "",
        var name: String = "",
        var date: String = "",
        var noteText: String = "",
        var noteDate: String = "",
        var dateType: Int = 0,
        var eventTime: String = "",
        var canHaveNote: Boolean = false,
        var image: String = ""
) : RealmObject()

open class RealmSettings(
        @PrimaryKey var id: String = "1",
        var interfaceLanguage: String = "",
        var localeLanguage: String = ""
) : RealmObject()

open class RealmCredentials(
        @PrimaryKey var id: String = "",
        var password: String = "",
        var isFingerPrintAllowed: Boolean = false
) : RealmObject()