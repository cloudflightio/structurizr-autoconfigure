package io.cloudflight.architecture.structurizr.autoconfigure

import com.structurizr.Workspace
import com.structurizr.api.StructurizrClient
import com.structurizr.encryption.AesEncryptionStrategy
import com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy
import com.structurizr.model.Model
import io.cloudflight.architecture.structurizr.service.StructurizrService
import io.cloudflight.architecture.structurizr.service.export.C4PlantUmlExportService
import io.cloudflight.architecture.structurizr.service.export.StructurizrWorkspaceExportService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * @author Klaus Lehner
 */
@Configuration
@ComponentScan(basePackageClasses = [StructurizrService::class])
@EnableConfigurationProperties(value = [StructurizrProperties::class])
internal class StructurizrAutoConfiguration {
    @Bean
    fun structurizrService(workspace: Workspace, applicationContext: ApplicationContext): StructurizrService {
        return StructurizrService(workspace, applicationContext)
    }

    @Bean
    fun model(workspace: Workspace, properties: StructurizrProperties): Model {
        if (properties.workspace.isAddImplicitRelationships) {
            workspace.model.impliedRelationshipsStrategy =
                CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy()
        }
        return workspace.model
    }

    @Bean
    fun workspace(config: StructurizrProperties): Workspace {
        return Workspace(config.workspace.name, config.workspace.description)
    }

    @Configuration
    @ConditionalOnProperty(value = ["structurizr.export.structurizr.enabled"], havingValue = "true")
    internal class StructurizrExportConfiguration {
        @Bean
        fun structurizrClient(config: StructurizrProperties): StructurizrClient {
            val structurizr = config.export.structurizr
            val structurizrClient = StructurizrClient(structurizr.url, structurizr.key, structurizr.secret)

            // Add client-side encryption if configured
            if (structurizr.encryptionPassphrase != null) {
                structurizrClient.setEncryptionStrategy(AesEncryptionStrategy(structurizr.encryptionPassphrase))
            }
            structurizrClient.workspaceArchiveLocation = structurizr.workspaceArchiveLocation
            return structurizrClient
        }

        @Bean
        fun structurizrWorkspaceExporter(
            properties: StructurizrProperties,
            client: StructurizrClient
        ): StructurizrWorkspaceExportService {
            return StructurizrWorkspaceExportService(properties, client)
        }
    }

    @Configuration
    @ConditionalOnProperty(value = ["structurizr.export.c4-plant-uml.enabled"], havingValue = "true")
    internal class C4PlantUmlExportConfiguration {
        @Bean
        fun c4PlantUmlExportService(properties: StructurizrProperties): C4PlantUmlExportService {
            return C4PlantUmlExportService(properties)
        }
    }
}