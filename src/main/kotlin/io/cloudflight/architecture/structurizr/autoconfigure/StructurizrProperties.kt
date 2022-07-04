package io.cloudflight.architecture.structurizr.autoconfigure

import org.springframework.boot.context.properties.ConfigurationProperties
import java.io.File

/**
 * Configuration properties for Structurizr.
 *
 * @author Klaus Lehner
 */
@ConfigurationProperties("structurizr")
class StructurizrProperties {
    /**
     * Object containing all properties of your workspace at Structurizr
     */
    var workspace: Workspace = Workspace()
    var export: Export = Export()

    class Export {
        var structurizr: StructurizrExport = StructurizrExport()
        var c4PlantUml: C4PlantUmlExport = C4PlantUmlExport()
    }

    class StructurizrExport {
        /**
         * if true, the workspace will be exported to a Structurizr server
         */
        var enabled = false

        /**
         * The URL of Structurizr.
         */
        var url = "https://api.structurizr.com"

        /**
         * Workspace identifier.
         */
        var id: Long = 0

        /**
         * Workspace API key.
         */
        var key: String? = null

        /**
         * Workspace API secret.
         */
        var secret: String? = null

        /**
         * Workspace client-side encryption passphrase
         */
        var encryptionPassphrase: String? = null

        /**
         * Directory to use to archive workspaces.
         */
        var workspaceArchiveLocation: File? = null
    }

    class C4PlantUmlExport {
        /**
         * if true, the Workspace will be exported using the PlantUML exporter
         */
        var enabled = false
        var outputDirectory = File("build/c4PlantUml")
    }

    class Workspace {
        /**
         * Workspace name.
         */
        var name: String? = null

        /**
         * Workspace description.
         */
        var description: String? = null

        /**
         * Propagates all relationships from children to their parents.
         */
        var isAddImplicitRelationships = true
    }
}