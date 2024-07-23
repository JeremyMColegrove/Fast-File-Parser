public class PreProcessor {
    //DIRECTIVE VARIABLES HERE
    public boolean AUTONEWLINE = false;
    public boolean SHOWTREE = false;
    public boolean SHOWTOKENS = false;

    public PreProcessor() {

    }

    public String process(String code) {
        // process the code
        code = code.trim();
        String[] lines = code.split("\n");
        StringBuilder result = new StringBuilder();
        boolean readingDirectives = true;
        for (int i=0; i<lines.length; i++) {
            String line = lines[i].trim();
            // scan for directives
            if (readingDirectives && line.startsWith("@")) {
                // read this directive
                String directive = line.substring(1);
                switch (directive) {
                    case "AUTONEWLINE":
                        AUTONEWLINE = true;
                        break;
                    case "SHOWTREE":
                        SHOWTREE = true;
                        break;
                    case "SHOWTOKENS":
                        SHOWTOKENS = true;
                        break;
                    default:
                        throw new RuntimeException("Unknown directive " + directive);
                }
            } else {
                readingDirectives = false;
                if (!line.isBlank()) {
                    result.append(line + "\n");
                }
            }
        }
        return result.toString();
    }
}
