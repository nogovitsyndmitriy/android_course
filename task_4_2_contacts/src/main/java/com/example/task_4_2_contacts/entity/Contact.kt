package com.example.task_4_2_contacts.entity

import java.io.Serializable
import java.util.*

class Contact(
    var name: String? = "",
    var phone: String? = null,
    var email: String? = null,
    var isPhone: Boolean = true
) : Serializable {
    val id: String = UUID.randomUUID().toString()
}