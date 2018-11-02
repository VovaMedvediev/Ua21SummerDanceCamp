package ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers

import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.Credentials
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.entity.RealmCredentials

class RealmCredentialsMapper : Mapper<RealmCredentials, Credentials> {

    override fun from(initialObject: RealmCredentials) = Credentials(initialObject.id,
            initialObject.password, initialObject.isFingerPrintAllowed)

    override fun to(initialObject: Credentials) = RealmCredentials(initialObject.id,
            initialObject.password, initialObject.isFingerPrintAllowed)
}