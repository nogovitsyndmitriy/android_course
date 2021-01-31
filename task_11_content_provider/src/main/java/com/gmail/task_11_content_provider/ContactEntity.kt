package com.gmail.task_11_content_provider

import java.io.Serializable
import java.util.*

class ContactEntity(
    val id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var phone: String? = null,
    var email: String? = null,
    var isPhone: Int = 0
) : Serializable