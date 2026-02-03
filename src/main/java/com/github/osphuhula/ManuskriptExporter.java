package com.github.osphuhula;

import com.github.osphuhula.core.ExportConfig;
import com.github.osphuhula.core.ManuskriptExportPipeline;
import com.github.osphuhula.core.cli.ArgsParser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ManuskriptExporter {

    public static void main(String[] args) {
    	String[] parameters = {
    			"--project-dir", "E:\\.rep\\manuskript\\book-CronicasBrunicia\\v8.0.0-20260126",
    			"--export-dir", "E:\\.rep\\manuskript\\book-CronicasBrunicia\\v8.0.0-20260126\\export",
    			"--manuskript-dir", "E:\\.rep\\manuskript\\book-CronicasBrunicia\\v8.0.0-20260126\\export\\manuskript",
    			"--input", "E:\\.rep\\manuskript\\book-CronicasBrunicia\\v8.0.0-20260126\\export\\current.md",
                "--out",   "E:\\\\.rep\\\\manuskript\\\\book-CronicasBrunicia\\\\v8.0.0-20260126\\\\export",
                "--pandoc", "E:\\.app\\Pandoc\\pandoc.exe",
                "--pdflatex", "C:\\Users\\jbrasileiro\\AppData\\Local\\Programs\\MiKTeX\\miktex\\bin\\x64\\pdflatex.exe",
                "--name", "CronicasBrunicia",
                "--pdf", "--html", "--txt", "--tex", "--md",
                "--index-html",
                "--toc-depth", "2",
                "--title", "Cronicas Brunicia — Indice"
            };
        try {
            ExportConfig config = ArgsParser.parse(parameters);

            ManuskriptExportPipeline pipeline = new ManuskriptExportPipeline();
            pipeline.run(config);

            log.info("OK. Output directory: {}", config.outDir());
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: " + e.getMessage());
            System.err.println();
            System.err.println(ArgsParser.usage());
            System.exit(2);
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
