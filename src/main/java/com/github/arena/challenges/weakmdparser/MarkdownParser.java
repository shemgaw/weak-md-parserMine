package com.github.arena.challenges.weakmdparser;

public class MarkdownParser {

    public String parse(String markdown) {
        String[] cutLines = markdown.split("\n");
        StringBuilder finalResult = new StringBuilder();
        boolean isUlTagOpened = false;

        for (String cutLine : cutLines) {

            String modifiedLine = convertToHeaderTagIfPresent(cutLine);         //if header then no bold neither italic

            if (modifiedLine.equals(cutLine)) {
                modifiedLine = convertToBulletPointTagOrParagraphTag(cutLine);     //if no header, then either bullet point or paragraph
            }                                                                   //and bold or italic

            modifiedLine = convertToItalicOrBoldTag(modifiedLine);

            if (modifiedLine.startsWith("<li>") && !isUlTagOpened) {            //we convert to list tag
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

    private static String convertToHeaderTagIfPresent(String markdown) {
        int headerSize = 0;

        for (int i = 0; i < markdown.length() && markdown.charAt(i) == '#'; i++) {
            headerSize++;
        }

        return headerSize == 0 ? markdown : "<h" + headerSize + ">" + markdown.substring(headerSize + 1) + "</h" + headerSize + ">";
    }

    private static String convertToBulletPointTagOrParagraphTag(String markdown) {
        if (markdown.startsWith("*")) {
            String skipAsterisk = markdown.substring(2);
            return "<li>" + skipAsterisk + "</li>";
        }
        return "<p>" + markdown + "</p>";
    }

    private static String convertToItalicOrBoldTag(String markdown) {

        return markdown.replaceAll("__(.+)__", "<strong>$1</strong>")
                .replaceAll("_(.+)_", "<em>$1</em>");
    }
}
