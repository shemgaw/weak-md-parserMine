package com.github.arena.challenges.weakmdparser;

public class MarkdownParser {

    String parse(String markdown) {
        String[] cutLines = markdown.split("\n");
        String result = "";
        boolean isUlTagOpened = false;

        for (String cutLine : cutLines) {

            String modifiedLine = convertToHeaderTagIfPresent(cutLine);

            if (modifiedLine == null) {
                modifiedLine = convertToListItemTagIfPresent(cutLine);
            }

            if (modifiedLine == null) {
                modifiedLine = convertToParagraphTag(cutLine);
            }

            if (modifiedLine.matches("(<li>).*") && !isUlTagOpened) {
                isUlTagOpened = true;
                result = result + "<ul>";
                result = result + modifiedLine;
            } else if (!modifiedLine.matches("(<li>).*") && isUlTagOpened) {
                isUlTagOpened = false;
                result = result + "</ul>";
                result = result + modifiedLine;
            } else {
                result = result + modifiedLine;
            }
        }

        if (isUlTagOpened) {
            result = result + "</ul>";
        }

        return result;
    }

    protected String convertToHeaderTagIfPresent(String markdown) {
        int count = 0;

        for (int i = 0; i < markdown.length() && markdown.charAt(i) == '#'; i++) {
            count++;
        }

        return count == 0 ? null : "<h" + count + ">" + markdown.substring(count + 1) + "</h" + count + ">";
    }

    public String convertToListItemTagIfPresent(String markdown) {
        if (markdown.startsWith("*")) {
            String skipAsterisk = markdown.substring(2);
            String listItemString = convertToItalicAndBoldTag(skipAsterisk);
            return "<li>" + listItemString + "</li>";
        }

        return null;
    }

    public String convertToParagraphTag(String markdown) {
        return "<p>" + convertToItalicAndBoldTag(markdown) + "</p>";
    }

    public String convertToItalicAndBoldTag(String markdown) {

        String boldMatchRegex = "__(.+)__";
        String italicMatchRegex = "_(.+)_";
        String converted = markdown.replaceAll(boldMatchRegex, "<strong>$1</strong>");
        return converted.replaceAll(italicMatchRegex, "<em>$1</em>");
    }
}
