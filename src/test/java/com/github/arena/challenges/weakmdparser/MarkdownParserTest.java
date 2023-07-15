package com.github.arena.challenges.weakmdparser;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MarkdownParserTest {

    private MarkdownParser markdownParser;
    private MarkdownParserMine markdownParserMine;

    @BeforeEach
    public void setup() {
        markdownParser = new MarkdownParser();
        markdownParserMine = new MarkdownParserMine();
    }

    @Test
    public void normalTextAsAParagraph() {
        String input = "This will be a paragraph";
        String expected = "<p>This will be a paragraph</p>";

        assertEquals(expected, markdownParser.parse(input));
        assertEquals(expected, markdownParserMine.parse(input));
    }

    @Test
    public void italics() {
        String input = "_This will be italic_";
        String expected = "<p><em>This will be italic</em></p>";

        assertEquals(expected, markdownParser.parse(input));
        assertEquals(expected, markdownParserMine.parse(input));
    }

    @Test
    public void boldText() {
        String input = "__This will be bold__";
        String expected = "<p><strong>This will be bold</strong></p>";

        assertEquals(expected, markdownParser.parse(input));
        assertEquals(expected, markdownParserMine.parse(input));
    }

    @Test
    public void normalItalicsAndBoldText() {
        String input = "This will _be_ __mixed__";
        String expected = "<p>This will <em>be</em> <strong>mixed</strong></p>";

        assertEquals(expected, markdownParser.parse(input));
        assertEquals(expected, markdownParserMine.parse(input));
    }

    @Test
    public void withH1HeaderLevel() {
        String input = "# This will be an h1";
        String expected = "<h1>This will be an h1</h1>";

        assertEquals(expected, markdownParser.parse(input));
        assertEquals(expected, markdownParserMine.parse(input));
    }

    @Test
    public void withH2HeaderLevel() {
        String input = "## This will be an h2";
        String expected = "<h2>This will be an h2</h2>";

        assertEquals(expected, markdownParser.parse(input));
        assertEquals(expected, markdownParserMine.parse(input));
    }

    @Test
    public void withH6HeaderLevel() {
        String input = "###### This will be an h6";
        String expected = "<h6>This will be an h6</h6>";

        assertEquals(expected, markdownParser.parse(input));
        assertEquals(expected, markdownParserMine.parse(input));
    }

    @Test
    public void unorderedLists() {
        String input = "* Item 1\n* Item 2";
        String expected = "<ul><li>Item 1</li><li>Item 2</li></ul>";

        assertEquals(expected, markdownParser.parse(input));
        assertEquals(expected, markdownParserMine.parse(input));
    }

    @Test
    public void aLittleBitOfEverything() {
        String input = "# Header!\n* __Bold Item__\n* _Italic Item_";
        String expected = "<h1>Header!</h1><ul><li><strong>Bold Item</strong></li><li><em>Italic Item</em></li></ul>";

        assertEquals(expected, markdownParser.parse(input));
        assertEquals(expected, markdownParserMine.parse(input));
    }

    @Test
    public void markdownSymbolsInTheHeaderShouldNotBeInterpreted() {
        String input = "# This is a header with # and * in the text";
        String expected = "<h1>This is a header with # and * in the text</h1>";

        assertEquals(expected, markdownParser.parse(input));
        assertEquals(expected, markdownParserMine.parse(input));
    }

    @Test
    public void markdownSymbolsInTheListItemTextShouldNotBeInterpreted() {
        String input = "* Item 1 with a # in the text\n* Item 2 with * in the text";
        String expected = "<ul><li>Item 1 with a # in the text</li><li>Item 2 with * in the text</li></ul>";

        assertEquals(expected, markdownParser.parse(input));
        assertEquals(expected, markdownParserMine.parse(input));
    }

    @Test
    public void markdownSymbolsInTheParagraphTextShouldNotBeInterpreted() {
        String input = "This is a paragraph with # and * in the text";
        String expected = "<p>This is a paragraph with # and * in the text</p>";

        assertEquals(expected, markdownParser.parse(input));
        assertEquals(expected, markdownParserMine.parse(input));
    }

    @Test
    public void markdownUnorderedListsCloseProperlyWithPrecedingAndFollowingLines() {
        String input = "# Start a list\n* Item 1\n* Item 2\nEnd a list";
        String expected = "<h1>Start a list</h1><ul><li>Item 1</li><li>Item 2</li></ul><p>End a list</p>";

        assertEquals(expected, markdownParser.parse(input));
        assertEquals(expected, markdownParserMine.parse(input));
    }
}