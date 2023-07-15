package com.github.arena.challenges.weakmdparser;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MarkdownParserMine {

    public String parse(String markdown) {
        Stream<String> stringOfCutLines = Stream.of(markdown.split("\n"));

        List<String> cutLinesList = convertToHeaderTagsIfPresent(stringOfCutLines);

        List<String> beforeBoldAndItalicConversion = convertToListOrParagraphTag(cutLinesList);

        return convertToBoldAndItalicTags(beforeBoldAndItalicConversion);
    }

    private static List<String> convertToHeaderTagsIfPresent(Stream<String> streamOfCutLines) {

        return streamOfCutLines
                .map(cutLine -> cutLine.replaceAll("^#{6}\s", "<h6>")
                        .replaceAll("^#{5}\s", "<h5>")
                        .replaceAll("^#{4}\s", "<h4>")
                        .replaceAll("^#{3}\s", "<h3>")
                        .replaceAll("^#{2}\s", "<h2>")
                        .replaceAll("^#\s", "<h1>"))
                .map(cutLine -> cutLine.startsWith("<h") ? cutLine + "</h" + cutLine.charAt(2) + ">" : cutLine)
                .collect(Collectors.toList());
    }

    private static List<String> convertToListOrParagraphTag(List<String> cutLinesList) {
        List<String> listOfLines = cutLinesList.stream()
                .map(cutLine -> cutLine.replaceAll("^[*]\s", "<li>"))
                .map(cutLine -> cutLine.startsWith("<li>") ? cutLine + "</li>" : cutLine)
                .map(cutLine -> cutLine.matches("^<h[1-6]>.+|^<li>.+") ? cutLine : "<p>" + cutLine + "</p>")
                .collect(Collectors.toList());

        boolean isULTagOpened = false;

        for (String line : listOfLines) {

            if (line.startsWith("<li>") && !isULTagOpened) {
                listOfLines.set(listOfLines.indexOf(line), "<ul>" + line);
                isULTagOpened = true;
            } else if (!line.startsWith("<li>") && isULTagOpened) {
                listOfLines.set(listOfLines.indexOf(line), "</ul>" + line);
                isULTagOpened = false;
            }
        }

        if (isULTagOpened) {
            listOfLines.add("</ul>");
        }

        return listOfLines;
    }

    private static String convertToBoldAndItalicTags(List<String> boldAndItalicToBeChanged) {
        return boldAndItalicToBeChanged.stream()
                .map(cutLine -> cutLine.replaceAll("__(.+)__", "<strong>$1</strong>"))
                .map(cutLine -> cutLine.replaceAll("_(.+)_", "<em>$1</em>"))
                .collect(Collectors.joining());
    }
}
