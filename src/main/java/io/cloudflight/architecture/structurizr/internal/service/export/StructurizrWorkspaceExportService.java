package io.cloudflight.architecture.structurizr.internal.service.export;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClientException;
import com.structurizr.api.WorkspaceApiClient;
import io.cloudflight.architecture.structurizr.WorkspaceExportService;
import io.cloudflight.architecture.structurizr.autoconfigure.StructurizrProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public final class StructurizrWorkspaceExportService implements WorkspaceExportService {

    private final StructurizrProperties properties;
    private final WorkspaceApiClient structurizrClient;

    private static final Logger LOG = LoggerFactory.getLogger(StructurizrWorkspaceExportService.class);

    public StructurizrWorkspaceExportService(StructurizrProperties properties, WorkspaceApiClient structurizrClient) {
        this.properties = properties;
        this.structurizrClient = structurizrClient;
    }

    @Override
    public void exportWorkspace(@Nonnull Workspace workspace) {
        if (properties.export().structurizr().enabled()) {
            try {
                LOG.info("Exporting workspace to ID {}", properties.export().structurizr().id());
                structurizrClient.putWorkspace(properties.export().structurizr().id(), workspace);
            } catch (StructurizrClientException e) {
                LOG.error("Could not put workspace.", e);
                throw new IllegalArgumentException("Could not put workspace", e);
            }
        }
    }
}