package io.cloudflight.architecture.structurizr.internal.service.export;

import com.structurizr.Workspace;
import com.structurizr.export.Diagram;
import com.structurizr.export.plantuml.C4PlantUMLExporter;
import com.structurizr.view.Configuration;
import com.structurizr.view.ThemeUtils;
import io.cloudflight.architecture.structurizr.WorkspaceExportService;
import io.cloudflight.architecture.structurizr.autoconfigure.StructurizrProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class C4PlantUmlExportService implements WorkspaceExportService {

    private static final Logger LOG = LoggerFactory.getLogger(C4PlantUmlExportService.class);
    private final StructurizrProperties properties;
    private final C4PlantUMLExporter c4exporter = new C4PlantUMLExporter();

    public C4PlantUmlExportService(StructurizrProperties properties) {
        this.properties = properties;
    }

    @Override
    public void exportWorkspace(@Nonnull Workspace workspace) {
        final Configuration configuration = workspace.getViews().getConfiguration();
        configuration.addProperty(C4PlantUMLExporter.PLANTUML_SEQUENCE_DIAGRAM_PROPERTY, Boolean.TRUE.toString());
        configuration.addProperty(C4PlantUMLExporter.C4PLANTUML_ELEMENT_PROPERTIES_PROPERTY, Boolean.TRUE.toString());
        configuration.addProperty(C4PlantUMLExporter.C4PLANTUML_TAGS_PROPERTY, Boolean.TRUE.toString());
        try {
            ThemeUtils.loadThemes(workspace);
        } catch (Exception e) {
            throw new RuntimeException("Could not load themes", e);
        }

        if (properties.export().c4PlantUml().enabled()) {
            c4exporter.export(workspace).forEach(this::storeDiagram);
        }
    }

    private void storeDiagram(Diagram diagram) {
        File file = new File(properties.export().c4PlantUml().outputDirectory(), diagram.getKey() + ".puml");
        LOG.info("Writing diagram ${diagram.key} to ${it.absolutePath}");
        file.getParentFile().mkdirs();
        try {
            Files.writeString(file.toPath(), diagram.getDefinition());
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not write to " + file.getAbsolutePath(), e);
        }
    }
}