package io.cloudflight.architecture.structurizr;

import com.structurizr.Workspace;

import javax.annotation.Nonnull;

public interface WorkspaceExportService {

    void exportWorkspace(@Nonnull Workspace workspace);
}
