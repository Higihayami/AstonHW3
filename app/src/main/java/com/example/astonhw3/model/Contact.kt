package com.example.astonhw3.model

import java.io.Serializable

data class Contact(val id: Int, val firstName: String?, val lastName: String?, val phoneNumber: String?) :
    Serializable
