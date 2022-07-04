package io.cloudflight.architecture.structurizr

import com.structurizr.Workspace

interface WorkspaceExportService {
    fun exportWorkspace(workspace: Workspace)
}