package com.github.arena.challenges.weakmdparser;

public class MarkdownParser {

    String parse(String markdown) {
        String[] cutLines = markdown.split("\n");
        StringBuilder finalResult = new StringBuilder();
        boolean isUlTagOpened = false;

        for (String cutLine : cutLines) {

            String modifiedLine = convertToHeaderTagIfPresent(cutLine);         //if header then no bold neither italic

            if (modifiedLine.equals(cutLine)) {
                modifiedLine = convertToListItemTagOrParagraphTag(cutLine);     //if no header, then either list or paragraph
            }                                                                   //and bold or italic

            if (modifiedLine.startsWith("<li>") && !isUlTagOpened) {
                isUlTagOpened = true;
                finalResult.append("<ul>").append(modifiedLine);

            } else if (!modifiedLine.startsWith("<li>") && isUlTagOpened) {
                isUlTagOpened = false;
                finalResult.append("</ul>").append(modifiedLine);

            } else {
                finalResult.append(modifiedLine);
            }
        }

        if (isUlTagOpened) {
            finalResult.append("</ul>");
        }

        return finalResult.toString();
    }

    protected String convertToHeaderTagIfPresent(String markdown) {
        int headerSize = 0;

        for (int i = 0; i < markdown.length() && markdown.charAt(i) == '#'; i++) {
            headerSize++;
        }

        return headerSize == 0 ? markdown : "<h" + headerSize + ">" + markdown.substring(headerSize + 1) + "</h" + headerSize + ">";
    }

    public String convertToListItemTagOrParagraphTag(String markdown) {
        if (markdown.startsWith("*")) {
            String skipAsterisk = markdown.substring(2);
            String listItemString = convertToItalicOrBoldTag(skipAsterisk);
            return "<li>" + listItemString + "</li>";
        }
            return "<p>" + convertToItalicOrBoldTag(markdown) + "</p>";
    }

    public String convertToItalicOrBoldTag(String markdown) {

        String boldMatchRegex = "__(.+)__";
        String italicMatchRegex = "_(.+)_";
        String converted = markdown.replaceAll(boldMatchRegex, "<strong>$1</strong>");
        return converted.replaceAll(italicMatchRegex, "<em>$1</em>");
    }
}
