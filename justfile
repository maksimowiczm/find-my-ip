default:
    @just --list

# $KTFMT_JAR - path to the ktfmt jar file
format:
    @find . -type f \( -name "*.kt" -o -name "*.kts" \) -not -path "*/build/*" | xargs java -jar $KTFMT_JAR --kotlinlang-style
