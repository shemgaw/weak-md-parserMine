package com.github.arena.challenges.weakmdparser;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MarkdownParserMine {

    public String parse(String markdown) {

        var afterHeaderTagsConversionStream = convertToHeaderTag(markdown.lines());

        var afterBulletPointsConversionStream = convertToListBulletPointTag(afterHeaderTagsConversionStream);

        var afterParagraphConversionStream = convertToParagraphTag(afterBulletPointsConversionStream);

        var afterListTagBoldTagAndItalicTagConversionStream = convertToBoldAndItalicTags(afterParagraphConversionStream);

        var afterListConversionStream = convertToListTag(afterListTagBoldTagAndItalicTagConversionStream);

        return afterListConversionStream.collect(Collectors.joining());
    }

    private static Stream<String> convertToHeaderTag(Stream<String> streamOfCutLines) {

        return streamOfCutLines
                .map(cutLine -> cutLine.replaceAll("^#{6}\s", "<h6>")
                        .replaceAll("^#{5}\s", "<h5>")
                        .replaceAll("^#{4}\s", "<h4>")
                        .replaceAll("^#{3}\s", "<h3>")
                        .replaceAll("^#{2}\s", "<h2>")
                        .replaceAll("^#\s", "<h1>"))
                .map(cutLine -> cutLine.startsWith("<h") ? cutLine + "</h" + cutLine.charAt(2) + ">" : cutLine);
    }

    private static Stream<String> convertToListBulletPointTag(Stream<String> streamOfCutLines) {
        return streamOfCutLines
                .map(cutLine -> cutLine.replaceAll("^[*]\s", "<li>"))
                .map(cutLine -> cutLine.startsWith("<li>") ? cutLine + "</li>" : cutLine);
    }

    private static Stream<String> convertToParagraphTag(Stream<String> streamOfCutLines) {
        return streamOfCutLines
                .map(cutLine -> cutLine.matches("^<h[1-6]>.+|^<li>.+") ? cutLine : "<p>" + cutLine + "</p>");
    }

    private static Stream<String> convertToListTag(Stream<String> streamOfCutLines) {
        List<String> listOfLines = streamOfCutLines.collect(Collectors.toList());
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
        return listOfLines.stream();
    }

    private static Stream<String> convertToBoldAndItalicTags(Stream<String> boldAndItalicToBeChanged) {
        return boldAndItalicToBeChanged
                .map(cutLine -> cutLine.replaceAll("__(.+)__", "<strong>$1</strong>")
                                        .replaceAll("_(.+)_", "<em>$1</em>"));
    }
}
