package kz.innlab.authservice.system.config.custom

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.util.Key

class GoogleIdTokenPayload: GoogleIdToken.Payload() {
    @Key("given_name")
    var given_name: String? = null

    @Key("family_name")
    var family_name: String? = null

    @Key("name")
    var name: Any? = null
}
