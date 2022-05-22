package ru.netology.nmedia.service

internal enum class Action(val key: String) {
    like("LIKE"),
    post("POST");

    companion object {
        const val KEY_ACTION = "action"
        const val KEY_CONTENT = "content"
    }
}