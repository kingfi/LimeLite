package com.example.limelite.models

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser
import org.parceler.Parcel

@Parcel
@ParseClassName("Link")
class Link : ParseObject() {

    companion object {
        const val KEY_LINK_USER = "userID"
        const val KEY_URL = "url"
        const val KEY_TYPE = "type"
    }

    var user: ParseUser?
        get() = getParseUser(KEY_LINK_USER)
        set(parseUser) {
            put(KEY_LINK_USER, parseUser!!)
        }

    var url: String?
        get() = getString(KEY_URL)
        set(newUrl) {
            put(KEY_URL, newUrl!!)
        }

    var type: String?
        get() = getString(KEY_TYPE)
        set(newUrl) {
            put(KEY_TYPE, newUrl!!)
        }

}
