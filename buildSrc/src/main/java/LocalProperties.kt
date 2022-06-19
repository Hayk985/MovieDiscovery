import org.gradle.api.plugins.ExtraPropertiesExtension
import java.io.File
import java.util.*

class LocalProperties(private val extraProperties: ExtraPropertiesExtension) {

    private val properties = Properties()

    init {
        loadProperties()
    }

    private fun loadProperties() {
        properties.load((extraProperties.get(PROPS_API_KEY) as File).reader())
        properties.load((extraProperties.get(PROPS_KEYSTORE) as File).reader())
    }

    fun getApiKey(): String = properties[API_KEY_PROP].toString()
    fun getKeyStorePassword(): String = properties[STORE_PASSWORD_PROP].toString()
    fun getAlias(): String = properties[ALIAS_PROP].toString()
    fun getAliasPassword(): String = properties[ALIAS_PASSWORD_PROP].toString()
    fun getKeystoreFile(): File = extraProperties.get(PROPS_KEYSTORE_FILE) as File

    companion object {
        const val PROPS_API_KEY = "apiKeyProps"
        const val PROPS_KEYSTORE = "keyStoreProps"
        const val PROPS_KEYSTORE_FILE = "keyStoreFile"

        const val API_KEY_PROP = "API_KEY"
        const val STORE_PASSWORD_PROP = "STORE_PASSWORD"
        const val ALIAS_PROP = "KEY_ALIAS"
        const val ALIAS_PASSWORD_PROP = "KEY_ALIAS_PASSWORD"
    }
}