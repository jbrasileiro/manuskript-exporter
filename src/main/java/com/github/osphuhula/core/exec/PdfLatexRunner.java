package com.github.osphuhula.core.exec;

import com.github.osphuhula.core.ExportConfig;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.util.List;

@RequiredArgsConstructor
public final class PdfLatexRunner {

	private final ExternalToolRunner runner;

	public void compileTwice(ExportConfig cfg) throws Exception {
		Path workDir = cfg.outDir();

		List<String> pass1 = List.of(cfg.pdflatexExe().toString(), "-interaction=nonstopmode", "-halt-on-error",
				cfg.texOut().getFileName().toString());
		int c1 = runner.run(workDir, pass1);
		if (c1 != 0)
			throw new RuntimeException("pdflatex pass1 failed with exit code " + c1);

		List<String> pass2 = List.of(cfg.pdflatexExe().toString(), "-interaction=nonstopmode", "-halt-on-error",
				cfg.texOut().getFileName().toString());
		int c2 = runner.run(workDir, pass2);
		if (c2 != 0)
			throw new RuntimeException("pdflatex pass2 failed with exit code " + c2);
	}
}
