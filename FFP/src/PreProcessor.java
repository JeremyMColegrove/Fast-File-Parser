public class PreProcessor {
    //DIRECTIVE VARIABLES HERE
    public boolean AUTONEWLINE = false;
    public boolean PRINTTREE = false;
    public boolean PRINTTOKENS = false;

    public Integer TIMEOUT = -1;

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
                String[] words = line.substring(1).split(" ");
                String directive = words[0];
                switch (directive) {
                    case "AUTONEWLINE":
                        AUTONEWLINE = true;
                        break;
                    case "PRINTTREE":
                        PRINTTREE = true;
                        break;
                    case "PRINTTOKENS":
                        PRINTTOKENS = true;
                        break;
                    case "TIMEOUT":
                        TIMEOUT = Integer.parseInt(words[1]);
                        break;
                    default:
                        throw new RuntimeException("Unknown directive " + directive);
                }
            } else {
                readingDirectives = false;
                if (!isBlank(line)) {
                    result.append(line + "\n");
                }
            }
        }
        return result.toString();
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
