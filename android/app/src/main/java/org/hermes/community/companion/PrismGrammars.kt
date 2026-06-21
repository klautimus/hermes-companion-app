package org.hermes.community.companion

import io.noties.prism4j.Prism4j

/**
 * Prism4j grammar definitions for syntax highlighting.
 * Each function creates a language grammar from regex patterns.
 */

internal fun grammarForKotlin(prism4j: Prism4j): Prism4j.Grammar {
    return Prism4j.grammar("kotlin",
        Prism4j.token("comment", Prism4j.pattern(java.util.regex.Pattern.compile("//[^\\n]*"))),
        Prism4j.token("comment", Prism4j.pattern(java.util.regex.Pattern.compile("/\\*[\\s\\S]*?\\*/"))),
        Prism4j.token("string", Prism4j.pattern(java.util.regex.Pattern.compile("\"(?:[^\"\\\\]|\\\\.)*\""))),
        Prism4j.token("string", Prism4j.pattern(java.util.regex.Pattern.compile("\"\"\"[\\s\\S]*?\"\"\""))),
        Prism4j.token("keyword", Prism4j.pattern(java.util.regex.Pattern.compile("\\b(?:abstract|actual|annotation|as|break|by|catch|class|companion|const|constructor|continue|crossinline|data|do|dynamic|else|enum|expect|external|final|finally|for|fun|get|if|import|in|infix|init|inline|inner|interface|internal|is|lateinit|noinline|null|object|open|operator|out|override|package|private|protected|public|reified|return|sealed|set|super|suspend|tailrec|this|throw|to|try|typealias|val|var|vararg|when|where|while)\\b"))),
        Prism4j.token("function", Prism4j.pattern(java.util.regex.Pattern.compile("\\w+(?=\\s*\\()"))),
        Prism4j.token("number", Prism4j.pattern(java.util.regex.Pattern.compile("\\b(?:0[xX][\\da-fA-F]+|0[bB][01]+|\\d+\\.?\\d*(?:[eE][+-]?\\d+)?)\\b"))),
        Prism4j.token("annotation", Prism4j.pattern(java.util.regex.Pattern.compile("@\\w+"))),
        Prism4j.token("operator", Prism4j.pattern(java.util.regex.Pattern.compile("[+\\-*/%<>!=]=?|&&|\\|\\||[?:]")))
    )
}

internal fun grammarForJava(prism4j: Prism4j): Prism4j.Grammar {
    return Prism4j.grammar("java",
        Prism4j.token("comment", Prism4j.pattern(java.util.regex.Pattern.compile("//[^\\n]*"))),
        Prism4j.token("comment", Prism4j.pattern(java.util.regex.Pattern.compile("/\\*[\\s\\S]*?\\*/"))),
        Prism4j.token("string", Prism4j.pattern(java.util.regex.Pattern.compile("\"(?:[^\"\\\\]|\\\\.)*\""))),
        Prism4j.token("keyword", Prism4j.pattern(java.util.regex.Pattern.compile("\\b(?:abstract|assert|boolean|break|byte|case|catch|char|class|const|continue|default|do|double|else|enum|extends|final|finally|float|for|goto|if|implements|import|instanceof|int|interface|long|native|new|package|private|protected|public|return|short|static|strictfp|super|switch|synchronized|this|throw|throws|transient|try|void|volatile|while)\\b"))),
        Prism4j.token("number", Prism4j.pattern(java.util.regex.Pattern.compile("\\b(?:0[xX][\\da-fA-F]+|0[bB][01]+|\\d+\\.?\\d*(?:[eE][+-]?\\d+)?)\\b"))),
        Prism4j.token("annotation", Prism4j.pattern(java.util.regex.Pattern.compile("@\\w+")))
    )
}

internal fun grammarForPython(prism4j: Prism4j): Prism4j.Grammar {
    return Prism4j.grammar("python",
        Prism4j.token("comment", Prism4j.pattern(java.util.regex.Pattern.compile("#[^\\n]*"))),
        Prism4j.token("string", Prism4j.pattern(java.util.regex.Pattern.compile("\"\"\"[\\s\\S]*?\"\"\"|'''[\\s\\S]*?'''"))),
        Prism4j.token("string", Prism4j.pattern(java.util.regex.Pattern.compile("\"(?:[^\"\\\\]|\\\\.)*\"|'(?:[^'\\\\]|\\\\.)*'"))),
        Prism4j.token("keyword", Prism4j.pattern(java.util.regex.Pattern.compile("\\b(?:and|as|assert|async|await|break|class|continue|def|del|elif|else|except|finally|for|from|global|if|import|in|is|lambda|nonlocal|not|or|pass|raise|return|try|while|with|yield)\\b"))),
        Prism4j.token("builtin", Prism4j.pattern(java.util.regex.Pattern.compile("\\b(?:__import__|abs|all|any|bool|dict|dir|enumerate|eval|exec|filter|float|frozenset|globals|hasattr|hash|help|hex|id|input|int|isinstance|issubclass|iter|len|list|map|max|min|next|object|oct|open|ord|pow|print|range|repr|reversed|round|set|sorted|str|sum|super|tuple|type|vars|zip)\\b"))),
        Prism4j.token("number", Prism4j.pattern(java.util.regex.Pattern.compile("\\b(?:0[xX][\\da-fA-F]+|0[bB][01]+|0[oO][0-7]+|\\d+\\.?\\d*(?:[eE][+-]?\\d+)?)\\b"))),
        Prism4j.token("decorator", Prism4j.pattern(java.util.regex.Pattern.compile("@\\w+"))),
        Prism4j.token("function", Prism4j.pattern(java.util.regex.Pattern.compile("\\w+(?=\\s*\\()")))
    )
}

