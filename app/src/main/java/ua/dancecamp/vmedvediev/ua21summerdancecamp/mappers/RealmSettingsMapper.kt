package ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers

import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.ApplicationSettings
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.entity.RealmSettings

class RealmSettingsMapper : Mapper<RealmSettings, ApplicationSettings> {

    override fun from(initialObject: RealmSettings) = ApplicationSettings(initialObject.id,
            initialObject.interfaceLanguage, initialObject.localeLanguage)

    override fun to(initialObject: ApplicationSettings) = RealmSettings(initialObject.id,
            initialObject.interfaceLanguage, initialObject.localeLanguage)
}