package io.cloudflight.architecture.structurizr.autoconfigure;

import com.structurizr.Workspace;
import com.structurizr.api.WorkspaceApiClient;
import com.structurizr.encryption.AesEncryptionStrategy;
import com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy;
import com.structurizr.model.Model;
import io.cloudflight.architecture.structurizr.internal.service.StructurizrService;
import io.cloudflight.architecture.structurizr.internal.service.export.C4PlantUmlExportService;
import io.cloudflight.architecture.structurizr.internal.service.export.StructurizrWorkspaceExportService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Klaus Lehner
 */
@Configuration
@ComponentScan(basePackageClasses = StructurizrService.class)
@EnableConfigurationProperties(value = StructurizrProperties.class)
class StructurizrAutoConfiguration {

    @Bean
    StructurizrService structurizrService(Workspace workspace, ApplicationContext applicationContext) {
        return new StructurizrService(workspace, applicationContext);
    }

    @Bean
    Model model(Workspace workspace, StructurizrProperties properties) {
        if (properties.workspace().isAddImplicitRelationships()) {
            workspace.getModel().setImpliedRelationshipsStrategy(
                    new CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy());
        }
        return workspace.getModel();
    }


    @Bean
    Workspace workspace(StructurizrProperties config) {
        return new Workspace(config.workspace().name(), config.workspace().description());
    }

    @Configuration
    @ConditionalOnProperty(value = "structurizr.export.structurizr.enabled", havingValue = "true")
    class StructurizrExportConfiguration {
        @Bean
        WorkspaceApiClient structurizrClient(StructurizrProperties config) {
            StructurizrProperties.StructurizrExport structurizr = config.export().structurizr();
            WorkspaceApiClient structurizrClient = new WorkspaceApiClient(structurizr.url(), structurizr.key(), structurizr.secret());

            // Add client-side encryption if configured
            if (structurizr.encryptionPassphrase() != null) {
                structurizrClient.setEncryptionStrategy(new AesEncryptionStrategy(structurizr.encryptionPassphrase()));
            }
            structurizrClient.setWorkspaceArchiveLocation(structurizr.workspaceArchiveLocation());
            return structurizrClient;
        }

        @Bean
        StructurizrWorkspaceExportService structurizrWorkspaceExporter(StructurizrProperties properties, WorkspaceApiClient client) {
            return new StructurizrWorkspaceExportService(properties, client);
        }
    }

    @Configuration
    @ConditionalOnProperty(value = "structurizr.export.c4-plant-uml.enabled", havingValue = "true")
    class C4PlantUmlExportConfiguration {
        @Bean
        C4PlantUmlExportService c4PlantUmlExportService(StructurizrProperties properties) {
            return new C4PlantUmlExportService(properties);
        }
    }
}