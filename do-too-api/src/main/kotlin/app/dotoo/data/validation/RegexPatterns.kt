package app.dotoo.data.validation

object RegexPatterns {
    val emailPattern = """\w+@\w+\.\w+""".toRegex()
    val passwordPatterns = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*\$".toRegex()
}
