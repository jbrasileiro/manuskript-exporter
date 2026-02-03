package com.github.osphuhula.core;

import lombok.Builder;

import java.nio.file.Path;
import java.util.Objects;

@Builder(toBuilder = true)
public record ExportConfig(
		Path projectDir,
		Path exportDir,
		Path manuskriptFileDir,
        Path inputMd,
        Path outDir,
        Path pandocExe,
        Path pdflatexExe,
        String baseName,
        boolean exportPdf,
        boolean exportHtml,
        boolean exportTxt,
        boolean exportTex,
        boolean exportMdNormalized,
        boolean exportIndexHtmlOnly,
        int tocDepth,
        String title
) {
    public ExportConfig {
        Objects.requireNonNull(inputMd, "inputMd");
        Objects.requireNonNull(outDir, "outDir");
        Objects.requireNonNull(pandocExe, "pandocExe");
        // pdflatexExe may be null if exportPdf=false
        if (exportPdf && pdflatexExe == null) {
            throw new IllegalArgumentException("pdflatexExe is required when --pdf is enabled.");
        }
        if (tocDepth < 1 || tocDepth > 6) {
            throw new IllegalArgumentException("tocDepth must be between 1 and 6.");
        }
        if (baseName == null || baseName.isBlank()) {
            throw new IllegalArgumentException("baseName is required.");
        }
        if (title == null || title.isBlank()) {
            title = "Indice";
        }
    }

	public Path texOut() {
		return outDir.resolve(baseName + ".tex");
	}

	public Path pdfOut() {
		return outDir.resolve(baseName + ".pdf");
	}

	public Path htmlOut() {
		return outDir.resolve(baseName + ".html");
	}

	public Path txtOut() {
		return outDir.resolve(baseName + ".txt");
	}

	public Path mdOut() {
		return outDir.resolve(baseName + ".md");
	}

	public Path indexHtmlOut() {
		return outDir.resolve("indice.html");
	}

	public Path tocTemplatePath() {
		return outDir.resolve("_toc-only.template.html");
	}
}
