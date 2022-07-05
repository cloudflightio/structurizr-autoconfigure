package io.cloudflight.architecture.structurizr.service.export

import com.structurizr.Workspace
import com.structurizr.export.Diagram
import com.structurizr.view.ThemeUtils
import io.cloudflight.architecture.structurizr.WorkspaceExportService
import io.cloudflight.architecture.structurizr.autoconfigure.StructurizrProperties
import io.cloudflight.structurizr.plantuml.ExtendedC4PlantUmlExporter
import org.slf4j.LoggerFactory
import java.io.File

internal class C4PlantUmlExportService(private val properties: StructurizrProperties) : WorkspaceExportService {

    private val c4exporter = ExtendedC4PlantUmlExporter()

    override fun exportWorkspace(workspace: Workspace) {
        val configuration = workspace.views.configuration
        configuration.addProperty(ExtendedC4PlantUmlExporter.PLANTUML_SEQUENCE_DIAGRAMS_PROPERTY, true.toString())
        configuration.addProperty(ExtendedC4PlantUmlExporter.PLANTUML_ADD_PROPERTIES_PROPERTY, true.toString())
        configuration.addProperty(ExtendedC4PlantUmlExporter.PLANTUML_ADD_TAGS_PROPERTY, true.toString())
        ThemeUtils.loadThemes(workspace)

        if (properties.export.c4PlantUml.enabled) {
            c4exporter.export(workspace).forEach {
                storeDiagram(it)
            }
        }
    }

    private fun storeDiagram(diagram: Diagram) {
        File(properties.export.c4PlantUml.outputDirectory, diagram.key + ".puml")
            .also { it.parentFile.mkdirs() }
            .also { LOG.info("Writing diagram ${diagram.key} to ${it.absolutePath}") }
            .writeText(diagram.definition)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(C4PlantUmlExportService::class.java)
    }
}