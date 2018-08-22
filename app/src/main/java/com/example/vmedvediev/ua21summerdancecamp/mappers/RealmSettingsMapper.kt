package com.example.vmedvediev.ua21summerdancecamp.mappers

import com.example.vmedvediev.ua21summerdancecamp.model.ApplicationSettings
import com.example.vmedvediev.ua21summerdancecamp.model.RealmSettings

class RealmSettingsMapper : Mapper<RealmSettings, ApplicationSettings> {

    override fun from(initialObject: RealmSettings) = ApplicationSettings(initialObject.id,
            initialObject.isNotificationsEnabled, initialObject.isLightTheme, initialObject.interfaceLanguage)

    override fun to(initialObject: ApplicationSettings) = RealmSettings(initialObject.id,
            initialObject.isNotificationsEnabled, initialObject.isLightTheme, initialObject.interfaceLanguage)
}