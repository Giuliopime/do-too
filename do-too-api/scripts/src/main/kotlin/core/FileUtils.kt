package core

import java.io.File

fun createScriptOutputsFolderIfNotExisting(): File {
    return File("script-outputs").also {
        it.mkdir()
    }
}