package app.dotoo

import app.dotoo.config.core.ConfigurationManager
import app.dotoo.config.core.ConfigurationReader

fun main() {
    println("""
         _____     ______        ______   ______     ______    
        /\  __-.  /\  __ \      /\__  _\ /\  __ \   /\  __ \   
        \ \ \/\ \ \ \ \/\ \     \/_/\ \/ \ \ \/\ \  \ \ \/\ \  
         \ \____-  \ \_____\       \ \_\  \ \_____\  \ \_____\ 
          \/____/   \/_____/        \/_/   \/_____/   \/_____/ 
                                                       
    """.trimIndent())

    /**
     * Load configuration properties (environment)
     */
    val configInitializer = ConfigurationManager(
        packageName = ConfigurationManager.DEFAULT_CONFIG_PACKAGE,
        configurationReader = ConfigurationReader::read
    )

    configInitializer.initialize()
}