package com.github.osphuhula.core.templates;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Templates {

    private Templates() {}

    /**
     * Pandoc HTML template that renders ONLY $toc$ (no $body$).
     */
    public static void writeTocOnlyHtmlTemplate(Path templatePath) throws Exception {
        if (Files.exists(templatePath)) return;

        String template = """
                $if(title)$
                <!doctype html>
                <html lang="pt-br">
                <head>
                  <meta charset="utf-8">
                  <meta name="viewport" content="width=device-width,initial-scale=1">
                  <title>$title$</title>
                </head>
                <body>
                  <h1>$title$</h1>
                  $toc$
                </body>
                </html>
                $else$
                <!doctype html>
                <html lang="pt-br">
                <head>
                  <meta charset="utf-8">
                  <meta name="viewport" content="width=device-width,initial-scale=1">
                  <title>Indice</title>
                </head>
                <body>
                  <h1>Indice</h1>
                  $toc$
                </body>
                </html>
                $endif$
                """;

        Files.createDirectories(templatePath.getParent());
        Files.writeString(templatePath, template, StandardCharsets.UTF_8);
    }
}