internal fun grammarForJson(prism4j: Prism4j): Prism4j.Grammar {
    return Prism4j.grammar("json",
        Prism4j.token("property", Prism4j.pattern(java.util.regex.Pattern.compile("\"(?:[^\"\\\\]|\\\\.)*\"(?=\\s*:)"))),
        Prism4j.token("string", Prism4j.pattern(java.util.regex.Pattern.compile("\"(?:[^\"\\\\]|\\\\.)*\""))),
        Prism4j.token("number", Prism4j.pattern(java.util.regex.Pattern.compile("-?(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[eE][+-]?\\d+)?"))),
        Prism4j.token("boolean", Prism4j.pattern(java.util.regex.Pattern.compile("\\b(?:true|false)\\b"))),
        Prism4j.token("null", Prism4j.pattern(java.util.regex.Pattern.compile("\\bnull\\b")))
    )
}

internal fun grammarForBash(prism4j: Prism4j): Prism4j.Grammar {
    return Prism4j.grammar("bash",
        Prism4j.token("comment", Prism4j.pattern(java.util.regex.Pattern.compile("#[^\\n]*"))),
        Prism4j.token("string", Prism4j.pattern(java.util.regex.Pattern.compile("\"(?:[^\"\\\\]|\\\\.)*\""))),
        Prism4j.token("string", Prism4j.pattern(java.util.regex.Pattern.compile("'[^']*'"))),
        Prism4j.token("keyword", Prism4j.pattern(java.util.regex.Pattern.compile("\\b(?:if|then|else|elif|fi|for|while|do|done|case|esac|function|return|exit|export|unset|readonly|shift|source|alias|break|continue|eval|exec|local|declare|typeset|trap|wait)\\b"))),
        Prism4j.token("variable", Prism4j.pattern(java.util.regex.Pattern.compile("\\$\\{?[a-zA-Z_][a-zA-Z0-9_]*\\}?"))),
        Prism4j.token("builtin", Prism4j.pattern(java.util.regex.Pattern.compile("\\b(?:echo|cd|pwd|ls|cat|grep|sed|awk|sort|uniq|wc|head|tail|cp|mv|rm|mkdir|chmod|chown|find|xargs|tee|cut|tr|printf|read|test|expr|let|true|false)\\b")))
    )
}

internal fun grammarForYaml(prism4j: Prism4j): Prism4j.Grammar {
    return Prism4j.grammar("yaml",
        Prism4j.token("comment", Prism4j.pattern(java.util.regex.Pattern.compile("#[^\\n]*"))),
        Prism4j.token("key", Prism4j.pattern(java.util.regex.Pattern.compile("^[ \\t]*[\\w.-]+(?=\\s*:)", java.util.regex.Pattern.MULTILINE))),
        Prism4j.token("string", Prism4j.pattern(java.util.regex.Pattern.compile("\"(?:[^\"\\\\]|\\\\.)*\"|'(?:[^'\\\\]|\\\\.)*'"))),
        Prism4j.token("number", Prism4j.pattern(java.util.regex.Pattern.compile("\\b(?:0[xX][\\da-fA-F]+|\\d+\\.?\\d*(?:[eE][+-]?\\d+)?)\\b"))),
        Prism4j.token("boolean", Prism4j.pattern(java.util.regex.Pattern.compile("\\b(?:true|false|yes|no|on|off)\\b", java.util.regex.Pattern.CASE_INSENSITIVE))),
        Prism4j.token("null", Prism4j.pattern(java.util.regex.Pattern.compile("\\b(?:null|~)\\b"))),
        Prism4j.token("punctuation", Prism4j.pattern(java.util.regex.Pattern.compile("[:\\-]")))
    )
}

