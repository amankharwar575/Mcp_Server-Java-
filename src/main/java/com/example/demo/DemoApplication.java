package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;

@SpringBootApplication(
        exclude = {
                org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
                org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class
        }
)
public class DemoApplication {
    private static final Logger logger = Logger.getLogger(DemoApplication.class.getName());    
    private static PresentationTools presentationTools = new PresentationTools();

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);

        // Stdio Server Transport (Support for SSE also available)
        var transportProvider = new StdioServerTransportProvider(new ObjectMapper());
        
        // Sync tool specification
        var syncToolSpecification = getSyncToolSpecification();

        // Create a server with custom configuration
        McpSyncServer syncServer = McpServer.sync(transportProvider)
                .serverInfo("javaone-mcp-server", "0.0.1")
                .capabilities(McpSchema.ServerCapabilities.builder()
                        .tools(true)
                        .logging()
                        .build())
                // Register tools, resources, and prompts
                .tools(syncToolSpecification)
                .build();

        logger.info("Starting JavaOne MCP Server...");
    }

    private static McpServerFeatures.SyncToolSpecification getSyncToolSpecification() {
        var schema = """
            {
              "type" : "object",
              "id" : "urn:jsonschema:Operation",
              "properties" : {
                "operation" : {
                  "type" : "string"
                }
              }
            }
            """;

        var syncToolSpecification = new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("get_presentations", "Get a list of all presentations from JavaOne", schema),
                (exchange, arguments) -> {
                    // Tool implementation
                    List<Presentation> presentations = presentationTools.getPresentations(); // ✅ works now
                    List<McpSchema.Content> contents = new ArrayList<>();
                    for (Presentation presentation : presentations) {
                        contents.add(new McpSchema.TextContent(presentation.toString()));
                    }
                    return new McpSchema.CallToolResult(contents, false);
                }
        );
        return syncToolSpecification;
    }
}
