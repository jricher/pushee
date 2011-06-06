package org.mitre.jdbc.datasource.util;

import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Matt Franklin
 *
 * Parses a file looking for create, alter, insert, update, delete or drop commands and appends them to an output
 * string or follows @@ syntax to read child scripts.
 *
 */
public class SqlFileParser {
    private static final Pattern WORD_PATTERN = Pattern.compile("^([a-zA-Z]*)[ ;]");
    private static final String CHILD_SCRIPT_INDICATOR = "@@";
    private static final Set<String> commandSet;

    static {
        commandSet = new HashSet<String>();
        commandSet.add("create");
        commandSet.add("alter");
        commandSet.add("insert");
        commandSet.add("update");
        commandSet.add("drop");
        commandSet.add("delete");
        commandSet.add("commit");
        commandSet.add("set");
        commandSet.add("truncate");
        commandSet.add("rollback");
    }

    private enum State {
        INIT,
        READFILE,
        READSQL
    }
    private Stack<State> stateStack;
    private Resource resource;

    /**
     * Constructor takes a Spring {@link Resource}
     * @param resource the initial file to parse
     */
    public SqlFileParser(Resource resource) {
        stateStack = new Stack<State>();
        this.resource = resource;
    }

    /**
     * Gets the executable SQL statements from the resource passed to the constructor and its children
     * @return a valid executable string containing SQL statements
     * @throws IOException if the resource or its children are not found
     */
    public String getSQL() throws IOException {
        return processResource(resource);
    }

    private String processResource(Resource res) throws IOException {
        StringBuilder sql = new StringBuilder();
        File resourceFile = res.getFile();
        stateStack.push(State.INIT);
        processFile(resourceFile, sql);
        stateStack.pop();
        return sql.toString();
    }

    private void processFile(File file, StringBuilder sql) throws IOException{
        BufferedReader fileReader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
        String line = null;
        while((line = fileReader.readLine()) != null) {
            processLine(sql, line);
        }
    }

    private void processLine(StringBuilder sql, String line) throws IOException{
        String lowerLine = line.toLowerCase().trim();
        switch(stateStack.peek()) {
            case INIT : {
                if(lowerLine.startsWith(CHILD_SCRIPT_INDICATOR)) {
                    //replace the current element in the stack with the new state
                    stateStack.pop();
                    stateStack.push(State.READFILE);
                    processLine(sql, line);
                } else if(commandSet.contains(getFirstWord(lowerLine))) {

                    //replace the current element in the stack with the new state
                    stateStack.pop();
                    stateStack.push(State.READSQL);
                    processLine(sql, line);
                }
                break;
            }
            case READFILE : {
                stateStack.push(State.INIT);
                Resource child = resource.createRelative(line.replace(CHILD_SCRIPT_INDICATOR, ""));
                sql.append(processResource(child));
                //Read File lines do not have a terminal character but are by definition only one line Long
                stateStack.pop();
                stateStack.push(State.INIT);
                break;
            }
            case READSQL : {
                sql.append(line);
                //add a space to accommodate line breaks.  Not a big deal if extraneous spaces are added
                sql.append(" ");
                if(lowerLine.endsWith(";")) {
                    stateStack.pop();
                    stateStack.push(State.INIT);
                }
                break;
            }
            default: {
                throw new RuntimeException("Invalid State");
            }
        }
    }

    private static String getFirstWord(String line) {
        Matcher match = WORD_PATTERN.matcher(line);
        return match.find() ? match.group(1) : null;
    }
}
