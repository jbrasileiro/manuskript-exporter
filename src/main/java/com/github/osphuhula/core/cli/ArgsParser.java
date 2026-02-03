package com.github.osphuhula.core.cli;

import com.github.osphuhula.core.ExportConfig;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class ArgsParser {

    private ArgsParser() {}

    public static String usage() {
        return """
                Usage:
                  java -jar exporter.jar \\
                    --input <path.md> \\
                    --out <dir> \\
                    --pandoc <pandoc.exe> \\
                    [--pdflatex <pdflatex.exe>] \\
                    --name <baseName> \\
                    [--pdf] [--html] [--txt] [--tex] [--md] \\
                    [--index-html] [--toc-depth <1..6>] [--title <string>]

                Examples:
                  java -jar exporter.jar --input CronicasBrunicia.md --out dist --pandoc E:\\.app\\Pandoc\\pandoc.EXE --pdflatex C:\\...\\pdflatex.exe --name CronicasBrunicia --pdf --html --txt --tex --md --index-html --toc-depth 2 --title "Cronicas — Indice"

                Notes:
                  - --pdflatex is required only if --pdf is enabled.
                  - --index-html generates dist/indice.html containing ONLY the table of contents.
                """;
    }

    public static ExportConfig parse(String[] args) {
        Map<String, String> kv = new HashMap<>();
        boolean pdf = false, html = false, txt = false, tex = false, md = false, indexHtml = false;
        int tocDepth = 2;
        String title = "Indice";

        for (int i = 0; i < args.length; i++) {
            String a = args[i].trim();
            switch (a) {
                case "--pdf" -> pdf = true;
                case "--html" -> html = true;
                case "--txt" -> txt = true;
                case "--tex" -> tex = true;
                case "--md" -> md = true;
                case "--index-html" -> indexHtml = true;
                case "--toc-depth" -> {
                    if (i + 1 >= args.length) throw new IllegalArgumentException("Missing value for --toc-depth");
                    tocDepth = Integer.parseInt(args[++i]);
                }
                case "--title" -> {
                    if (i + 1 >= args.length) throw new IllegalArgumentException("Missing value for --title");
                    title = args[++i];
                }
                default -> {
                    if (a.startsWith("--")) {
                        if (i + 1 >= args.length) throw new IllegalArgumentException("Missing value for " + a);
                        kv.put(a, args[++i]);
                    } else {
                        throw new IllegalArgumentException("Unknown arg: " + a);
                    }
                }
            }
        }

        Path projectDir = pathReq(kv, "--project-dir");
        Path exportDir = pathReq(kv, "--export-dir");
        Path manuskriptFileDir = pathReq(kv, "--manuskript-dir");
        Path input = pathReq(kv, "--input");
        Path out = pathReq(kv, "--out");
        Path pandoc = pathReq(kv, "--pandoc");
        Path pdflatex = kv.containsKey("--pdflatex") ? Path.of(kv.get("--pdflatex")) : null;

        String name = kv.get("--name");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Missing --name");

        // If no outputs specified, default to: --pdf --html --txt --tex --md --index-html
        boolean any = pdf || html || txt || tex || md || indexHtml;
        if (!any) {
            pdf = html = txt = tex = md = indexHtml = true;
        }

        return ExportConfig.builder()
        		.projectDir(projectDir)
        		.exportDir(exportDir)
        		.manuskriptFileDir(manuskriptFileDir)
                .inputMd(input)
                .outDir(out)
                .pandocExe(pandoc)
                .pdflatexExe(pdflatex)
                .baseName(name)
                .exportPdf(pdf)
                .exportHtml(html)
                .exportTxt(txt)
                .exportTex(tex)
                .exportMdNormalized(md)
                .exportIndexHtmlOnly(indexHtml)
                .tocDepth(tocDepth)
                .title(title)
                .build();
    }

    private static Path pathReq(Map<String, String> kv, String key) {
        String v = kv.get(key);
        if (v == null || v.isBlank()) throw new IllegalArgumentException("Missing " + key);
        return Path.of(v);
    }
}
