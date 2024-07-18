package io.cloudflight.architecture.structurizr.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.io.File;

/**
 * Configuration properties for Structurizr.
 *
 * @author Klaus Lehner
 */
@ConfigurationProperties("structurizr")
public record StructurizrProperties(Workspace workspace, Export export) {

    public record Export(
            StructurizrExport structurizr,
            C4PlantUmlExport c4PlantUml
    ) {
    }

    /**
     * @param enabled                  if <code>true</code>, the workspace will be exported to a Structurizr server
     * @param url                      the url of the Structurizr server, the default is <code>https://api.structurizr.com</code>
     * @param id
     * @param key                      Workspace API key
     * @param secret                   Workspace API secret
     * @param encryptionPassphrase     Workspace client-side encryption passphrase
     * @param workspaceArchiveLocation Directory to use to archive workspaces.
     */
    public record StructurizrExport(
            @DefaultValue("false")
            boolean enabled,
            @DefaultValue("https://api.structurizr.com")
            String url,
            @DefaultValue("0")
            Long id,
            String key,
            String secret,
            String encryptionPassphrase,
            File workspaceArchiveLocation
    ) {
    }

    /**
     * @param enabled         if <code>true</code>, the Workspace will be exported using the PlantUML exporter
     * @param outputDirectory
     */
    public record C4PlantUmlExport(
            @DefaultValue("false")
            boolean enabled,
            @DefaultValue("build/c4PlantUml")
            File outputDirectory
    ) {
    }

    /**
     * @param name                       workspace name
     * @param description                workspace description
     * @param isAddImplicitRelationships Propagates all relationships from children to their parents.
     */
    public record Workspace(
            String name,
            String description,
            @DefaultValue("true")
            boolean isAddImplicitRelationships
    ) {
    }
}
