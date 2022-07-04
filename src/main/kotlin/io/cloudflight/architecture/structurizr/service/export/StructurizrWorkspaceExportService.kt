package io.cloudflight.architecture.structurizr.service.export

import com.structurizr.Workspace
import com.structurizr.api.StructurizrClient
import com.structurizr.api.StructurizrClientException
import io.cloudflight.architecture.structurizr.WorkspaceExportService
import io.cloudflight.architecture.structurizr.autoconfigure.StructurizrProperties
import org.slf4j.LoggerFactory

internal class StructurizrWorkspaceExportService(
    private val properties: StructurizrProperties,
    private val structurizrClient: StructurizrClient
) : WorkspaceExportService {
    override fun exportWorkspace(workspace: Workspace) {
        if (properties.export.structurizr.enabled) {
            try {
                LOG.info("Exporting workspace to ID " + properties.export.structurizr.id + " at " + structurizrClient.url)
                structurizrClient.putWorkspace(properties.export.structurizr.id, workspace)
            } catch (e: StructurizrClientException) {
                LOG.error("Could not put workspace.", e)
                throw IllegalArgumentException("Could not put workspace", e)
            }
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(StructurizrWorkspaceExportService::class.java)
    }
}