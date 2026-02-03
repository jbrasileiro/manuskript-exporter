package com.github.osphuhula.core;


import com.github.osphuhula.core.exec.ManuskriptCopyFileRunner;
import com.github.osphuhula.core.exec.ExternalToolRunner;
import com.github.osphuhula.core.exec.FileContentAssemblerRunner;
import com.github.osphuhula.core.exec.PandocRunner;
import com.github.osphuhula.core.exec.PdfLatexRunner;
import com.github.osphuhula.core.fs.Files2;
import com.github.osphuhula.core.templates.Templates;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ManuskriptExportPipeline {

    private final ExternalToolRunner toolRunner = new ExternalToolRunner();
    private final PandocRunner pandoc = new PandocRunner(toolRunner);
    private final PdfLatexRunner pdflatex = new PdfLatexRunner(toolRunner);
    private final ManuskriptCopyFileRunner manuskriptCopyFile = new ManuskriptCopyFileRunner(toolRunner);
    private final FileContentAssemblerRunner fileContentAssembler = new FileContentAssemblerRunner();

    public void run(ExportConfig cfg) throws Exception {
        Files2.ensureDir(cfg.outDir());

        Files2.requireFile(cfg.inputMd(), "Input Markdown not found: " + cfg.inputMd());
        Files2.requireFile(cfg.pandocExe(), "Pandoc not found: " + cfg.pandocExe());
        if (cfg.exportPdf()) {
            Files2.requireFile(cfg.pdflatexExe(), "pdflatex not found: " + cfg.pdflatexExe());
        }

        // 1) Optional: HTML only with index (TOC-only)
        if (cfg.exportIndexHtmlOnly()) {
            Templates.writeTocOnlyHtmlTemplate(cfg.tocTemplatePath());
            pandoc.exportIndexHtmlOnly(cfg);
        }

        // 2) Normalized MD
        if (cfg.exportMdNormalized()) {
            pandoc.exportMarkdownNormalized(cfg);
        }

        // 3) TXT
        if (cfg.exportTxt()) {
            pandoc.exportTxt(cfg);
        }

        // 4) HTML full
        if (cfg.exportHtml()) {
            pandoc.exportHtml(cfg);
        }

        // 5) TEX
        if (cfg.exportTex() || cfg.exportPdf()) {
            pandoc.exportTex(cfg);
        }

        // 6) PDF (2 passes)
        if (cfg.exportPdf()) {
            pdflatex.compileTwice(cfg);
        }
        
        // 7) Copy all files from Manuskript directory
        manuskriptCopyFile.copy(cfg);
        
        // 8) Generate bundle-all.txt
        fileContentAssembler.assemble(cfg);

        log.info("Generated outputs in {}", cfg.outDir());
    }
}
