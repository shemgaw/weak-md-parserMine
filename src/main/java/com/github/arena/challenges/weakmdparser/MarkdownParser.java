package com.github.arena.challenges.weakmdparser;

public class MarkdownParser {
    String parse(String markdown) {
        String[] lines = markdown.split("\n");
        StringBuilder result = new StringBuilder();
        boolean activeList = false;

        for (String line : lines) {
            String parsedLine = parseHeader(line);

            if (parsedLine == null) {
                parsedLine = parseLi(line);
            }

            if (parsedLine == null) {
                parsedLine = parseParagraph(line);
            }

            if (parsedLine.matches("(<li>).*") && !parsedLine.matches("(<h).*") && !parsedLine.matches("(<p>).*") && !activeList) {
                activeList = true;
                result.append("<ul>");
                result.append(parsedLine);
            } else if (!parsedLine.matches("(<li>).*") && activeList) {
                activeList = false;
                result.append("</ul>");
                result.append(parsedLine);
            } else {
                result.append(parsedLine);
            }
        }

        if (activeList) {
            result.append("</ul>");
        }

        return result.toString();
    }

    private String parseHeader(String markdown) {
        int headerLevel = 0;

        for (int i = 0; i < markdown.length() && markdown.charAt(i) == '#'; i++) {
            headerLevel++;
        }

        if (headerLevel == 0) {
            return null;
        }

        return "<h" + headerLevel + ">" + markdown.substring(headerLevel + 1) + "</h" + headerLevel + ">";
    }

    private String parseLi(String markdown) {
        if (markdown.startsWith("*")) {
            String skipAsterisk = markdown.substring(2);
            String listItemString = parseSomeSymbols(skipAsterisk);
            return "<li>" + listItemString + "</li>";
        }

        return null;
    }

    private String parseParagraph(String markdown) {
        return "<p>" + parseSomeSymbols(markdown) + "</p>";
    }

    private String parseSomeSymbols(String markdown) {
        String lookingFor = "__(.+)__";
        String update = "<strong>$1</strong>";
        String workingOn = markdown.replaceAll(lookingFor, update);

        lookingFor = "_(.+)_";
        update = "<em>$1</em>";
        return workingOn.replaceAll(lookingFor, update);
    }
}
