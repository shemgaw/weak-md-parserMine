package com.github.arena.challenges.weakmdparser;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MarkdownParserMine {

    public String parse(String markdown) {

        List<String> afterHeaderTagsConversionList = convertToHeaderTagsIfPresent(markdown.lines());

        List<String> afterBulletPointsConversionList = convertToListBulletPointTag(afterHeaderTagsConversionList);

        List<String> afterParagraphConversionList = convertToParagraphTag(afterBulletPointsConversionList);

        convertToListTag(afterParagraphConversionList);

        List<String> afterListTagBoldTagAndItalicTagConversionList = convertToBoldAndItalicTags(afterParagraphConversionList);

        return String.join("", afterListTagBoldTagAndItalicTagConversionList);
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

    private static List<String> convertToListBulletPointTag(List<String> cutLinesList) {
        return cutLinesList.stream()
                .map(cutLine -> cutLine.replaceAll("^[*]\s", "<li>"))
                .map(cutLine -> cutLine.startsWith("<li>") ? cutLine + "</li>" : cutLine)
                .collect(Collectors.toList());
    }

    private static List<String> convertToParagraphTag(List<String> cutLinesList) {
        return cutLinesList.stream()
                .map(cutLine -> cutLine.matches("^<h[1-6]>.+|^<li>.+") ? cutLine : "<p>" + cutLine + "</p>")
                .collect(Collectors.toList());
    }

    private static void convertToListTag(List<String> listOfLines) {
        boolean isULTagOpened = false;

        for (int i = 0; i < listOfLines.size(); i++) {

            if (listOfLines.get(i).startsWith("<li>") && !isULTagOpened) {
                listOfLines.set(i, "<ul>" + listOfLines.get(i));
                isULTagOpened = true;
            } else if (!listOfLines.get(i).startsWith("<li>") && isULTagOpened) {
                listOfLines.set(i - 1, listOfLines.get(i - 1) + "</ul>");
                isULTagOpened = false;
            }
        }

        if (isULTagOpened) {
            listOfLines.add("</ul>");
        }
    }

    private static List<String> convertToBoldAndItalicTags(List<String> boldAndItalicToBeChanged) {
        return boldAndItalicToBeChanged.stream()
                .map(cutLine -> cutLine.replaceAll("__(.+)__", "<strong>$1</strong>"))
                .map(cutLine -> cutLine.replaceAll("_(.+)_", "<em>$1</em>"))
                .collect(Collectors.toList());
    }
}