internal fun grammarForJavascript(prism4j: Prism4j): Prism4j.Grammar {
    return Prism4j.grammar("javascript",
        Prism4j.token("comment", Prism4j.pattern(java.util.regex.Pattern.compile("//[^\\n]*"))),
        Prism4j.token("comment", Prism4j.pattern(java.util.regex.Pattern.compile("/\\*[\\s\\S]*?\\*/"))),
        Prism4j.token("string", Prism4j.pattern(java.util.regex.Pattern.compile("`(?:[^`\\\\]|\\\\.)*`"))),
        Prism4j.token("string", Prism4j.pattern(java.util.regex.Pattern.compile("\"(?:[^\"\\\\]|\\\\.)*\"|'(?:[^'\\\\]|\\\\.)*'"))),
        Prism4j.token("keyword", Prism4j.pattern(java.util.regex.Pattern.compile("\\b(?:as|async|await|break|case|catch|class|const|continue|debugger|default|delete|do|else|enum|export|extends|finally|for|from|function|get|if|implements|import|in|instanceof|interface|let|new|null|of|package|private|protected|public|return|set|static|super|switch|this|throw|try|typeof|var|void|while|with|yield)\\b"))),
        Prism4j.token("number", Prism4j.pattern(java.util.regex.Pattern.compile("\\b(?:0[xX][\\da-fA-F]+|0[bB][01]+|\\d+\\.?\\d*(?:[eE][+-]?\\d+)?)\\b"))),
        Prism4j.token("function", Prism4j.pattern(java.util.regex.Pattern.compile("\\w+(?=\\s*\\()"))),
        Prism4j.token("operator", Prism4j.pattern(java.util.regex.Pattern.compile("[+\\-*/%<>!=]=?|&&|\\|\\||[?:]|\\?\\.|\\.{3}")))
    )
}

internal fun grammarForGo(prism4j: Prism4j): Prism4j.Grammar {
    return Prism4j.grammar("go",
        Prism4j.token("comment", Prism4j.pattern(java.util.regex.Pattern.compile("//[^\\n]*"))),
        Prism4j.token("comment", Prism4j.pattern(java.util.regex.Pattern.compile("/\\*[\\s\\S]*?\\*/"))),
        Prism4j.token("string", Prism4j.pattern(java.util.regex.Pattern.compile("\"(?:[^\"\\\\]|\\\\.)*\""))),
        Prism4j.token("string", Prism4j.pattern(java.util.regex.Pattern.compile("`[^`]*`"))),
        Prism4j.token("keyword", Prism4j.pattern(java.util.regex.Pattern.compile("\\b(?:break|case|chan|const|continue|default|defer|else|fallthrough|for|func|go|goto|if|import|interface|map|package|range|return|select|struct|switch|type|var)\\b"))),
        Prism4j.token("builtin", Prism4j.pattern(java.util.regex.Pattern.compile("\\b(?:append|cap|close|complex|copy|delete|imag|len|make|new|panic|print|println|real|recover)\\b"))),
        Prism4j.token("number", Prism4j.pattern(java.util.regex.Pattern.compile("\\b(?:0[xX][\\da-fA-F]+|0[bB][01]+|\\d+\\.?\\d*(?:[eE][+-]?\\d+)?)\\b")))
    )
}

internal fun grammarForMarkdown(prism4j: Prism4j): Prism4j.Grammar {
    return Prism4j.grammar("markdown",
        Prism4j.token("heading", Prism4j.pattern(java.util.regex.Pattern.compile("^#{1,6}\\s+.+$", java.util.regex.Pattern.MULTILINE))),
        Prism4j.token("bold", Prism4j.pattern(java.util.regex.Pattern.compile("\\*\\*(.+?)\\*\\*|__(.+?)__"))),
        Prism4j.token("italic", Prism4j.pattern(java.util.regex.Pattern.compile("\\*(.+?)\\*|_(.+?)_"))),
        Prism4j.token("code", Prism4j.pattern(java.util.regex.Pattern.compile("`[^`]+`"))),
        Prism4j.token("code", Prism4j.pattern(java.util.regex.Pattern.compile("```[\\s\\S]*?```"))),
        Prism4j.token("link", Prism4j.pattern(java.util.regex.Pattern.compile("\\[([^\\]]+)\\]\\(([^\\)]+)\\)"))),
        Prism4j.token("list", Prism4j.pattern(java.util.regex.Pattern.compile("^\\s*[-*+]\\s+|^\\s*\\d+\\.\\s+", java.util.regex.Pattern.MULTILINE))),
        Prism4j.token("blockquote", Prism4j.pattern(java.util.regex.Pattern.compile("^\\s*>\\s+", java.util.regex.Pattern.MULTILINE))),
        Prism4j.token("hr", Prism4j.pattern(java.util.regex.Pattern.compile("^\\s*(?:---+|\\*\\*\\*+|___+)\\s*$", java.util.regex.Pattern.MULTILINE)))
    )
}
