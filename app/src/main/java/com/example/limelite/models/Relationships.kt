package com.example.limelite.models

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser
import org.parceler.Parcel

@Parcel
@ParseClassName("Relationships")
class Relationships : ParseObject() {

    companion object {
        const val KEY_REQUESTOR = "requestorID"
        const val KEY_REQUESTEE = "requesteeID"
        const val KEY_STATUS = "status"
    }

    var requestor: ParseUser?
        get() = getParseUser(KEY_REQUESTOR)
        set(newRequestor) {
            put(KEY_REQUESTOR, newRequestor!!)
        }

    var requestee: ParseUser?
        get() = getParseUser(KEY_REQUESTEE)
        set(newRequestee) {
            put(KEY_REQUESTEE, newRequestee!!)
        }

    var status: Int?
        get() = getInt(KEY_STATUS)
        set(newStatus) {
            put(KEY_STATUS, newStatus!!)
        }



}